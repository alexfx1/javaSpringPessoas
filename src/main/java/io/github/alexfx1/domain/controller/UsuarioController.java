package io.github.alexfx1.domain.controller;

import io.github.alexfx1.domain.dto.user.*;
import io.github.alexfx1.domain.entity.Usuario;
import io.github.alexfx1.domain.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @GetMapping("/find")
    @ApiOperation(value = "Encontrar usuarios cadastrados")
    public ResponseEntity<List<UserResponseDTO>> findUsers(UserSearchDTO userSearchDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findUsers(userSearchDTO));
    }

    @GetMapping
    @ApiOperation(value = "Todos os usuarios")
    public ResponseEntity<List<Usuario>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAll());
    }

    @PutMapping
    @ApiOperation(value = "Atualiza os dados do usuario")
    public ResponseEntity<UserUpdatePutDTO> atualizarUsuario(@RequestParam long id, @RequestBody UserUpdatePutDTO userUpdatePutDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.userPut(id,userUpdatePutDTO));
    }

    @PatchMapping("/valid-email")
    @ApiOperation(value = "gera codigo para validar email")
    public String generateCode(@RequestParam String email) {
        return userService.generateCode(email);
    }

    @PatchMapping("/forget-password")
    @ApiOperation(value = "mudar de senha")
    public String generateNewPassword(@RequestParam String email, int code, String passwordNew) {
        return userService.forgetPassword(email,code,passwordNew);
    }
}
