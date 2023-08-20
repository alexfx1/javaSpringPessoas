package io.github.alexfx1.domain.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id", nullable = false)
    private Long idUsuario;

    @Column(name = "login")
    @NotEmpty
    private String login;

    @Column(name = "senha")
    @NotEmpty
    private String senha;

    @Column(name = "admin")
    private boolean admin;

    @Column(name = "email")
    @NotEmpty
    private String email;

    @Column(name = "password_code", nullable = true)
    private Integer passwordCode;
}
