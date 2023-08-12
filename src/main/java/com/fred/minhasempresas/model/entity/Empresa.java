package com.fred.minhasempresas.model.entity;

import lombok.*;

import jakarta.persistence.*;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table ( name = "empresa", schema = "empresas_fornecedores" )
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "empresa_id")
public class Empresa {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "empresa_id")
    private Long empresa_id;

    @Column( name = "cnpj" )
    private String cnpj;

    @Column( name = "nome_fantasia" )
    private String nome_fantasia;

    @Column( name = "cep" )
    private String cep;
 
   /*
    @ManyToMany(mappedBy = "empresas"
    		, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Fornecedor> fornecedores ;
    */
    
    @ManyToMany(mappedBy = "empresas",cascade = CascadeType.ALL)
    private List<Fornecedor> fornecedores;
    
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Empresa {")
          .append("empresa_id=").append(empresa_id)
          .append(", cnpj='").append(cnpj).append('\'')
          .append(", nome_fantasia='").append(nome_fantasia).append('\'')
          .append(", cep='").append(cep).append('\'');
        
        if (fornecedores != null && !fornecedores.isEmpty()) {
            sb.append(", fornecedores=[");
            for (Fornecedor fornecedor : fornecedores) {
                sb.append(fornecedor.getNome()).append(", ");
            }
            sb.delete(sb.length() - 2, sb.length()); // Remove a última vírgula e espaço
            sb.append("]");
        } else {
            sb.append(", fornecedores=[]");
        }

        sb.append('}');
        return sb.toString();
    }

    
}