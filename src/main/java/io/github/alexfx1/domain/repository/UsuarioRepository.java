package io.github.alexfx1.domain.repository;

import io.github.alexfx1.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM USUARIO WHERE LOGIN = :login")
    Optional<Usuario> findByLogin(@Param("login") String login);
}
