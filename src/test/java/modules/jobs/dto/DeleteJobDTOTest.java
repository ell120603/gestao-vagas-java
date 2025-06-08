package br.com.eliel.gestao_vagas.modules.jobs.dto;

import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class DeleteJobDTOTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void testBuilderAndGetters() {
        DeleteJobDTO dto = DeleteJobDTO.builder()
                .password("senha123")
                .build();

        assertEquals("senha123", dto.getPassword());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        DeleteJobDTO dto = new DeleteJobDTO();
        dto.setPassword("outraSenha");

        assertEquals("outraSenha", dto.getPassword());
    }

    @Test
    void testAllArgsConstructor() {
        DeleteJobDTO dto = new DeleteJobDTO("minhaSenha");

        assertEquals("minhaSenha", dto.getPassword());
    }

    @Test
    void testEqualsAndHashCode() {
        DeleteJobDTO dto1 = DeleteJobDTO.builder().password("abc").build();
        DeleteJobDTO dto2 = DeleteJobDTO.builder().password("abc").build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DeleteJobDTO dto = DeleteJobDTO.builder().password("senha").build();
        String str = dto.toString();
        assertTrue(str.contains("senha"));
    }

    @Test
    void testPasswordValidation() {
        DeleteJobDTO dto = new DeleteJobDTO();
        
        dto.setPassword("");
        Set<ConstraintViolation<DeleteJobDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals("A senha é obrigatória", violations.iterator().next().getMessage());

        dto.setPassword(null);
        violations = validator.validate(dto);
        assertFalse(violations.isEmpty());

        dto.setPassword("senhaValida");
        violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }
}