package io.github.alexfx1.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InformacoesPedidoDTO {
    private Long id;
    private String cpf;
    private String nomePessoa;
    private BigDecimal total;
    private String dataPedido;
    private String status;
    private List<InformacoesItemPedidoDTO> itensDoPedido;
}
