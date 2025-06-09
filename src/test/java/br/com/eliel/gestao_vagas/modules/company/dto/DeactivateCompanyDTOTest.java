package br.com.eliel.gestao_vagas.modules.company.dto;

import br.com.eliel.gestao_vagas.modules.company.dto.DeactivateCompanyDTO;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import static org.junit.jupiter.api.Assertions.*;

class DeactivateCompanyDTOTest {

    private Validator validator;
    private DeactivateCompanyDTO deactivateCompanyDTO;

    private static final String PASSWORD = "senha123456";

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        deactivateCompanyDTO = new DeactivateCompanyDTO();
        deactivateCompanyDTO.setPassword(PASSWORD);
    }

    @Test
    void shouldCreateValidDeactivateCompanyDTO() {
        Set<ConstraintViolation<DeactivateCompanyDTO>> violations = validator.validate(deactivateCompanyDTO);
        assertTrue(violations.isEmpty(), "Não deve haver violações de validação");
    }

    @Test
    void shouldNotAllowEmptyPassword() {
        deactivateCompanyDTO.setPassword("");
        Set<ConstraintViolation<DeactivateCompanyDTO>> violations = validator.validate(deactivateCompanyDTO);
        
        assertFalse(violations.isEmpty(), "Deve haver violações de validação");
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getPropertyPath().toString().equals("password") &&
                violation.getMessage().equals("A senha é obrigatória")));
    }

    @Test
    void shouldNotAllowNullPassword() {
        deactivateCompanyDTO.setPassword(null);
        Set<ConstraintViolation<DeactivateCompanyDTO>> violations = validator.validate(deactivateCompanyDTO);
        
        assertFalse(violations.isEmpty(), "Deve haver violações de validação");
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getPropertyPath().toString().equals("password") &&
                violation.getMessage().equals("A senha é obrigatória")));
    }

    @Test
    void shouldHaveCorrectToString() {
        String toString = deactivateCompanyDTO.toString();
        assertTrue(toString.contains("password=" + PASSWORD));
    }

    @Test
    void shouldHaveCorrectEqualsAndHashCode() {
        DeactivateCompanyDTO dto1 = new DeactivateCompanyDTO();
        dto1.setPassword(PASSWORD);

        DeactivateCompanyDTO dto2 = new DeactivateCompanyDTO();
        dto2.setPassword(PASSWORD);

        DeactivateCompanyDTO dto3 = new DeactivateCompanyDTO();
        dto3.setPassword("different");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1, dto3);
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }
}
