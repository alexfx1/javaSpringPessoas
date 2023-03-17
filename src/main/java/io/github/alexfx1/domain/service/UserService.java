package io.github.alexfx1.domain.service;

import io.github.alexfx1.domain.dto.user.TokenDTO;
import io.github.alexfx1.domain.dto.user.UserCreateDTO;
import io.github.alexfx1.domain.dto.user.UserCreatePostDTO;
import io.github.alexfx1.domain.entity.Usuario;
import io.github.alexfx1.domain.exceptions.SenhaInvalidaException;
import io.github.alexfx1.domain.repository.UsuarioRepository;
import io.github.alexfx1.domain.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        Usuario usuario = Usuario.builder()
                .login(userCreatePostDTO.getLogin())
                .senha(userCreatePostDTO.getSenha())
                .admin(userCreatePostDTO.isAdmin())
                .build();

        String encode = encoder.encode(usuario.getSenha());
        usuario.setSenha(encode);
        return usuarioRepository.save(usuario);
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
}
