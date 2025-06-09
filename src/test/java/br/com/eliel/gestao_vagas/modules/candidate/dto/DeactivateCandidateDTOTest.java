package br.com.eliel.gestao_vagas.modules.candidate.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import static org.junit.jupiter.api.Assertions.*;

class DeactivateCandidateDTOTest {

    private Validator validator;
    private DeactivateCandidateDTO deactivateCandidateDTO;

    private static final String PASSWORD = "senha123456";

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        deactivateCandidateDTO = new DeactivateCandidateDTO();
        deactivateCandidateDTO.setPassword(PASSWORD);
    }

    @Test
    void shouldCreateValidDeactivateCandidateDTO() {
        Set<ConstraintViolation<DeactivateCandidateDTO>> violations = validator.validate(deactivateCandidateDTO);
        assertTrue(violations.isEmpty(), "Não deve haver violações de validação");
    }

    @Test
    void shouldNotAllowEmptyPassword() {
        deactivateCandidateDTO.setPassword("");
        Set<ConstraintViolation<DeactivateCandidateDTO>> violations = validator.validate(deactivateCandidateDTO);
        
        assertFalse(violations.isEmpty(), "Deve haver violações de validação");
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getPropertyPath().toString().equals("password") &&
                violation.getMessage().equals("A senha é obrigatória")));
    }

    @Test
    void shouldNotAllowNullPassword() {
        deactivateCandidateDTO.setPassword(null);
        Set<ConstraintViolation<DeactivateCandidateDTO>> violations = validator.validate(deactivateCandidateDTO);
        
        assertFalse(violations.isEmpty(), "Deve haver violações de validação");
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getPropertyPath().toString().equals("password") &&
                violation.getMessage().equals("A senha é obrigatória")));
    }

    @Test
    void shouldGetAndSetPassword() {
        String newPassword = "novasenha123456";
        deactivateCandidateDTO.setPassword(newPassword);
        assertEquals(newPassword, deactivateCandidateDTO.getPassword());
    }
}
