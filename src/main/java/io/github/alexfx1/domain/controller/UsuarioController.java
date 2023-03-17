package io.github.alexfx1.domain.controller;

import io.github.alexfx1.domain.dto.user.TokenDTO;
import io.github.alexfx1.domain.dto.user.UserCreateDTO;
import io.github.alexfx1.domain.dto.user.UserCreatePostDTO;
import io.github.alexfx1.domain.entity.Usuario;
import io.github.alexfx1.domain.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/usuario")
@Api(value = "Api Rest usuario")
public class UsuarioController {
    @Autowired
    private UserService userService;

    @PostMapping
    @ApiOperation(value = "Cadastrar Usuario")
    public ResponseEntity<Usuario> salvar(@RequestBody @Valid UserCreatePostDTO userCreatePostDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.salvarUsuario(userCreatePostDTO));
    }

    @PostMapping("/auth")
    @ApiOperation(value = "Gerar token / login")
    public ResponseEntity<TokenDTO> autenticarToken(@RequestBody UserCreateDTO userCreateDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.autenticar(userCreateDTO));
    }
}
