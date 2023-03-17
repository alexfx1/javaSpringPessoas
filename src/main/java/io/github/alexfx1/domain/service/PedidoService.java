package io.github.alexfx1.domain.service;

import io.github.alexfx1.domain.dto.*;
import io.github.alexfx1.domain.entity.ItemPedido;
import io.github.alexfx1.domain.entity.Pedido;
import io.github.alexfx1.domain.entity.Pessoa;
import io.github.alexfx1.domain.entity.Produto;
import io.github.alexfx1.domain.enums.StatusPedido;
import io.github.alexfx1.domain.exceptions.CustomException;
import io.github.alexfx1.domain.exceptions.PedidoNaoEncontradoException;
import io.github.alexfx1.domain.repository.ItemPedidoRepository;
import io.github.alexfx1.domain.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private ProdutoService produtoService;

    @Transactional
    public Pedido salvarPedido(PedidoDTO pedidoDTO) {
        try {
            Pedido pedido = new Pedido();
            Pessoa pessoa = pessoaService.consultarPessoa(pedidoDTO.getIdPessoa());
            pedido.setPessoa(pessoa);
            pedido.setIdPessoa(pessoa.getIdPessoa());

            pedido.setDataPedido(LocalDate.now());

            pedidoRepository.save(pedido);


            if(pedidoDTO.getItems().isEmpty()){
                throw new CustomException("LISTA VAZIA");
            }

            List<ItemPedido> itemPedidos = pedidoDTO.getItems().stream().map(dto -> {
                try {
                    ItemPedido itemPedido = new ItemPedido();
                    Produto produto = produtoService.consultarProduto(dto.getIdProduto());
                    itemPedido.setProduto(produto);
                    itemPedido.setIdProduto(produto.getIdProduto());
                    itemPedido.setQuantidade(dto.getQuantidade());
                    itemPedido.setPedido(pedido);
                    itemPedido.setIdPedido(pedido.getIdPedido());

                    BigDecimal totalPrecoItens = produto.getPreco().multiply(BigDecimal.valueOf(dto.getQuantidade()));
                    itemPedido.setTotalPrecoQuantidadeItens(totalPrecoItens);
                    return itemPedido;
                } catch (Exception ex){
                    System.out.println("Erro ou id produto nao encontrado: " + ex);
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Erro ou id produto nao encontrado: " + ex);
                }
            }).collect(Collectors.toList());


            BigDecimal totalPedido = itemPedidos.stream().map(ItemPedido::getTotalPrecoQuantidadeItens).reduce(BigDecimal.ZERO, BigDecimal::add);
            pedido.setTotal(totalPedido);


            pedido.setItemPedidos(itemPedidos);
            pedido.setStatusPedido(StatusPedido.REALIZADO);
            itemPedidoRepository.saveAll(itemPedidos);

            return pedido;

        } catch (Exception ex){
            System.out.println("erro ou id's nao encontrado: " + ex);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "erro ou id's nao encontrado: " + ex);
        }
    }

    public InformacoesPedidoDTO getById(Long idPedido){
        return obterPedidoCompleto(idPedido).map(this::converter)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Pedido nao encontrado"));
    }

    public List<InformacoesPedidoDTO> buscarPedidosPessoa(Long idPessoa) {
        return pedidoRepository.findByPessoa(idPessoa).stream().map(pedido -> InformacoesPedidoDTO.builder()
                .id(pedido.getIdPedido())
                .dataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .cpf(pedido.getPessoa().getCpf())
                .nomePessoa(pedido.getPessoa().getNome())
                .total(pedido.getTotal())
                .status(pedido.getStatusPedido().name())
                .itensDoPedido(converterListaDeInfoPedido(pedido.getItemPedidos()))
                .build()).collect(Collectors.toList());
    }

    @Transactional
    public void updateStatusPedido(AtualizacaoStatusPedidoDTO statusPedidoDTO, Long idPedido){
        String novoStatus = statusPedidoDTO.getStatus();

        pedidoRepository.findById(idPedido).map(pedido -> {
            pedido.setStatusPedido(StatusPedido.valueOf(novoStatus));
            return pedidoRepository.save(pedido);
        }).orElseThrow(PedidoNaoEncontradoException::new);
    }

    private Optional<Pedido> obterPedidoCompleto(Long idPedido){
        return pedidoRepository.findByIdFetchItemPedidos(idPedido);
    }

    private InformacoesPedidoDTO converter(Pedido pedido) {
        return InformacoesPedidoDTO.builder()
                .id(pedido.getIdPedido())
                .dataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .cpf(pedido.getPessoa().getCpf())
                .nomePessoa(pedido.getPessoa().getNome())
                .total(pedido.getTotal())
                .status(pedido.getStatusPedido().name())
                .itensDoPedido(converterListaDeInfoPedido(pedido.getItemPedidos()))
                .build();
    }

    private List<InformacoesItemPedidoDTO> converterListaDeInfoPedido(List<ItemPedido> itemPedidos){
        if(CollectionUtils.isEmpty(itemPedidos)){
            return Collections.emptyList();
        }

        return itemPedidos.stream().map(itemPedido -> InformacoesItemPedidoDTO.builder()
                .nomeProduto(itemPedido.getProduto().getNomeProduto())
                .marca(itemPedido.getProduto().getMarca())
                .descricaoProduto(itemPedido.getProduto().getDescricao())
                .precoUnitario(itemPedido.getProduto().getPreco())
                .quantidade(itemPedido.getQuantidade())
                .build()
        ).collect(Collectors.toList());
    }

}
