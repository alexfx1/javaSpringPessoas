package io.github.alexfx1.domain.controller;

import io.github.alexfx1.domain.dto.PessoaDTO;
import io.github.alexfx1.domain.entity.Pessoa;
import io.github.alexfx1.domain.service.PessoaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
@Api(value = "Api Rest Pessoas")
@CrossOrigin(origins = "*")
public class PessoaController {
    @Autowired
    private PessoaService pessoaService;

    @PostMapping("/pessoa")
    @ApiOperation(value = "Cadastrar Pessoa")
    public ResponseEntity<Pessoa> savarPessoa(@RequestBody PessoaDTO pessoaDTO){
        Pessoa pessoa = new Pessoa();
        BeanUtils.copyProperties(pessoaDTO,pessoa);
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaService.criarPessoa(pessoa));
    }

    @GetMapping("/pessoa/{id}")
    @ApiOperation(value = "Consultar Pessoa")
    public ResponseEntity<Pessoa> getById(@PathVariable Long id){
        Pessoa pessoa = pessoaService.consultarPessoa(id);
        return new ResponseEntity<>(pessoa,HttpStatus.OK);
    }

    @GetMapping("/pessoa/listar")
    @ApiOperation(value = "Listar Pessoas Cadastradas")
    public ResponseEntity<List<Pessoa>> listarPessoas(){
        List<Pessoa> list = pessoaService.listarPessoas();
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    @PutMapping("pessoa/alterar/{id}")
    @ApiOperation(value = "Editar Pessoa")
    public ResponseEntity<Pessoa> alteraPessoa(@RequestBody Pessoa pessoa, @PathVariable long id){
        Pessoa novaPessoa = pessoaService.editarPessoa(pessoa,id);
        return new ResponseEntity<>(novaPessoa,HttpStatus.CREATED);
    }

}

