package br.com.eliel.gestao_vagas.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthenticationExceptionTest {

    @Test
    void testDefaultConstructor() {
        AuthenticationException exception = new AuthenticationException();
        assertEquals("Usuario/senha incorreto", exception.getMessage());
    }

    @Test
    void testCustomMessageConstructor() {
        String customMessage = "Custom error message";
        AuthenticationException exception = new AuthenticationException(customMessage);
        assertEquals(customMessage, exception.getMessage());
    }
}
