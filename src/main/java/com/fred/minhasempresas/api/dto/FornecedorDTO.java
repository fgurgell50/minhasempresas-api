package com.fred.minhasempresas.api.dto;

import java.time.LocalDate;
import java.util.List;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FornecedorDTO {
	
	 private Long Id;
	 private String cnpj_cpf;
     private String nome;
     private String email;
     private String rg;
     private LocalDate data_nascimento;
     private String cep;
     private List<EmpresaDTO> empresas;

}
