package io.github.alexfx1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication
public class PessoasApplication {

    public static void main(String[] args) {
        SpringApplication.run(PessoasApplication.class, args);
    }
    // When application is running please access: http://localhost:8081/swagger-ui.html
    // User for test login: alex, 123
}
