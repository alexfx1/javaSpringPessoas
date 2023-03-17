package io.github.alexfx1.domain.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.*;

import java.math.BigDecimal;


@Entity
@Getter
@Setter
@Table(name = "produto")
@AllArgsConstructor
@NoArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "produto_id", nullable = false)
    private Long idProduto;

    @Column(name = "nome_produto")
    @NotEmpty(message = "campo nome obrigatorio")
    private String nomeProduto;

    @Column(name = "marca")
    @NotEmpty(message = "campo marca obrigatorio")
    private String marca;

    @Column(name = "descricao", length = 750)
    private String descricao;

    @Column(name = "preco")
    @NotNull(message = "campo preco obrigatorio")
    private BigDecimal preco;

}

