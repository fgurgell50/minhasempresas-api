package com.fred.minhasempresas.api.resources;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fred.minhasempresas.api.dto.EmpresaDTO;
import com.fred.minhasempresas.api.dto.FornecedorDTO;
import com.fred.minhasempresas.model.entity.Empresa;
import com.fred.minhasempresas.model.entity.Fornecedor;
import com.fred.minhasempresas.service.EmpresaService;
import com.fred.minhasempresas.service.FornecedorService;
import com.fred.minhasempresas.service.exceptions.RegraNegocioException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/fornecedor")
@RequiredArgsConstructor

public class FornecedorResource {

	private final FornecedorService service;
	private final EmpresaService empresaService;
		
	@PostMapping
	public ResponseEntity salvar( @RequestBody FornecedorDTO dto ) throws IOException, InterruptedException {
		
		List<Empresa> empresas = null;
		Fornecedor fornecedor = null;
		
		try {
			if( dto.getEmpresas() == null || dto.getEmpresas().isEmpty() ) {
				
				System.out.println("Entrou DTO Empresa Null:" + dto);
				System.out.println("Entrou DTO Empresa Null:" + dto.getEmpresas());
				
						fornecedor = Fornecedor.builder()
						.cnpj_cpf(dto.getCnpj_cpf())
						.nome(dto.getNome())
						.email(dto.getEmail())
						.data_nascimento(dto.getData_nascimento())
						.rg(dto.getRg())
						.cep(dto.getCep())
						.empresas(empresas)
						.build();
			
			}else {
				empresas = converterListaEmpresasDTO(dto.getEmpresas());
				
				System.out.println("Entrou DTO Empresa NOT NULL:" + empresas);
				
				 	fornecedor = Fornecedor.builder()
					.cnpj_cpf(dto.getCnpj_cpf())
					.nome(dto.getNome())
					.email(dto.getEmail())
					.data_nascimento(dto.getData_nascimento())
					.rg(dto.getRg())
					.cep(dto.getCep())
					.empresas(empresas)
					.build();
			}
			
			
		if (fornecedor.getCnpj_cpf().length() < 14) {
			if (empresaService.validarFornecedoresParana(fornecedor.getCep()) && service.validarIdadeMinima(fornecedor.getData_nascimento())) {
				 
				System.out.println("Cep Paraná:" + empresaService.validarFornecedoresParana(fornecedor.getCep()));
				System.out.println("Menor de Idade:" + service.validarIdadeMinima(fornecedor.getData_nascimento()));
				
				return ResponseEntity.badRequest()
							.body("Não foi possível cadastrar o Fornecedor com eses dados."
									+ "Para o Paraná é necessário ter maior de 18 anos"
									+ "para ser um fornecedor de serviço");           	
			} else {
				Fornecedor salvarFornecedor = service.salvarFornecedor(fornecedor);
				return new ResponseEntity(salvarFornecedor, HttpStatus.CREATED);   
	
			}
		 
		} else {
			Fornecedor salvarFornecedor = service.salvarFornecedor(fornecedor);
			return new ResponseEntity(salvarFornecedor, HttpStatus.CREATED);       
			}
		} catch (RegraNegocioException e) {
			  return ResponseEntity.badRequest().body(e.getMessage());
		} 
	}
	

	private List<Empresa> converterListaEmpresasDTO(List<EmpresaDTO> empresasDTO) {
	    return empresasDTO.stream()
	            .map(empresaDTO -> Empresa.builder()
	                    .empresa_id(empresaDTO.getId()) // Apenas o id da empresa é atribuído
	                    .build())
	            .collect(Collectors.toList());
	}
	
	@PutMapping("{id}")
	public ResponseEntity atualizar( @PathVariable("id") Long id, @RequestBody FornecedorDTO dto ) {
			return service.obterPorId(id).map( entity -> {
			try {
				Fornecedor fornecedor = converter(dto);
				fornecedor.setFornecedor_id(entity.getFornecedor_id());
				service.atualizar(fornecedor);
				return ResponseEntity.ok(fornecedor);			
			}catch (RegraNegocioException e) {			
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> 
		new ResponseEntity("Lancamento não encontrado ma base de dados", HttpStatus.BAD_REQUEST));
	}
	
	@GetMapping
	public ResponseEntity buscar(
			@RequestParam( value = "cnpj_cpf" , required = false) String cnpj_cpf,
			@RequestParam( value = "nome" , required = false) String nome
			//@RequestParam( "empresa" ) Long idEmpresa
			
			//@RequestParam java.util.Map<String, String> params
			//Poderia ser dessa forma tb para receber os parametros
			// porém todos os parametros são opcionais
			
			) {
		Fornecedor fornecedorFiltro = new Fornecedor();
		System.out.println("CNPJCPF:" + cnpj_cpf);
		System.out.println("NOME:" + nome);
		fornecedorFiltro.setCnpj_cpf(cnpj_cpf);
		fornecedorFiltro.setNome(nome);
		
		List<Fornecedor> fornecedor = service.buscarFornecedor(fornecedorFiltro);	
		return ResponseEntity.ok(fornecedor);
	}
	
	@GetMapping("{id}")
	public ResponseEntity obterFornecedor( @PathVariable("id") Long id) {
		
		System.out.println("Fornecedor de obterFornecedor:" + service.obterPorId(id));

		return service.obterPorId(id)
				.map( fornecedor -> new ResponseEntity(converter(fornecedor), HttpStatus.OK))
				.orElseGet( () -> new ResponseEntity(HttpStatus.NOT_FOUND));
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity deletar(@PathVariable("id") Long id) {
		System.out.println("Objeto Fornecedor recuperado" + id);
		System.out.println("Objeto Fornecedor recuperado" + service.obterPorId(id));
		return service.obterPorId(id).map( entidade -> {
			try {
				service.deletar(entidade);
				System.out.println("Entrou no Deletar Resourcer" + entidade);
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> 
		new ResponseEntity("Fornecedor não encontrado ma base de dados", HttpStatus.BAD_REQUEST));
	}
	
	private FornecedorDTO converter(Fornecedor fornecedor) {
		
	    Long empresaId = fornecedor.getEmpresas().get(0).getEmpresa_id();
	    EmpresaDTO empresaDTO = EmpresaDTO.builder().id(empresaId).build();
	    List<EmpresaDTO> empresasDTO = Collections.singletonList(empresaDTO);
	    
		return FornecedorDTO.builder()
				.Id(fornecedor.getFornecedor_id())
				.cnpj_cpf(fornecedor.getCnpj_cpf())
				.nome(fornecedor.getNome())
				.email(fornecedor.getEmail())
				.rg(fornecedor.getRg())
				.data_nascimento(fornecedor.getData_nascimento())
				.cep(fornecedor.getCep())
				.empresas(empresasDTO)
				.build();
	}
	
	private Fornecedor converter(FornecedorDTO dto) {
		
		System.out.println("Objeto DTO em Converter" + dto);
		
		Fornecedor fornecedor = new Fornecedor();
		fornecedor.setFornecedor_id(dto.getId());
		fornecedor.setCnpj_cpf(dto.getCnpj_cpf());
		fornecedor.setNome(dto.getNome());
		fornecedor.setEmail(dto.getEmail());
		fornecedor.setData_nascimento(dto.getData_nascimento());
		fornecedor.setCep(dto.getCep());
		
	    List<EmpresaDTO> empresasDTO = dto.getEmpresas();
	    if (empresasDTO != null && !empresasDTO.isEmpty()) {
	        Long empresaId = empresasDTO.get(0).getId();
	        Optional<Empresa> empresaOptional = empresaService.obterPorId(empresaId);
	        if (empresaOptional.isPresent()) {
	            fornecedor.setEmpresas(Collections.singletonList(empresaOptional.get()));
	        } else {
	            throw new RegraNegocioException("Empresa não encontrada para o ID informado");
	        }
	    } else {
	        fornecedor.setEmpresas(null);
	    }

	    System.out.println("Objeto Fornecedor em Converter" + fornecedor);
	    return fornecedor;
	}
}







