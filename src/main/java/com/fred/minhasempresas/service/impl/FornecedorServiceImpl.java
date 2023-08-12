package com.fred.minhasempresas.service.impl;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;

import com.fred.minhasempresas.model.entity.Empresa;
import com.fred.minhasempresas.model.entity.Fornecedor;
import com.fred.minhasempresas.model.repository.EmpresaRepository;
import com.fred.minhasempresas.model.repository.FornecedorRepository;
import com.fred.minhasempresas.service.FornecedorService;
import com.fred.minhasempresas.service.exceptions.*;
import com.fred.minhasempresas.util.cep.CepUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class FornecedorServiceImpl implements FornecedorService {
	
	private FornecedorRepository repository;
	private EmpresaRepository empresaRepository;
    @PersistenceContext
    private EntityManager entityManager;
	
	public FornecedorServiceImpl(FornecedorRepository repository, EmpresaRepository empresaRepository) {
		super();
		this.repository = repository;
		this.empresaRepository = empresaRepository;
	}

	@Override
	@Transactional
	public Fornecedor salvarFornecedor(Fornecedor fornecedor) {
		
	    if (CepUtil.validarCepExiste(fornecedor.getCep())) {
	        validarFonecedor(fornecedor);
	        validarFornecedorExiste(limparCnpj_Cpf(fornecedor.getCnpj_cpf()));
	        verificaPF(limparCnpj_Cpf(fornecedor.getCnpj_cpf()), fornecedor.getData_nascimento());
	    } else {
	        throw new RegraNegocioException("CEP Inválido, por favor digite o CEP novamente");
	    }

	    if ( fornecedor.getEmpresas() == null || fornecedor.getEmpresas().isEmpty()) {
	    	
	    	 System.out.println("Entrou na Lista Empresa Vazia:" + fornecedor.getEmpresas());
	    	 System.out.println("Fornecedor na Lista Empresa Vazia:" + fornecedor);
	    	return repository.save(fornecedor);
	    	
	    } else {
	    	
	    	System.out.println("Entrou no Else da validação da Lista Empresas:" + fornecedor.getEmpresas());
	    	System.out.println("Entrou no Else da validação da Lista Empresas:" + fornecedor.getNome());
           
	    	
	        Long empresaId = fornecedor.getEmpresas().get(0).getEmpresa_id();
	      
	        System.out.println("Empresa ID" + empresaId);
        	
        	Empresa empresa = empresaRepository.findById(empresaId)
        		   .orElseThrow(() -> new RegraNegocioException("Empresa não encontrada: " + empresaId));
        	
        	fornecedor.getEmpresas().clear(); // Remova todas as associações anteriores (caso existam)
        	fornecedor.getEmpresas().add(empresa); // Adicione a nova empresa ao fornecedor
        		
        	System.out.println("Fornecedor Lista de Empresa: " + fornecedor.getEmpresas());
        		
        	repository.save(fornecedor); // Salve o fornecedor atualizado
	          	
        	System.out.println("Empresa: " + empresa.getNome_fantasia());
	        System.out.println("Fornecedor: " + fornecedor.getNome());
    	
	        empresa.getFornecedores().add(fornecedor);
	        	
	        System.out.println("Fornecedor: " + empresa.getFornecedores());
	        		
	        empresaRepository.save(empresa);
	        	  	
	        System.out.println("Empresa Retorno: " + empresa);
	        	  	
	        return fornecedor;

	    }
	}

	
	private void verificaPF(String Cnpj_Cpf, LocalDate Data_Nasc) {
		if( Cnpj_Cpf.length()<14 && Data_Nasc == null ) {
			throw new RegraNegocioException("Por favor, cmo setrata de uma Pessoa Física é necessário "
					+ "que preencha a Data de NAscimento. Muito Obrigado");
		}
		if( Cnpj_Cpf.length() >= 14 && Data_Nasc != null ) {
			throw new RegraNegocioException("Por favor, como se trata de uma Pessoa Jurídica"
					+ " não é necessário que preencha a Data de Nascimento. Muito Obrigado");
		}
		
	}
	
	 public boolean validarIdadeMinima(LocalDate dataNascimento) {
	        int idadeMinima = 18;
	        LocalDate dataAtual = LocalDate.now();
	        Period periodo = Period.between(dataNascimento, dataAtual);
	        int idade = periodo.getYears();
	        System.out.println("Idade:" + idade);
	        System.out.println("Idade Mínima:" + idadeMinima);
	        if (idade > idadeMinima) {
	            //throw new RegraNegocioException("Fornecedores pessoa física do Paraná devem ter pelo menos 18 anos.");
	        	return false;
	        }else {
				return true;
	        }

	    }

	@Override
	@Transactional
	public void validarFornecedorExiste(String cnpj_cpf) {
		boolean existe = repository.existsByCnpj_cpf(cnpj_cpf);
		if(existe) {
			throw new RegraNegocioException("Já existe um Fornecedor cadastrado com esse CNPJ/CPF.");
		}
	}

	@Override
	@Transactional
	public Fornecedor atualizarFornecedor(Fornecedor fornecedor) {
		Objects.requireNonNull(fornecedor.getFornecedor_id());
		validarFonecedor(fornecedor);
		return repository.save(fornecedor);
	}

	@Override
	@Transactional
	public void deletar(Fornecedor fornecedor) {
		System.out.println("Deletar o Fornecedor" + fornecedor);
		Objects.requireNonNull(fornecedor.getFornecedor_id());
		repository.delete(fornecedor);
		
	}

	@Override
	@Transactional
	public List<Fornecedor> buscarFornecedor(Fornecedor fornecedorFiltro) {
		//Vai pega a instância do Objeto fornecedorFiltro com os dados preenchidos
		// vai passar para o Objeto Example
		Example example = Example.of( fornecedorFiltro, 
				ExampleMatcher.matching()
				.withIgnoreCase()
				.withStringMatcher( StringMatcher.CONTAINING) );
		//CONTAINING busca comr parte do Lancamento que foi decrito na colsuta
		//Exact busca pela decrição exata 
		//Ending encontrar a descrição TERMINE com o Lancamento que foi decrito na colsuta
		List<Fornecedor> listaFornecedor = repository.findAll(example);		
		System.out.println("Fornecedor Lista: " + listaFornecedor);
		System.out.println("Fornecedor Empresa: " + listaFornecedor.get(0).getEmpresas());
		return repository.findAll(example);
	}

	@Override
	@Transactional
	public void validarFonecedor(Fornecedor fornecedor) {
		
		if(fornecedor.getCnpj_cpf()== null || fornecedor.getCnpj_cpf().trim().equals("") ) {
			throw new RegraNegocioException("Informe um CNPJ/CPF Válido");
		}
		
		
		if( fornecedor.getEmail()== null || fornecedor.getEmail().trim().equals("") ) {
			throw new RegraNegocioException("Informe um Email Válido");
		}
		
		if(fornecedor.getNome()== null || fornecedor.getNome().trim().equals("")) {
			throw new RegraNegocioException("Informe um Nome Válido");
		}
		
		if(fornecedor.getRg() == null || fornecedor.getRg().length() > 9) {
			throw new RegraNegocioException("Informe um RG Válido");
		}
	}
	//@Override
	//public Optional<Fornecedor> obterPorId(Long id) {
		// TODO Auto-generated method stub
	//	return Optional.empty();
	//}
	
    @Override
    public Optional<Fornecedor> obterPorId(Long id) {
        return repository.findById(id);
    }
	
    public static String limparCnpj_Cpf(String cnpj) {
        String cnpjLimpo = cnpj.replaceAll("[^0-9]", "");
        return cnpjLimpo;
    }
    

	@Transactional
    public void associarEmpresaAoFornecedor(Long fornecedorId, Long empresaId) {
        Fornecedor fornecedor = repository.findById(fornecedorId)
                .orElseThrow(() -> new RegraNegocioException("Fornecedor não encontrado: " + fornecedorId));

        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new RegraNegocioException("Empresa não encontrada: " + empresaId));

        fornecedor.getEmpresas().add(empresa);
        empresa.getFornecedores().add(fornecedor);

        repository.save(fornecedor);
        empresaRepository.save(empresa);
    }
}



	
	

