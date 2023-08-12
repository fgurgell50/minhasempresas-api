package com.fred.minhasempresas.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.fred.minhasempresas.model.entity.Fornecedor;



public interface FornecedorService {
	
	Fornecedor salvarFornecedor(Fornecedor fornecedor);
	
	Fornecedor atualizarFornecedor(Fornecedor fornecedor);
	
	void deletar(Fornecedor fornecedor);
	
	List<Fornecedor> buscarFornecedor(Fornecedor fornecedorFiltro);
	
	void validarFonecedor(Fornecedor fornecedor);
	
	Optional<Fornecedor> obterPorId(Long id);

	void validarFornecedorExiste(String cnpj_cpf);

	boolean validarIdadeMinima(LocalDate data_nascimento);

	void associarEmpresaAoFornecedor(Long fornecedorId, Long empresaId);

}
