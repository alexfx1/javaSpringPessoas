package io.github.alexfx1.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

import io.github.alexfx1.domain.dto.user.*;
import io.github.alexfx1.domain.entity.Pessoa;
import io.github.alexfx1.domain.entity.Usuario;
import io.github.alexfx1.domain.exceptions.SenhaInvalidaException;
import io.github.alexfx1.domain.repository.UsuarioRepository;
import io.github.alexfx1.domain.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Environment environment;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByLogin(s)
                .orElseThrow(() ->  new UsernameNotFoundException("Usuario não encontrado"));

        String[] roles = usuario.isAdmin() ? new String[]{"ADMIN", "USER"} : new String[]{"USER"};

        return User.builder()
                .username(usuario.getLogin())
                .password(usuario.getSenha())
                .roles(roles)
                .build();
    }

    @Transactional
    public Usuario salvarUsuario(UserCreatePostDTO userCreatePostDTO){
        try {
            Usuario usuario = Usuario.builder()
                    .login(userCreatePostDTO.getLogin())
                    .senha(userCreatePostDTO.getSenha())
                    .email(userCreatePostDTO.getEmail())
                    .admin(userCreatePostDTO.isAdmin())
                    .build();

            String encode = encoder.encode(usuario.getSenha());
            usuario.setSenha(encode);
            return usuarioRepository.save(usuario);

        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"could not create user: " + ex.getMessage());
        }
    }

    public TokenDTO autenticar(UserCreateDTO userCreateDTO){
        try {

            Usuario usuario = Usuario.builder()
                    .login(userCreateDTO.getLogin())
                    .senha(userCreateDTO.getSenha())
                    .build();
            UserDetails userDetails = autenticarUser(usuario);

            String token = jwtService.gerarToken(usuario);
            return new TokenDTO(usuario.getLogin(), token);

        } catch (UsernameNotFoundException | SenhaInvalidaException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,e.getMessage());
        }
    }

    private UserDetails autenticarUser(Usuario usuario) {
        UserDetails userDetails = loadUserByUsername(usuario.getLogin());
        boolean verifyPassword = encoder.matches(usuario.getSenha(), userDetails.getPassword());

        if(!verifyPassword){
            throw new SenhaInvalidaException();
        }

        return userDetails;
    }

    public List<UserResponseDTO> findUsers(UserSearchDTO userSearchDTO) {
        Usuario user = Usuario.builder().login(userSearchDTO.getLogin()).email(userSearchDTO.getEmail()).build();

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<Usuario> example = Example.of(user,matcher);

        List<Usuario> users = usuarioRepository.findAll(example);

        if(users.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found");
        }

        return users.stream().map(usuario -> UserResponseDTO.builder()
                .login(usuario.getLogin()).build()).collect(Collectors.toList());
    }

    public String forgetPassword(String email, Integer code, String passwordNew) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if(usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found");
        }

        if(!Objects.equals(code, usuario.getPasswordCode())) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "código invalido para trocar a senha");
        }

        String encode = encoder.encode(passwordNew);
        usuario.setSenha(encode);

        usuarioRepository.save(usuario);
        return "your password has been updated!";
    }

    public String generateCode(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if(usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found");
        }

        try {
            Random random = new Random();
            Integer code = 1000 + random.nextInt(9000);
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(email);
            msg.setFrom(environment.getRequiredProperty("mail.from"));
            msg.setSubject("EMAIL VERIFICATION");
            msg.setText("your code verification is: " + code);

            usuario.setPasswordCode(code);
            javaMailSender.send(msg);
            usuarioRepository.save(usuario);

            return msg.getText();

        } catch (Exception ex) {
            System.out.println("Houve um erro ao enviar o email: " + ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Sistema server: " + ex);
        }
    }

    public UserUpdatePutDTO userPut(long id, UserUpdatePutDTO userUpdatePutDTO) {
        Usuario usuario = usuarioRepository.findById(id);
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found");
        }

        // Login
        if (userUpdatePutDTO.getLogin() != null && !userUpdatePutDTO.getLogin().isEmpty()) {
            usuario.setLogin(userUpdatePutDTO.getLogin());
        }

        // Email
        if (userUpdatePutDTO.getEmail() != null && !userUpdatePutDTO.getEmail().isEmpty()) {
            usuario.setEmail(userUpdatePutDTO.getEmail());
        }

        usuario.setAdmin(userUpdatePutDTO.isAdmin());
        usuarioRepository.save(usuario);

        return userUpdatePutDTO;
    }


    public List<Usuario> getAll() {
        List<Usuario> users = usuarioRepository.findAll();
        if(users.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "empty list");
        }
        return users;
    }
}
