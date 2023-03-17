package io.github.alexfx1.domain.controller;

import io.github.alexfx1.domain.dto.PessoaDTO;
import io.github.alexfx1.domain.entity.Pessoa;
import io.github.alexfx1.domain.service.PessoaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/pessoa")
@Api(value = "Api Rest Pessoas")
public class PessoaController {
    @Autowired
    private PessoaService pessoaService;

    @PostMapping
    @ApiOperation(value = "Cadastrar Pessoa")
    public ResponseEntity<Pessoa> savarPessoa(@Valid @RequestBody PessoaDTO pessoaDTO){
        Pessoa pessoa = new Pessoa();
        BeanUtils.copyProperties(pessoaDTO,pessoa);
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaService.criarPessoa(pessoa));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Consultar Pessoa")
    @ApiResponses({@ApiResponse(code = 404, message = "pessoa nao encontrada"), @ApiResponse(code = 200, message = "pessoa encontrada")})
    public ResponseEntity<Pessoa> getById(@PathVariable Long id){
        Pessoa pessoa = pessoaService.consultarPessoa(id);
        return new ResponseEntity<>(pessoa,HttpStatus.OK);
    }

    @GetMapping("/listar")
    @ApiOperation(value = "Listar Pessoas Cadastradas")
    public ResponseEntity<List<Pessoa>> listarPessoas(){
        List<Pessoa> list = pessoaService.listarPessoas();
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    @GetMapping("/filtro")
    @ApiOperation(value = "Consultar pessoas por diferentes filtros")
    public ResponseEntity<List<Pessoa>> consultarPessoaFiltro(PessoaDTO pessoaDTO){
        Pessoa pessoa = new Pessoa();
        BeanUtils.copyProperties(pessoaDTO,pessoa);
        List<Pessoa> pessoas = pessoaService.consultarPorFiltro(pessoa);
        return new ResponseEntity<>(pessoas,HttpStatus.OK);
    }

    @PutMapping("/alterar/{id}")
    @ApiOperation(value = "Editar Pessoa")
    public ResponseEntity<Pessoa> alteraPessoa(@RequestBody @Valid PessoaDTO pessoa, @PathVariable long id){
        Pessoa novaPessoa = new Pessoa();
        BeanUtils.copyProperties(pessoa,novaPessoa);
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaService.editarPessoa(novaPessoa,id));
    }

    @DeleteMapping
    @ApiOperation(value = "Deleta pessoa e seus enderecos no banco")
    public void deletarPessoa(@RequestParam Long idPessoa){
        pessoaService.deletarPessoa(idPessoa);
    }

}

