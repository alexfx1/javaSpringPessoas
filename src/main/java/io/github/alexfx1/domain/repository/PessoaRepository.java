package io.github.alexfx1.domain.repository;


import io.github.alexfx1.domain.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    Pessoa findById(long id);

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE PESSOA SET ENDERECOS = :json WHERE PESSOA_ID = :pessoa_id")
    void listaEnderecos(@Param("pessoa_id") Long idPessoa, @Param("json") String json);

    @Query(nativeQuery = true, value = "SELECT * FROM PESSOA C LEFT JOIN PEDIDOS WHERE c.idPessoa = :pessoa_id")
    Pessoa findPessoaFetchPedidos(@Param("pessoa_id") Long idPessoa);
}



