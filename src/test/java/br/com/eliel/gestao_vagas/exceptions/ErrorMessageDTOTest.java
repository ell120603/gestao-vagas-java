package br.com.eliel.gestao_vagas.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorMessageDTOTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        ErrorMessageDTO dto = new ErrorMessageDTO("Campo obrigatório", "email");
        assertEquals("Campo obrigatório", dto.getMessage());
        assertEquals("email", dto.getField());
    }

    @Test
    void testSetters() {
        ErrorMessageDTO dto = new ErrorMessageDTO(null, null);
        dto.setMessage("Erro de validação");
        dto.setField("senha");

        assertEquals("Erro de validação", dto.getMessage());
        assertEquals("senha", dto.getField());
    }

    @Test
    void testEqualsAndHashCode() {
        ErrorMessageDTO dto1 = new ErrorMessageDTO("msg", "campo");
        ErrorMessageDTO dto2 = new ErrorMessageDTO("msg", "campo");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ErrorMessageDTO dto = new ErrorMessageDTO("mensagem", "campo");
        String str = dto.toString();
        assertTrue(str.contains("mensagem"));
        assertTrue(str.contains("campo"));
    }
}
