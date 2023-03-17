package io.github.alexfx1.domain.repository;

import io.github.alexfx1.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Produto findById(long id);

    @Query(nativeQuery = true, value = "SELECT nome_produto FROM PRODUTO WHERE nome_produto = :nome")
    String findByName(@Param(value = "nome") String name);

    @Query(nativeQuery = true, value = "SELECT marca FROM PRODUTO WHERE marca = :marca")
    String findByNameMarca(@Param(value = "marca") String marca);
}
