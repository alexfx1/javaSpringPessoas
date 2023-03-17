package io.github.alexfx1.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InformacoesItemPedidoDTO {
    private String nomeProduto;
    private String marca;
    private String descricaoProduto;
    private BigDecimal precoUnitario;
    private Integer quantidade;
}
