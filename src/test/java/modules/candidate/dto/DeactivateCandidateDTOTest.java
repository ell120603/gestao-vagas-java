package br.com.eliel.gestao_vagas.modules.candidate.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DeactivateCandidateDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testSetAndGetPassword() {
        DeactivateCandidateDTO dto = new DeactivateCandidateDTO();
        dto.setPassword("senha123");
        assertEquals("senha123", dto.getPassword());
    }

    @Test
    void testEqualsAndHashCode() {
        DeactivateCandidateDTO dto1 = new DeactivateCandidateDTO();
        dto1.setPassword("senha123");
        DeactivateCandidateDTO dto2 = new DeactivateCandidateDTO();
        dto2.setPassword("senha123");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DeactivateCandidateDTO dto = new DeactivateCandidateDTO();
        dto.setPassword("senha123");
        String str = dto.toString();
        assertTrue(str.contains("senha123"));
    }

    @Test
    void testPasswordNotBlankValidation() {
        DeactivateCandidateDTO dto = new DeactivateCandidateDTO();
        dto.setPassword(""); // Senha vazia para disparar a validação @NotBlank

        Set<ConstraintViolation<DeactivateCandidateDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Deve haver violações para senha em branco");
        assertEquals(1, violations.size(), "Deve haver apenas uma violação para senha em branco");

        ConstraintViolation<DeactivateCandidateDTO> violation = violations.iterator().next();
        assertEquals("password", violation.getPropertyPath().toString());
        assertEquals("A senha é obrigatória", violation.getMessage());

        dto.setPassword(null);
        violations = validator.validate(dto);
        assertFalse(violations.isEmpty(), "Deve haver violações para senha nula");
        assertEquals(1, violations.size(), "Deve haver apenas uma violação para senha nula");
        violation = violations.iterator().next();
        assertEquals("password", violation.getPropertyPath().toString());
        assertEquals("A senha é obrigatória", violation.getMessage());
    }

    @Test
    void testPasswordValid() {
        DeactivateCandidateDTO dto = new DeactivateCandidateDTO();
        dto.setPassword("minhasenhaforte"); // Senha válida

        Set<ConstraintViolation<DeactivateCandidateDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Não deve haver violações para senha válida");
    }
}