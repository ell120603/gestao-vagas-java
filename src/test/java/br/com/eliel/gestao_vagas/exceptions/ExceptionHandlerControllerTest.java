package br.com.eliel.gestao_vagas.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExceptionHandlerControllerTest {

    @Test
    void testMethodArgumentNotValidException() {
        MessageSource messageSource = mock(MessageSource.class);
        ExceptionHandlerController controller = new ExceptionHandlerController(messageSource);

        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError1 = new FieldError("obj", "email", "default");
        FieldError fieldError2 = new FieldError("obj", "senha", "default");

        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));
        when(messageSource.getMessage(eq(fieldError1), any(Locale.class))).thenReturn("Email inválido");
        when(messageSource.getMessage(eq(fieldError2), any(Locale.class))).thenReturn("Senha obrigatória");

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<List<ErrorMessageDTO>> response = controller.methodArgumentNotValidException(ex);

        assertEquals(400, response.getStatusCodeValue());
        List<ErrorMessageDTO> body = response.getBody();
        assertNotNull(body);
        assertEquals(2, body.size());
        assertEquals("Email inválido", body.get(0).getMessage());
        assertEquals("email", body.get(0).getField());
        assertEquals("Senha obrigatória", body.get(1).getMessage());
        assertEquals("senha", body.get(1).getField());
    }
}
