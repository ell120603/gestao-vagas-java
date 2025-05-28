package br.com.eliel.gestao_vagas.exceptions;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException() {
        super("Username/password incorrect");
    }

    public AuthenticationException(String message) {
        super(message);
    }
} 