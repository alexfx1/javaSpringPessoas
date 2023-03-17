package io.github.alexfx1.domain.service;

import io.github.alexfx1.domain.entity.Produto;
import io.github.alexfx1.domain.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;


@Service
public class ProdutoService {
    @Autowired
    private ProdutoRepository produtoRepository;

    public Produto saveProduct(Produto produto) {

        if(Objects.equals(produto.getNomeProduto(), getProductByName(produto.getNomeProduto())) && Objects.equals(produto.getMarca(), getMarcaName(produto.getMarca()))){
            System.out.println("Produto para esta marca já existente");
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Produto para esta marca já existente");
        }

        if(produto.getNomeProduto().isEmpty() || produto.getPreco() == null){
            System.out.println("Preenchimento incompleto!");
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Preenchimento dos dados obrigatórios de forma errada!");
        }

        return produtoRepository.save(produto);
    }

    public List<Produto> getAllProducts(){
        return produtoRepository.findAll();
    }

    public List<Produto> consultarProdutoPorFiltro(Produto produto) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<Produto> example = Example.of(produto,matcher);

        return produtoRepository.findAll(example);
    }

    public Produto consultarProduto(long id){
        try {
            return produtoRepository.findById(id);
        } catch (Exception ex){
            System.out.println("Produto com este id nao encontrado! --> " + id + " " + ex);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa com este id nao encontrada!");
        }
    }

    public Produto editarProduto(Produto produto, long id){
        try {
            Produto produtoAlterado = produtoRepository.findById(id);

            if(produto.getNomeProduto().isEmpty() || produto.getPreco() == null){
                System.out.println("Preenchimento incompleto!");
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Preenchimento dos dados obrigatórios de forma errada!");
            }

            produtoAlterado.setNomeProduto(produto.getNomeProduto());
            produtoAlterado.setMarca(produto.getMarca());
            produtoAlterado.setPreco(produto.getPreco());
            produtoAlterado.setDescricao(produto.getDescricao());
            return produtoRepository.save(produtoAlterado);

        } catch (Exception ex){
            System.out.println("produto nao encontrado! --> " + id + " " + ex);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "produto com este id nao encontrado!");
        }
    }

    public void deletarProduto(long id){
        try {
            Produto produtoExcluido = consultarProduto(id);
            produtoRepository.delete(produtoExcluido);
        } catch (Exception ex) {
            System.out.println("Erro ou Produto não encontrado" + " " + ex);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto com este id nao encontrado");
        }
    }

    private String getProductByName(String nomeProduto) {
        return produtoRepository.findByName(nomeProduto);
    }

    private String getMarcaName(String marca){
        return produtoRepository.findByNameMarca(marca);
    }
}
