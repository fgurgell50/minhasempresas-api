package com.fred.minhasempresas.api.dto;

import java.util.List;



import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaDTO {
	
	private Long id;
	private String cnpj;
	private String nome_fantasia;
	private String cep;
    private List<FornecedorDTO> fornecedores;

}
