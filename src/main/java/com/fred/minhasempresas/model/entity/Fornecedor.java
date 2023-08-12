package com.fred.minhasempresas.model.entity;

import lombok.*;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table ( name = "fornecedor", schema = "empresas_fornecedores" )
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "fornecedor_id")
public class Fornecedor {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fornecedor_id")
    private Long fornecedor_id;

    @Column(name = "cnpj_cpf" )
    private String cnpj_cpf;

    @Column(name = "nome" )
    private String nome;

    @Column(name = "email")
    private String email;

    @Column(name = "rg")
    private String rg;
    
    @Column( name = "data_nascimento" )
    private LocalDate data_nascimento; // Atualizar para o tipo LocalDate

    @Column(name = "cep" )
    private String cep;
    
    //@Column( name = "empresa_fornecedor_id")
   // private Long empresa_fornenecedor_id;


    /*
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable( name = "empresa_fornecedor",
            joinColumns = @JoinColumn( name = "fornecedor_id"),
            inverseJoinColumns = @JoinColumn( name = "empresa_id"))
    @JsonBackReference
    private List<Empresa> empresas;
    */
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "empresa_fornecedor",
        joinColumns = @JoinColumn(name = "fornecedor_id"),
        inverseJoinColumns = @JoinColumn(name = "empresa_id")
    )
    private List<Empresa> empresas;
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Fornecedor {")
          .append("fornecedor_id=").append(fornecedor_id)
          .append(", cnpj_cpf='").append(cnpj_cpf).append('\'')
          .append(", nome='").append(nome).append('\'')
          .append(", email='").append(email).append('\'')
          .append(", rg='").append(rg).append('\'')
          .append(", data_nascimento=").append(data_nascimento)
          .append(", cep='").append(cep).append('\'');

        if (empresas != null && !empresas.isEmpty()) {
            sb.append(", empresas=[");
            for (Empresa empresa : empresas) {
                sb.append(empresa.getNome_fantasia()).append(", ");
            }
            sb.delete(sb.length() - 2, sb.length()); // Remove a última vírgula e espaço
            sb.append("]");
        } else {
            sb.append(", empresas=[]");
        }

        sb.append('}');
        return sb.toString();
    }
}
