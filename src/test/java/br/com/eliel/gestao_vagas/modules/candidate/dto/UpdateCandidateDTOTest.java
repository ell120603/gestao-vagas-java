package br.com.eliel.gestao_vagas.modules.candidate.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import static org.junit.jupiter.api.Assertions.*;

class UpdateCandidateDTOTest {

    private Validator validator;
    private UpdateCandidateDTO updateCandidateDTO;

    private static final String NAME = "João Silva";
    private static final String USERNAME = "joaosilva";
    private static final String EMAIL = "joao@email.com";
    private static final String DESCRIPTION = "Desenvolvedor Java";

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        updateCandidateDTO = new UpdateCandidateDTO();
        updateCandidateDTO.setName(NAME);
        updateCandidateDTO.setUsername(USERNAME);
        updateCandidateDTO.setEmail(EMAIL);
        updateCandidateDTO.setDescription(DESCRIPTION);
    }

    @Test
    void shouldCreateValidUpdateCandidateDTO() {
        Set<ConstraintViolation<UpdateCandidateDTO>> violations = validator.validate(updateCandidateDTO);
        assertTrue(violations.isEmpty(), "Não deve haver violações de validação");
    }

    @Test
    void shouldNotAllowUsernameWithSpaces() {
        updateCandidateDTO.setUsername("joao silva");
        Set<ConstraintViolation<UpdateCandidateDTO>> violations = validator.validate(updateCandidateDTO);
        
        assertFalse(violations.isEmpty(), "Deve haver violações de validação");
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getPropertyPath().toString().equals("username") &&
                violation.getMessage().equals("O username não deve conter espaço")));
    }

    @Test
    void shouldNotAllowInvalidEmail() {
        updateCandidateDTO.setEmail("email_invalido");
        Set<ConstraintViolation<UpdateCandidateDTO>> violations = validator.validate(updateCandidateDTO);
        
        assertFalse(violations.isEmpty(), "Deve haver violações de validação");
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getPropertyPath().toString().equals("email") &&
                violation.getMessage().equals("O email deve ser válido")));
    }

    @Test
    void shouldCreateUpdateCandidateDTOWithBuilder() {
        UpdateCandidateDTO dto = UpdateCandidateDTO.builder()
            .name(NAME)
            .username(USERNAME)
            .email(EMAIL)
            .description(DESCRIPTION)
            .build();

        assertNotNull(dto);
        assertEquals(NAME, dto.getName());
        assertEquals(USERNAME, dto.getUsername());
        assertEquals(EMAIL, dto.getEmail());
        assertEquals(DESCRIPTION, dto.getDescription());
    }

    @Test
    void shouldCreateUpdateCandidateDTOWithAllArgsConstructor() {
        UpdateCandidateDTO dto = new UpdateCandidateDTO(NAME, USERNAME, EMAIL, DESCRIPTION);

        assertNotNull(dto);
        assertEquals(NAME, dto.getName());
        assertEquals(USERNAME, dto.getUsername());
        assertEquals(EMAIL, dto.getEmail());
        assertEquals(DESCRIPTION, dto.getDescription());
    }

    @Test
    void shouldHaveCorrectToString() {
        UpdateCandidateDTO dto = UpdateCandidateDTO.builder()
            .name(NAME)
            .username(USERNAME)
            .email(EMAIL)
            .description(DESCRIPTION)
            .build();

        String toString = dto.toString();

        assertTrue(toString.contains("name=" + NAME));
        assertTrue(toString.contains("username=" + USERNAME));
        assertTrue(toString.contains("email=" + EMAIL));
        assertTrue(toString.contains("description=" + DESCRIPTION));
    }

    @Test
    void shouldHaveCorrectEqualsAndHashCode() {
        UpdateCandidateDTO dto1 = UpdateCandidateDTO.builder()
            .name(NAME)
            .username(USERNAME)
            .email(EMAIL)
            .description(DESCRIPTION)
            .build();

        UpdateCandidateDTO dto2 = UpdateCandidateDTO.builder()
            .name(NAME)
            .username(USERNAME)
            .email(EMAIL)
            .description(DESCRIPTION)
            .build();

        UpdateCandidateDTO dto3 = UpdateCandidateDTO.builder()
            .name("Maria Silva")
            .username("mariasilva")
            .email("maria@email.com")
            .description("Desenvolvedora Java")
            .build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1, dto3);
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }
}
