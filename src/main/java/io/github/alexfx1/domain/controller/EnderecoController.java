package io.github.alexfx1.domain.controller;

import io.github.alexfx1.domain.dto.EnderecoDTO;
import io.github.alexfx1.domain.entity.Endereco;
import io.github.alexfx1.domain.service.EnderecoService;
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
@Api(value = "Api Rest Endereco")
@CrossOrigin(origins = "*")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    @PostMapping("/endereco")
    @ApiOperation(value = "Criar Endereco Para pessoa")
    public ResponseEntity<Endereco> savarEnderecoParaPessoa(@RequestBody EnderecoDTO enderecoDTO){
        Endereco endereco = new Endereco();
        BeanUtils.copyProperties(enderecoDTO,endereco);
        return ResponseEntity.status(HttpStatus.CREATED).body(enderecoService.criarEndereco(endereco));
    }

    @GetMapping("/endereco/listar")
    @ApiOperation(value = "Lista todos os enderecos para a pessoa cadastrada")
    public ResponseEntity<List<Endereco>> listarEnderecos(@RequestParam Long idPessoa){
        List<Endereco> list = enderecoService.listarEnderecos(idPessoa);
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    @GetMapping("/endereco/{id}")
    @ApiOperation(value = "Lista todos os enderecos principais para a pessoa")
    public ResponseEntity<List<Endereco>> enderecoPrincipal(@RequestParam Long idPessoa){
        List<Endereco> list = enderecoService.enderecoPrincipal(idPessoa);
        return new ResponseEntity<>(list,HttpStatus.OK);
    }
}


