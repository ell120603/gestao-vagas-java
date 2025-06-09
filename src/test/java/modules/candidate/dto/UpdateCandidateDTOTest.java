package br.com.eliel.gestao_vagas.modules.candidate.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UpdateCandidateDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        UpdateCandidateDTO dto = new UpdateCandidateDTO();
        dto.setName("João Silva");
        dto.setUsername("joaosilva");
        dto.setEmail("joao@email.com");
        dto.setDescription("Desenvolvedor Java");

        assertEquals("João Silva", dto.getName());
        assertEquals("joaosilva", dto.getUsername());
        assertEquals("joao@email.com", dto.getEmail());
        assertEquals("Desenvolvedor Java", dto.getDescription());
    }

    @Test
    void testAllArgsConstructor() {
        UpdateCandidateDTO dto = new UpdateCandidateDTO(
                "João Silva", "joaosilva", "joao@email.com", "Desenvolvedor Java"
        );
        assertEquals("João Silva", dto.getName());
        assertEquals("joaosilva", dto.getUsername());
        assertEquals("joao@email.com", dto.getEmail());
        assertEquals("Desenvolvedor Java", dto.getDescription());
    }

    @Test
    void testBuilder() {
        UpdateCandidateDTO dto = UpdateCandidateDTO.builder()
                .name("João Silva")
                .username("joaosilva")
                .email("joao@email.com")
                .description("Desenvolvedor Java")
                .build();

        assertEquals("João Silva", dto.getName());
        assertEquals("joaosilva", dto.getUsername());
        assertEquals("joao@email.com", dto.getEmail());
        assertEquals("Desenvolvedor Java", dto.getDescription());
    }

    @Test
    void testEqualsAndHashCode() {
        UpdateCandidateDTO dto1 = new UpdateCandidateDTO(
                "João Silva", "joaosilva", "joao@email.com", "Desenvolvedor Java"
        );
        UpdateCandidateDTO dto2 = new UpdateCandidateDTO(
                "João Silva", "joaosilva", "joao@email.com", "Desenvolvedor Java"
        );
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        UpdateCandidateDTO dto = new UpdateCandidateDTO(
                "João Silva", "joaosilva", "joao@email.com", "Desenvolvedor Java"
        );
        String str = dto.toString();
        assertTrue(str.contains("João Silva"));
        assertTrue(str.contains("joaosilva"));
        assertTrue(str.contains("joao@email.com"));
        assertTrue(str.contains("Desenvolvedor Java"));
    }


    @Test
    void testUsernamePatternValidationWithSpace() {
        UpdateCandidateDTO dto = new UpdateCandidateDTO();
        dto.setUsername("joao silva");
        dto.setEmail("joao@email.com"); 

        Set<ConstraintViolation<UpdateCandidateDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Deve haver violações para username com espaço");
        assertEquals(1, violations.size(), "Deve haver apenas uma violação para username com espaço");

        ConstraintViolation<UpdateCandidateDTO> violation = violations.iterator().next();
        assertEquals("username", violation.getPropertyPath().toString());
        assertEquals("O username não deve conter espaço", violation.getMessage());
    }

    @Test
    void testEmailValidationWithInvalidEmail() {
        UpdateCandidateDTO dto = new UpdateCandidateDTO();
        dto.setUsername("joaosilva"); 
        dto.setEmail("email_invalido"); 

        Set<ConstraintViolation<UpdateCandidateDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Deve haver violações para email inválido");
        assertEquals(1, violations.size(), "Deve haver apenas uma violação para email inválido");

        ConstraintViolation<UpdateCandidateDTO> violation = violations.iterator().next();
        assertEquals("email", violation.getPropertyPath().toString());
        assertEquals("O email deve ser válido", violation.getMessage());
    }

    @Test
    void testValidUpdateCandidateDTO() {
        UpdateCandidateDTO dto = new UpdateCandidateDTO(
                "Maria", "mariasilva", "maria@email.com", "Desenvolvedora Frontend"
        );

        Set<ConstraintViolation<UpdateCandidateDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Não deve haver violações para um DTO válido");
    }
}