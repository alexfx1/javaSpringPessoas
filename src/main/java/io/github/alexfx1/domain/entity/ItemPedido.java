package io.github.alexfx1.domain.entity;

import javax.persistence.*;

import lombok.*;

import java.math.BigDecimal;


@Entity
@Getter
@Setter
@Table(name = "item_pedido")
@AllArgsConstructor
@NoArgsConstructor
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_pedido_id", nullable = false)
    private Long idItemPedido;

    @Column(name = "quantidade")
    private Integer quantidade;

    @Column(name = "total_preco_itens")
    private BigDecimal totalPrecoQuantidadeItens;

    @Column(name = "pedido_id")
    private Long idPedido;

    @Column(name = "produto_id")
    private Long idProduto;

    @ManyToOne
    @JoinColumn(name = "pedido_id", referencedColumnName = "pedido_id", insertable = false, updatable = false)
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "produto_id", referencedColumnName = "produto_id", insertable = false, updatable = false)
    private Produto produto;

}

