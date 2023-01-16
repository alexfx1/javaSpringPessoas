package io.github.alexfx1.domain.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "pessoa")
@AllArgsConstructor
@NoArgsConstructor
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "seq_pessoa")
    @SequenceGenerator(name = "seq_pessoa",sequenceName = "seq_pessoa")
    @Column(name = "pessoa_id", nullable = false)
    private Long idPessoa;

    @Column(name = "nome")
    private String nome;

    @Column(name = "dtNascimento")
    private Date dtNascimento;

    @Column(name = "enderecos",length = 100000)
    private String jsonEnderecos;

    @OneToMany
    @JoinColumn(name = "pessoa_id", referencedColumnName = "pessoa_id")
    @JsonIgnore
    private List<Endereco> enderecos;

}

