package io.github.alexfx1.domain.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "pessoa")
@AllArgsConstructor
@NoArgsConstructor
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pessoa_id", nullable = false)
    private Long idPessoa;

    @Column(name = "nome")
    @NotEmpty(message = "preencha os dados corretamente")
    private String nome;

    @Column(name = "dtNascimento")
    private Date dtNascimento;

    @Column(name = "cpf", length = 11)
    @NotEmpty(message = "preencha os dados corretamente")
    @CPF(message = "cpf invalido")
    private String cpf;

    @Column(name = "enderecos",length = 100000)
    private String jsonEnderecos;

    @OneToMany(mappedBy = "pessoa", fetch = FetchType.LAZY)
    private Set<Endereco> enderecoSet;

    @OneToMany
    @JoinColumn(name = "pessoa_id", referencedColumnName = "pessoa_id")
    @JsonIgnore
    private List<Endereco> enderecos;

}

