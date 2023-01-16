package io.github.alexfx1.domain.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "endereco")
@AllArgsConstructor
@NoArgsConstructor
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "seq_endereco")
    @SequenceGenerator(name = "seq_endereco",sequenceName = "seq_endereco")
    @Column(name = "endereco_id", nullable = false)
    private Long idEndereco;

    @Column(name = "logradouro")
    private String logradouro;

    @Column(name = "cep")
    private String cep;

    @Column(name = "numero")
    private Integer numero;

    @Column(name = "cidade")
    private String cidade;

    @Column(name = "pessoa_id")
    private Long pessoaId;

    @Column(name = "endereco_principal")
    private boolean enderecoPrincipal;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pessoa_id", referencedColumnName = "pessoa_id", insertable = false, updatable = false)
    @JsonIgnore
    private Pessoa pessoa;

}

