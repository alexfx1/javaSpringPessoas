package io.github.alexfx1.domain.dto;

import io.github.alexfx1.domain.entity.ItemPedido;
import io.github.alexfx1.domain.validation.NotEmptyList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDTO {
    @NotNull(message = "campo obrigatorio")
    private Long idPessoa;
    @NotEmptyList
    private List<ItemPedidoDTO> items;
}
