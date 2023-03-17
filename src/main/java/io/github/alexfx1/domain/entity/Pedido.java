package io.github.alexfx1.domain.entity;

import javax.persistence.*;

import io.github.alexfx1.domain.enums.StatusPedido;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Entity
@Getter
@Setter
@Table(name = "pedido")
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pedido_id", nullable = false)
    private Long idPedido;

    @Column(name = "pessoa_id")
    private Long idPessoa;

    @ManyToOne
    @JoinColumn(name = "pessoa_id", updatable = false, insertable = false)
    private Pessoa pessoa;

    @Column(name = "data_pedido")
    private LocalDate dataPedido;

    @Column(name = "total", scale = 2, precision = 20)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusPedido statusPedido;

    @OneToMany(mappedBy = "pedido")
    private List<ItemPedido> itemPedidos;
}

