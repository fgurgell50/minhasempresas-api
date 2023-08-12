package com.fred.minhasempresas.api.resources;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fred.minhasempresas.api.dto.EmpresaDTO;
import com.fred.minhasempresas.api.dto.FornecedorDTO;
import com.fred.minhasempresas.model.entity.Empresa;
import com.fred.minhasempresas.model.entity.Fornecedor;
import com.fred.minhasempresas.service.EmpresaService;
import com.fred.minhasempresas.service.exceptions.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/empresa")
@RequiredArgsConstructor
//Substitui o código comentado com a inclusão do final
public class EmpresaResource {
	
	/*
	@GetMapping("/")
	public String helloworld() {
		return "Hello World";
	}
	*/
	
	private final EmpresaService service;
		
	/*
	@PostMapping
	public ResponseEntity salvar( @RequestBody EmpresaDTO dto ) {
		
		Empresa empresa = Empresa.builder()
				.cnpj(dto.getCnpj())
				.nome_fantasia(dto.getNome_fantasia())
				.cep(dto.getCep()).build();
	
		try {
			Empresa  empresaSalva = service.salvarEmpresa(empresa);
			return new ResponseEntity(empresaSalva, HttpStatus.CREATED);
		}catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	
	}*/

	@PostMapping
	public ResponseEntity salvar( @RequestBody EmpresaDTO dto ) throws IOException, InterruptedException {
		
			List<Fornecedor> fornecedores = null;
			Empresa empresa = null;
		
		try {
			
			if ( dto.getFornecedores() == null || dto.getFornecedores().isEmpty()) {
				
				empresa = Empresa.builder()
						.cnpj(dto.getCnpj())
						.nome_fantasia(dto.getNome_fantasia())
						.cep(dto.getCep())
						.fornecedores(fornecedores)
						.build();
				
			}else {
				
				fornecedores = converterListaFornecedorDTO(dto.getFornecedores());
				
				empresa = Empresa.builder()
				.cnpj(dto.getCnpj())
				.nome_fantasia(dto.getNome_fantasia())
				.cep(dto.getCep())
				.fornecedores(fornecedores)
				.build();
							
			}
			
		if (empresa.getCnpj().length() < 14) {
			System.out.println("Fornecedor Depois de Salvar: " + empresa.getCnpj().length());
			
			return ResponseEntity.badRequest()
					.body("Não foi possível cadastrar a Empresa com eses dados."
							+ "Verifique o CNPJ digitado e tente novamente");
           	
			} else {
				Empresa salvarEmpresa = service.salvarEmpresa(empresa);
				return new ResponseEntity(salvarEmpresa, HttpStatus.CREATED);   
			}
		} catch (RegraNegocioException e) {
			  return ResponseEntity.badRequest().body(e.getMessage());
		} 
	}
	
	private List<Fornecedor> converterListaFornecedorDTO(List<FornecedorDTO> fornecedoresDTO) {
	    return fornecedoresDTO.stream()
	            .map(fornecedorDTO -> Fornecedor.builder()
	                    .fornecedor_id(fornecedorDTO.getId()) // Apenas o id do fornecedor é atribuído
	                    .build())
	            .collect(Collectors.toList());
	}
	
	@GetMapping
	public ResponseEntity buscar(
			@RequestParam( value = "cnpj" , required = false) String cnpj,
			@RequestParam( value = "nome_fantasua" , required = false) String nome_fantasia
			//@RequestParam( "empresa" ) Long idEmpresa
			
			//@RequestParam java.util.Map<String, String> params
			//Poderia ser dessa forma tb para receber os parametros
			// porém todos os parametros são opcionais
			
			) {
		Empresa empresaFiltro = new Empresa();
		System.out.println("CNPJCPF:" + cnpj);
		System.out.println("NOME:" + nome_fantasia);
		empresaFiltro.setCnpj(cnpj);
		empresaFiltro.setNome_fantasia(nome_fantasia);
		
		List<Empresa> empresa = service.buscarEmpresa(empresaFiltro);	
		return ResponseEntity.ok(empresa);
	}
	
}
