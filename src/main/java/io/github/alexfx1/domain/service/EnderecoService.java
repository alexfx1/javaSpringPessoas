package io.github.alexfx1.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.alexfx1.domain.entity.Endereco;
import io.github.alexfx1.domain.entity.Pessoa;
import io.github.alexfx1.domain.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EnderecoService {
    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private PessoaService pessoaService;

    public Endereco criarEndereco(Endereco endereco) throws ResponseStatusException {
        try{
            Pessoa pessoa = pessoaService.consultarPessoa(endereco.getPessoaId());
            endereco.setPessoa(pessoa);
            List<Endereco> enderecos = enderecoRepository.enderecoPrincipal(endereco.getPessoaId());

            if(!enderecos.isEmpty() && endereco.isEnderecoPrincipal()){
                System.out.println("Endereco principal ja existe para este id");
                throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED,"Endereco principal ja existe para este id");
            }

            enderecoRepository.save(endereco);
            String json = toJson(pessoa);
            pessoaService.updateListaEnderecos(endereco.getPessoaId(),json);

            return endereco;
        } catch (Exception ex) {
            System.out.println("Endereco para pessoa com este id nao encontrado! --> " + endereco.getPessoaId() + " "  + ex);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereco para pessoa com este id nao encontrado!");
        }
    }

    public List<Endereco> listarEnderecos(Long idPessoa) {
        try{
            Pessoa pessoa = pessoaService.consultarPessoa(idPessoa);
            return enderecoRepository.listarEnderecosPessoa(pessoa.getIdPessoa());
        } catch (Exception ex) {
            System.out.println("Lista de enderecos para este id nao encontrada! --> " + idPessoa + " " + ex);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereco para pessoa com este id nao encontrado!");
        }
    }

    public List<Endereco> enderecoPrincipal(Long idPessoa){
        try {
            Pessoa pessoa = pessoaService.consultarPessoa(idPessoa);
            return enderecoRepository.enderecoPrincipal(pessoa.getIdPessoa());
        } catch (Exception ex){
            System.out.println("Endereco principal para este id nao encontrado! --> " + idPessoa + " " + ex);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereco principal para este id nao encontrado!");
        }
    }

    private String toJson(Pessoa pessoa) {
        String body = "";
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            body = objectMapper.writeValueAsString(pessoa.getEnderecos());
        } catch (Exception ex){
            System.out.println("erro ao serializar obj" + " " + ex);
        }
        return body;
    }

}
