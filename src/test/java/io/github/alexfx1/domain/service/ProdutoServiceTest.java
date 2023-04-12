package io.github.alexfx1.domain.service;



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

import java.util.Collections;
import java.util.List;

import io.github.alexfx1.domain.entity.Produto;
import io.github.alexfx1.domain.repository.ProdutoRepository;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

public class ProdutoServiceTest {
    @InjectMocks
    private ProdutoService produtoService;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ExampleMatcher exampleMatcher;

    private Produto createProduto() {
        return Produto.builder()
                .idProduto(1L)
                .nomeProduto("Orange juice")
                .marca("Heal")
                .preco(BigDecimal.valueOf(7.66))
                .descricao("orange juice light 0 sugar")
                .build();
    }

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void create_productSuccess() {
        Produto produto = createProduto();

        doReturn(produto).when(produtoRepository).save(any());

        Produto result = produtoService.saveProduct(produto);

        assertEquals(produto,result);
    }

    @Test
    public void createProduto_null_failed() {
        String msgError = "Produto nulo";

        when(produtoRepository.save(any())).thenThrow(new NullPointerException());

        assertThrows(NullPointerException.class,()-> produtoService.saveProduct(null), msgError);
    }

    @Test
    public void create_produto_failedbynamemarca() {
        Produto product_one = createProduto();

        Produto product_save_wrong = Produto.builder()
                .descricao("test")
                .preco(BigDecimal.valueOf(7.66))
                .marca("Heal")
                .nomeProduto("Orange juice")
                .idProduto(2L)
                .build();

        when(produtoRepository.save(any())).thenReturn(any());
        produtoService.saveProduct(product_one);

        when(produtoRepository.save(any())).thenThrow(new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE));
        assertThrows(ResponseStatusException.class, () -> produtoService.saveProduct(product_save_wrong));

    }

    @Test
    public void create_produto_failed_campos_vazios() {
        Produto product_one = createProduto();

        Produto product_save_wrong = Produto.builder()
                .descricao("test")
                .marca("Heal")
                .nomeProduto("")
                .idProduto(2L)
                .build();

        when(produtoRepository.save(any())).thenReturn(any());
        produtoService.saveProduct(product_one);

        when(produtoRepository.save(any())).thenThrow(new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE));
        assertThrows(ResponseStatusException.class, () -> produtoService.saveProduct(product_save_wrong));
    }

    @Test
    public void listar_produtos_success() {
        List<Produto> produtoList = Collections.singletonList(createProduto());

        when(produtoRepository.findAll()).thenReturn(produtoList);

        List<Produto> produtoListResult = produtoService.getAllProducts();

        assertEquals(produtoList,produtoListResult);
    }

    @Test
    public void consultar_produto_success() {
        when(produtoRepository.findById(anyLong())).thenReturn(createProduto());
        Produto produtoResult = produtoService.consultarProduto(1L);

        Assertions.assertEquals(1L,produtoResult.getIdProduto());
    }

    /*@Test TODO
    public void consultar_produto_matcher() {
        List<Produto> produtoList = Collections.singletonList(createProduto());
        Example<Produto> example = Example.of(createProduto(),exampleMatcher);

        when(produtoRepository.findAll(example)).thenReturn(produtoList);
        List<Produto> produtoResult = produtoService.consultarProdutoPorFiltro(createProduto());

        verify(produtoRepository, times(1)).findAll(example);
    }*/

    @Test
    public void consultar_produto_failed() {
        String msgError = "Produto nao encontrado";

        when(produtoRepository.findById(anyLong())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        assertThrows(ResponseStatusException.class,()->{
            produtoService.consultarProduto(createProduto().getIdProduto());
        }, msgError);
    }

    @Test
    public void alterar_produto_success() {
        Produto newProduct = Produto.builder()
                .nomeProduto("test name")
                .preco(BigDecimal.valueOf(5.99))
                .marca("test")
                .descricao("...")
                .build();

        Produto defaultProduct = createProduto();

        when(produtoRepository.findById(anyLong())).thenReturn(defaultProduct);
        when(produtoRepository.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        Produto productUpdated = produtoService.editarProduto(newProduct,defaultProduct.getIdProduto());

        assertEquals("test name",productUpdated.getNomeProduto());
        assertEquals("test",productUpdated.getMarca());
        assertEquals(BigDecimal.valueOf(5.99),productUpdated.getPreco());
        assertEquals("...",productUpdated.getDescricao());
    }

    @Test
    public void alterar_produto_failed() {
        String msgError = "produto nao encontrado";

        when(produtoRepository.findById(anyLong())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        when(produtoRepository.save(any())).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        assertThrows(ResponseStatusException.class,()->{
            produtoService.editarProduto(createProduto(),0L);
        }, msgError);
    }

    @Test
    public void deletar_produto_success() {
        when(produtoRepository.findById(anyLong())).thenReturn(createProduto());
        Produto produtoResult = produtoService.consultarProduto(1L);

        doNothing().when(produtoRepository).delete(any());
        produtoService.deletarProduto(produtoResult.getIdProduto());

        Assertions.assertEquals(1L,produtoResult.getIdProduto());
        verify(produtoRepository, times(1)).delete(produtoResult);
    }

    @Test
    public void deletar_produto_failed() {
        String msgError = "produto nao encontrado";

        when(produtoRepository.findById(anyLong())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        doNothing().when(produtoRepository).delete(any());

        assertThrows(ResponseStatusException.class,()->{
            produtoService.deletarProduto(createProduto().getIdProduto());
        }, msgError);
    }
}
