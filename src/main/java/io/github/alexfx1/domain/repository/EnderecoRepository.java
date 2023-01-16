package io.github.alexfx1.domain.repository;

import io.github.alexfx1.domain.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM ENDERECO WHERE PESSOA_ID = :pessoa_id")
    List<Endereco> listarEnderecosPessoa(@Param("pessoa_id") Long idPessoa);

    @Query(nativeQuery = true, value = "SELECT * FROM ENDERECO WHERE ENDERECO_PRINCIPAL = TRUE AND PESSOA_ID = :pessoa_id")
    List<Endereco> enderecoPrincipal(@Param("pessoa_id") Long idPessoa);

}
