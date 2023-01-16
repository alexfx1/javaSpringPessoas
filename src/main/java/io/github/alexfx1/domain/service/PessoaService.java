package io.github.alexfx1.domain.service;

import io.github.alexfx1.domain.entity.Pessoa;
import io.github.alexfx1.domain.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PessoaService {
    @Autowired
    private PessoaRepository pessoaRepository;

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

    @Transactional
    public void updateListaEnderecos(Long idPessoa, String json){
        pessoaRepository.listaEnderecos(idPessoa, json);
    }
}

