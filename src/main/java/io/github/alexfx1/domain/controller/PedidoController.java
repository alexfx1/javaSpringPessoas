package io.github.alexfx1.domain.controller;


import io.github.alexfx1.domain.dto.AtualizacaoStatusPedidoDTO;
import io.github.alexfx1.domain.dto.InformacoesPedidoDTO;
import io.github.alexfx1.domain.dto.PedidoDTO;
import io.github.alexfx1.domain.entity.Pedido;
import io.github.alexfx1.domain.service.PedidoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/pedido")
@Api(value = "Api Rest Pedidos")
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    @ApiOperation("Salva um pedido")
    @ResponseStatus(HttpStatus.CREATED)
    public Long savePedido(@RequestBody @Valid PedidoDTO pedidoDTO){
        Pedido pedido = pedidoService.salvarPedido(pedidoDTO);
        return pedido.getIdPedido();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Consultar Pedido")
    public ResponseEntity<InformacoesPedidoDTO> getInfoPedido(@RequestParam Long idPedido) {
        InformacoesPedidoDTO informacoesPedidoDTO = pedidoService.getById(idPedido);
        return new ResponseEntity<>(informacoesPedidoDTO, HttpStatus.OK);
    }

    @GetMapping("/totalpedidos/{id}")
    @ApiOperation(value = "Consulta todos os pedidos feitos por uma pessoa")
    public ResponseEntity<List<InformacoesPedidoDTO>> getInfoPedidoPorPessoa(@RequestParam Long idPessoa) {
        List<InformacoesPedidoDTO> dtoList = pedidoService.buscarPedidosPessoa(idPessoa);
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "Alterar Status Pedido")
    public void updateStatus(@RequestBody AtualizacaoStatusPedidoDTO statusPedidoDTO, @RequestParam Long idPedido) {
        pedidoService.updateStatusPedido(statusPedidoDTO,idPedido);
    }

}
