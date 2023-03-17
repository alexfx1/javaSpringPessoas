package io.github.alexfx1.domain.repository;


import io.github.alexfx1.domain.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM PEDIDO WHERE PESSOA_ID = :pessoa_id")
    List<Pedido> findByPessoa(@Param("pessoa_id") Long idPessoa);

    @Query(nativeQuery = true, value = "SELECT * FROM pedido p LEFT JOIN item_pedido ip ON p.pedido_id = ip.pedido_id WHERE p.pedido_id = :pedido_id")
    Optional<Pedido> findByIdFetchItemPedidos(@Param("pedido_id") Long idPedido);

}
