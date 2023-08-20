package io.github.alexfx1.domain.service;


import static org.junit.Assert.assertEquals;
import io.github.alexfx1.domain.entity.Endereco;
import io.github.alexfx1.domain.entity.Pessoa;
import io.github.alexfx1.domain.repository.EnderecoRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class EnderecoServiceTest {

    @InjectMocks
    private EnderecoService enderecoService;

    @Mock
    private EnderecoRepository enderecoRepository;

    @Mock
    private PessoaService pessoaService;

    private List<Endereco> createListEndereco(){
        List<Endereco> enderecoList = new ArrayList<>();
        Endereco endereco = createEndereco();

        enderecoList.add(endereco);
        return enderecoList;
    }

    private Endereco createEndereco(){
        Endereco endereco = new Endereco();

        endereco.setIdEndereco(2L);
        endereco.setNumero(1300);
        endereco.setCep("31265779");
        endereco.setEnderecoPrincipal(false);
        endereco.setCidade("Uberlândia");
        endereco.setPessoaId(1L);
        endereco.setLogradouro("Rua Abreu");

        return endereco;
    }

    private Pessoa createPessoa(){
        Pessoa pessoa = new Pessoa();

        pessoa.setIdPessoa(1L);
        pessoa.setJsonEnderecos("");
        pessoa.setNome("Alex");
        pessoa.setDtNascimento(new Date());
        pessoa.setEnderecos(createListEndereco());

        return pessoa;
    }

    @Before
    public void initMocks(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createEndereco_Success(){
        Pessoa pessoa = createPessoa();
        Endereco endereco = createEndereco();

        when(pessoaService.consultarPessoa(anyLong())).thenReturn(pessoa);
        endereco.setPessoa(pessoa);

        when(enderecoRepository.enderecoPrincipal(anyLong())).thenReturn(pessoa.getEnderecos());

        doReturn(endereco).when(enderecoRepository).save(any(Endereco.class));
        Endereco result = enderecoService.criarEndereco(endereco);

        assertEquals(result,endereco);
    }

    @Test
    public void createEndereco_Failed() {
        String msgError = "Endereco null";

        when(pessoaService.consultarPessoa(anyLong())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        when(enderecoRepository.enderecoPrincipal(anyLong())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        when(enderecoRepository.save(any())).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        assertThrows(NullPointerException.class,()->{
            enderecoService.criarEndereco(null);
        }, msgError);
    }

    @Test
    public void listarEnderecos_Success(){
        Pessoa pessoa = createPessoa();
        List<Endereco> enderecoList = createListEndereco();
        enderecoList.add(createEndereco());

        when(pessoaService.consultarPessoa(anyLong())).thenReturn(pessoa);
        when(enderecoRepository.listarEnderecosPessoa(anyLong())).thenReturn(enderecoList);

        List<Endereco> returns = enderecoService.listarEnderecos(pessoa.getIdPessoa());

        assertEquals(enderecoList,returns);
    }

    @Test
    public void listarEnderecos_Failed(){
        String msgError = "Lista vazia/nao encontrada";
        when(pessoaService.consultarPessoa(anyLong())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        when(enderecoRepository.listarEnderecosPessoa(anyLong())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        assertThrows(ResponseStatusException.class,()->{
            enderecoService.listarEnderecos(0L);
        }, msgError);
    }

    @Test
    public void listEnderecoPrincipal_Success(){
        Pessoa pessoa = createPessoa();
        List<Endereco> enderecoList = createListEndereco();
        enderecoList.add(createEndereco());

        when(pessoaService.consultarPessoa(anyLong())).thenReturn(pessoa);
        when(enderecoRepository.enderecoPrincipal(anyLong())).thenReturn(enderecoList);

        List<Endereco> returns = enderecoService.enderecoPrincipal(pessoa.getIdPessoa());

        assertEquals(enderecoList,returns);
    }

    @Test
    public void listEnderecoPrincipal_Failed(){
        String msgError = "Lista vazia/nao encontrada";
        when(pessoaService.consultarPessoa(anyLong())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        when(enderecoRepository.listarEnderecosPessoa(anyLong())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        assertThrows(ResponseStatusException.class,()->{
            enderecoService.enderecoPrincipal(0L);
        }, msgError);
    }
}
