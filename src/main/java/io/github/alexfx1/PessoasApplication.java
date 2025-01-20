package io.github.alexfx1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@Slf4j
@SpringBootApplication
public class PessoasApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext app = new SpringApplicationBuilder(PessoasApplication.class).run();
        Environment env = app.getEnvironment();
        String applicationPort = env.getProperty("server.port");
        log.info("Access URL for development: http://localhost:{}/swagger-ui.html", applicationPort);
    }
}
