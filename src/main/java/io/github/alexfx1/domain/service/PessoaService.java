package io.github.alexfx1.domain.service;

import io.github.alexfx1.domain.entity.Pessoa;
import io.github.alexfx1.domain.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PessoaService {
    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private EnderecoService enderecoService;

    @Transactional
    public Pessoa criarPessoa(Pessoa pessoa){
        try {
            return pessoaRepository.save(pessoa);
        }catch (Exception ex){
            System.out.println("Erro ao criar pessoa.." + " " + ex);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    public List<Pessoa> listarPessoas(){
        try {
            return pessoaRepository.findAll();
        } catch (Exception ex){
            System.out.println("Lista nao encontrada.." + ex);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lista nao encontrada");
        }
    }

    public Pessoa consultarPessoa(long id){
        try {
            return pessoaRepository.findById(id);
        } catch (Exception ex){
            System.out.println("Pessoa com este id nao encontrada! --> " + id + " " + ex);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa com este id nao encontrada!");
        }
    }

    public List<Pessoa> consultarPorFiltro(Pessoa pessoa) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                                                .withIgnoreCase()
                                                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<Pessoa> example = Example.of(pessoa,matcher);

        return pessoaRepository.findAll(example);
    }

    public Pessoa editarPessoa(Pessoa pessoa, long id){
        try {
            Pessoa pessoaAlterada = pessoaRepository.findById(id);
            pessoaAlterada.setNome(pessoa.getNome());
            pessoaAlterada.setDtNascimento(pessoa.getDtNascimento());
            return pessoaRepository.save(pessoaAlterada);
        } catch (Exception ex){
            System.out.println("Pessoa nao encontrada! --> " + id + " " + ex);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa com este id nao encontrada!");
        }
    }

    public void deletarPessoa(long id){
        try {
            Pessoa pessoaExcluida = pessoaRepository.findById(id);
            pessoaRepository.delete(pessoaExcluida);
            enderecoService.deletarEnderecosParaPessoa(pessoaExcluida.getIdPessoa());
        } catch (Exception ex) {
            System.out.println("Pessoa nao encontrada! --> " + id + " " + ex);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa com este id nao encontrada!");
        }
    }

    @Transactional
    public void updateListaEnderecos(Long idPessoa, String json){
        pessoaRepository.listaEnderecos(idPessoa, json);
    }
}

