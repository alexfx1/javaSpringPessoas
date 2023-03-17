package io.github.alexfx1.domain.exceptions;

public class SenhaInvalidaException extends RuntimeException {
    public SenhaInvalidaException() {
        super("Senha incorreta.");
    }
}
