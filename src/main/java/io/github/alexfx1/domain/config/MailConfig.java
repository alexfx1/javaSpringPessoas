package io.github.alexfx1.domain.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Autowired
    private Environment environment;

    @Bean
    public JavaMailSenderImpl mailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(environment.getRequiredProperty("mail.host"));
        javaMailSender.setPort(Integer.parseInt(environment.getRequiredProperty("mail.port")));
        javaMailSender.setUsername(environment.getRequiredProperty("mail.username"));
        javaMailSender.setPassword(environment.getRequiredProperty("mail.password"));

        javaMailSender.setPort(Integer.parseInt(environment.getRequiredProperty("mail.port")));

        // Enable STARTTLS for secure connection
        Properties props = javaMailSender.getJavaMailProperties();
        props.put("mail.smtp.starttls.enable", "true");

        // Specify supported protocols and cipher suites
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.ssl.ciphersuites", "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256");
        props.put("mail.smtp.ssl.trust", "*");

        javaMailSender.setJavaMailProperties(props);

        return javaMailSender;
    }

}
