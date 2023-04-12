package io.github.alexfx1.domain.service;

import io.github.alexfx1.domain.dto.ItemPedidoDTO;
import io.github.alexfx1.domain.dto.PedidoDTO;
import io.github.alexfx1.domain.entity.Pedido;
import io.github.alexfx1.domain.entity.Pessoa;
import io.github.alexfx1.domain.enums.StatusPedido;
import io.github.alexfx1.domain.repository.ItemPedidoRepository;
import io.github.alexfx1.domain.repository.PedidoRepository;
import org.junit.Before;
import org.junit.Test;

import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.github.alexfx1.domain.entity.Produto;
import io.github.alexfx1.domain.repository.ProdutoRepository;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

public class PedidoServiceTest {

    @InjectMocks
    private PedidoService pedidoService;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private PessoaService pessoaService;

    @Mock
    private ItemPedidoRepository itemPedidoRepository;

    @Mock
    private ProdutoService produtoService;

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    private List<ItemPedidoDTO> itemPedidoDTOList() {
        List<ItemPedidoDTO> list = new ArrayList<>();
        ItemPedidoDTO itemPedidoDTO = new ItemPedidoDTO();

        itemPedidoDTO.setIdProduto(1L);
        itemPedidoDTO.setQuantidade(2);

        list.add(itemPedidoDTO);
        return list;
    }

    private PedidoDTO pedidoDTO() {
        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setIdPessoa(1L);
        pedidoDTO.setItems(itemPedidoDTOList());
        return pedidoDTO;
    }

    private Pessoa createPessoa(){
        Pessoa pessoa = new Pessoa();

        pessoa.setIdPessoa(1L);
        pessoa.setJsonEnderecos("");
        pessoa.setNome("Alex");
        pessoa.setDtNascimento(new Date());

        return pessoa;
    }

    private Pedido createPedido() {
        return Pedido.builder()
                .idPedido(1L)
                .idPessoa(pedidoDTO().getIdPessoa())
                .pessoa(createPessoa())
                .dataPedido(LocalDate.ofEpochDay(2023- 3 - 4))
                .statusPedido(StatusPedido.REALIZADO)
                .build();
    }
}
