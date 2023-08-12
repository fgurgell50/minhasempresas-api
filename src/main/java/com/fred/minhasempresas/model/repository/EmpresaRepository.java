package com.fred.minhasempresas.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fred.minhasempresas.model.entity.Empresa;
import com.fred.minhasempresas.util.cep.CepUtil;


public interface EmpresaRepository extends JpaRepository<Empresa, Long>{
	
	//final EmpresaRepository repository = null;
	
	boolean existsByCnpj(String cnpj);
	
	Optional<Empresa> findById(Long id);
	
	Optional<Empresa> findByCnpj(String cnpj);
	
	}
