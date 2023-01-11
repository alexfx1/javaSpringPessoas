package io.github.alexfx1;

import io.github.alexfx1.domain.entity.Cliente;
import io.github.alexfx1.domain.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class VendasApplication {

    @Autowired
    private ClienteRepository clienteRepository;

    @Bean
    public CommandLineRunner init(){
        return args -> {
            clienteRepository.salvar(new Cliente("Alex"));
            clienteRepository.salvar(new Cliente("Ruy"));

            List<Cliente> todosClientes = clienteRepository.getAllClientes();
            todosClientes.forEach(System.out::println);
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(VendasApplication.class, args);
    }

}
