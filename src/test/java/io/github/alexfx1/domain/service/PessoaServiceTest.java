package io.github.alexfx1.domain.service;


import static org.junit.Assert.assertEquals;
import io.github.alexfx1.domain.entity.Endereco;
import io.github.alexfx1.domain.entity.Pessoa;
import io.github.alexfx1.domain.repository.PessoaRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import org.junit.jupiter.api.Assertions;
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


public class PessoaServiceTest {

    @InjectMocks
    private PessoaService pessoaService;

    @Mock
    private PessoaRepository pessoaRepository;

    private List<Endereco> createEndereco(){
        List<Endereco> enderecoList = new ArrayList<>();
        Endereco endereco = new Endereco();

        endereco.setIdEndereco(1L);
        endereco.setNumero(1300);
        endereco.setCep("31265779");
        endereco.setEnderecoPrincipal(true);
        endereco.setCidade("Uberlândia");
        endereco.setPessoaId(1L);
        endereco.setLogradouro("Rua Abreu");

        enderecoList.add(endereco);
        return enderecoList;
    }

    private Pessoa createPessoa(){
        Pessoa pessoa = new Pessoa();

        pessoa.setIdPessoa(1L);
        pessoa.setJsonEnderecos("");
        pessoa.setNome("Alex");
        pessoa.setDtNascimento(new Date());
        pessoa.setEnderecos(createEndereco());

        return pessoa;
    }

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createPessoa_Success(){
        Pessoa pessoa = createPessoa();

        doReturn(pessoa).when(pessoaRepository).save(any(Pessoa.class));

        Pessoa result = pessoaService.criarPessoa(pessoa);

        assertEquals(result,pessoa);
    }

    @Test
    public void createPessoa_Failed() {
        String msgError = "Pessoa Nula";

        when(pessoaRepository.save(any())).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        assertThrows(ResponseStatusException.class,()->{
            pessoaService.criarPessoa(null);
        }, msgError);
    }

    @Test
    public void listarPessoas_Success(){
        List<Pessoa> pessoas = new ArrayList<>();
        pessoas.add(createPessoa());

        when(pessoaRepository.findAll()).thenReturn(pessoas);

        List<Pessoa> pessoaList = pessoaService.listarPessoas();

        assertEquals(pessoas,pessoaList);
    }

    @Test
    public void listarPessoas_Failed(){
        String msgError = "Lista vazia/nao encontrada";
        when(pessoaRepository.findAll()).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        assertThrows(ResponseStatusException.class,()->{
            pessoaService.listarPessoas();
        }, msgError);
    }

    @Test
    public void consultarPessoa_Success(){

        when(pessoaRepository.findById(anyLong())).thenReturn(createPessoa());

        Pessoa pessoaEncontrada = pessoaService.consultarPessoa(1L);

        Assertions.assertEquals(1L,pessoaEncontrada.getIdPessoa());
    }

    @Test
    public void consultarPessoa_Failed(){
        String msgError = "Pessoa nao encontrada";
        when(pessoaRepository.findById(anyLong())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        assertThrows(ResponseStatusException.class,()->{
            pessoaService.consultarPessoa(0L);
        }, msgError);
    }

    @Test
    public void editarPessoa_Success(){
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("Test_name");
        pessoa.setDtNascimento(new Date());

        Pessoa pessoaNotUpdated = createPessoa();
        when(pessoaRepository.findById(anyLong())).thenReturn(pessoaNotUpdated);

        when(pessoaRepository.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        Pessoa pessoaUpdated = pessoaService.editarPessoa(pessoa,pessoaNotUpdated.getIdPessoa());

        assertEquals("Test_name",pessoaUpdated.getNome());
        assertEquals(pessoa.getDtNascimento(),pessoaUpdated.getDtNascimento());
    }

    @Test
    public void editarPessoa_Failed(){
        String msgError = "Erro ao editar";
        when(pessoaRepository.findById(anyLong())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        when(pessoaRepository.save(any())).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        assertThrows(ResponseStatusException.class,()->{
            pessoaService.editarPessoa(createPessoa(),0L);
        }, msgError);
    }

}
