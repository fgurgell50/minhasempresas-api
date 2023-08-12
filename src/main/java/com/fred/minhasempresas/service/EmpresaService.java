package com.fred.minhasempresas.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.fred.minhasempresas.model.entity.Empresa;
import com.fred.minhasempresas.model.entity.Fornecedor;


public interface EmpresaService {
	
	Empresa salvarEmpresa(Empresa empresa);
	
	Empresa atualizarEmpresa(Empresa empresa);
	
	void deletarEmpresa(Empresa empresa);
	
	void validarEmpresa(String cnpj);
	
	List<Empresa> buscarEmpresa(Empresa empresarFiltro);
	
	Optional<Empresa> obterPorId(Long id);
	
	//boolean validarFornecedoresParana(String cep) throws IOException, InterruptedException;
	
	boolean validarFornecedoresParana(String cep) throws IOException, InterruptedException;
	

	}
