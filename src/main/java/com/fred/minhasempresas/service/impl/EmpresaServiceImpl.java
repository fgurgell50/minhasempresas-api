package com.fred.minhasempresas.service.impl;

import java.io.IOException;
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
import com.fred.minhasempresas.service.EmpresaService;
import com.fred.minhasempresas.service.exceptions.RegraNegocioException;
import com.fred.minhasempresas.util.cep.CepUtil;

import jakarta.transaction.Transactional;

@Service
public class EmpresaServiceImpl implements EmpresaService {
	

    private EmpresaRepository repository;
    private FornecedorRepository fornecedorRepository;

    public EmpresaServiceImpl(EmpresaRepository repository, FornecedorRepository fornecedorRepository) {
        this.repository = repository;
        this.fornecedorRepository = fornecedorRepository;
    }
	
	@Override
	@Transactional
	public Empresa salvarEmpresa(Empresa empresa) {
	
			if(CepUtil.validarCepExiste(empresa.getCep())) {
		        System.out.println("Dentro do Método salvaEmpresa:" + CepUtil.validarCepExiste(empresa.getCep()));
				validarEmpresa(empresa.getCnpj());
			}else {
				throw new RegraNegocioException("CEP Inválido, por favor digite o CEP novamente");
			}
			
			 if (empresa.getFornecedores() == null || empresa.getFornecedores().isEmpty() ) {
			        
				 System.out.println("Dentro do lista Fornecedores Vazia:" + empresa.getFornecedores());
				 
				 return repository.save(empresa);
				 
			    } else {
	    	
			    	System.out.println("Dentro do lista Fornecedores Preenchida:" + empresa.getFornecedores());
			    	
			        Long fornecedorId = empresa.getFornecedores().get(0).getFornecedor_id();
			        
			        System.out.println("Id do Fornecedor da Lista dentro de Empresa:" + empresa.getFornecedores().get(0).getFornecedor_id());
			        
			        Fornecedor fornecedorEmpresa = fornecedorRepository.findById(fornecedorId)
			                .orElseThrow(() -> new RegraNegocioException("Fornecedor não encontrada: " + fornecedorId));
			        
			        empresa.getFornecedores().clear();
			        empresa.getFornecedores().add(fornecedorEmpresa); 
			        
			        System.out.println("Empresa Lista de Fornecedores: " + empresa.getFornecedores());
			        System.out.println("Empresa a ser Inserida: " + empresa);
			        
			        repository.save(empresa);
			        
	        		fornecedorEmpresa.getEmpresas().add(empresa);
		        	
	        		System.out.println("Fornecedor: " + fornecedorEmpresa.getEmpresas());
	        		System.out.println("Fornecedor a ser Inserido: " + fornecedorEmpresa);
	        		
	        	  	fornecedorRepository.save(fornecedorEmpresa);
	        	  	
	        		System.out.println("Fornecedor Depois de Salvar: " + fornecedorEmpresa);
	        		
	        		System.out.println("Empresa que vai retornar Depois de Salvar: " + empresa);
			        
			        return empresa;
			    }
		}
		
	@Override
	@Transactional
	public void validarEmpresa(String cnpj) {
		boolean existe = repository.existsByCnpj(cnpj);
		if(existe) {
			throw new RegraNegocioException("Já existe uma Empresa cadastrado com esse CNPJ.");
		}
	}

	@Override
	@Transactional
	public Optional<Empresa> obterPorId(Long id) {
		// TODO Auto-generated method stub
		return repository.findById(id);
	}


	@Override
	@Transactional
	public Empresa atualizarEmpresa(Empresa empresa) {
		Objects.requireNonNull(empresa.getEmpresa_id());
		validarEmpresa(empresa.getCnpj());
		return repository.save(empresa);
	}


	@Override
	@Transactional
	public void deletarEmpresa(Empresa empresa) {
		Objects.requireNonNull(empresa.getEmpresa_id());
		repository.delete(empresa);
		
	}

	@Override
	public boolean validarFornecedoresParana(String cep) throws IOException, InterruptedException {
		if (CepUtil.consultarCep(cep)) {
	        return true;
	    } else {
	    	return false;
	        // Lógica para tratar CEP inválido ou não correspondente ao Paraná...
	    	}
		}
	
	@Transactional
    public void associarFonecedorAEmpresa(Long fornecedorId, Long empresaId) {
        Fornecedor fornecedor = fornecedorRepository.findById(fornecedorId)
                .orElseThrow(() -> new RegraNegocioException("Fornecedor não encontrado: " + fornecedorId));

        Empresa empresa = repository.findById(empresaId)
                .orElseThrow(() -> new RegraNegocioException("Empresa não encontrada: " + empresaId));

        fornecedor.getEmpresas().add(empresa);
        empresa.getFornecedores().add(fornecedor);

        fornecedorRepository.save(fornecedor);
        repository.save(empresa);
    }

	@Override
	@Transactional
	public List<Empresa> buscarEmpresa(Empresa empresarFiltro) {
		Example example = Example.of( empresarFiltro, 
				ExampleMatcher.matching()
				.withIgnoreCase()
				.withStringMatcher( StringMatcher.CONTAINING) );
		List<Empresa> listaEmpresa = repository.findAll(example);		
		System.out.println("Empresa Lista: " + listaEmpresa);
		System.out.println("Empresa Fornecedor: " + listaEmpresa.get(0).getFornecedores());
		return repository.findAll(example);
	}
	
}
