package com.fred.minhasempresas.model.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fred.minhasempresas.model.entity.Empresa;
import com.fred.minhasempresas.model.entity.Fornecedor;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {

    //@Query("SELECT f FROM Fornecedor f WHERE f.cnpj_cpf = :cnpj_cpf")
    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM Fornecedor f "
    		+ "WHERE f.cnpj_cpf = :cnpj_cpf")
    boolean existsByCnpj_cpf(@Param("cnpj_cpf") String cnpj_cpf);
    
	//Optional<Fornecedor> findByEmail(String email);
	
	//Optional<Fornecedor> findById(Long id);
	
	}