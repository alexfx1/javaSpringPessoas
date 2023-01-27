package io.github.alexfx1.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoDTO {
    private String logradouro;

    private String cep;

    private Integer numero;

    private String cidade;

    private Long pessoaId;

    private boolean enderecoPrincipal;
}
