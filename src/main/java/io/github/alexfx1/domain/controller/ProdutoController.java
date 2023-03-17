package io.github.alexfx1.domain.controller;

import io.github.alexfx1.domain.dto.ProdutoDTO;
import io.github.alexfx1.domain.entity.Produto;
import io.github.alexfx1.domain.service.ProdutoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/produto")
@Api(value = "Api Rest Produtos")
public class ProdutoController {
    @Autowired
    private ProdutoService produtoService;

    @PostMapping
    @ApiOperation(value = "Adicionar Produto")
    public ResponseEntity<Produto> saveProduct(@RequestBody @Valid ProdutoDTO produtoDTO) {
        Produto produto = new Produto();
        BeanUtils.copyProperties(produtoDTO,produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.saveProduct(produto));
    }

    @GetMapping
    @ApiOperation(value = "Lista de produtos")
    public ResponseEntity<List<Produto>> getAllProducts(){
        List<Produto> produtoList = produtoService.getAllProducts();
        return ResponseEntity.ok().body(produtoList);
    }

    @GetMapping("/filtro")
    @ApiOperation(value = "Lista de produtos por filtro")
    public ResponseEntity<List<Produto>> consultarPorFiltro(ProdutoDTO produtoDTO){
        Produto produto = new Produto();
        BeanUtils.copyProperties(produtoDTO,produto);
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.consultarProdutoPorFiltro(produto));
    }

    @PutMapping("/alterar/{id}")
    @ApiOperation(value = "Editar produto")
    public ResponseEntity<Produto> alteraPessoa(@RequestBody @Valid ProdutoDTO produtoDTO, @PathVariable long id){
        Produto novoProduto = new Produto();
        BeanUtils.copyProperties(produtoDTO,novoProduto);
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.editarProduto(novoProduto,id));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Exclui um produto")
    public void deletaProduto(@PathVariable Long idProduto){
        produtoService.deletarProduto(idProduto);
    }

}
