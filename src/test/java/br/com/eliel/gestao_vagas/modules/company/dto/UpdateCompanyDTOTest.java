package br.com.eliel.gestao_vagas.modules.company.dto;

import br.com.eliel.gestao_vagas.modules.company.dto.UpdateCompanyDTO;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import static org.junit.jupiter.api.Assertions.*;

class UpdateCompanyDTOTest {

    private Validator validator;
    private UpdateCompanyDTO updateCompanyDTO;

    private static final String NAME = "Tech Solutions";
    private static final String USERNAME = "techsolutions";
    private static final String EMAIL = "contato@techsolutions.com";
    private static final String WEBSITE = "https://techsolutions.com";
    private static final String DESCRIPTION = "Empresa de tecnologia focada em soluções inovadoras";

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        updateCompanyDTO = UpdateCompanyDTO.builder()
            .name(NAME)
            .username(USERNAME)
            .email(EMAIL)
            .website(WEBSITE)
            .description(DESCRIPTION)
            .build();
    }

    @Test
    void shouldCreateValidUpdateCompanyDTO() {
        Set<ConstraintViolation<UpdateCompanyDTO>> violations = validator.validate(updateCompanyDTO);
        assertTrue(violations.isEmpty(), "Não deve haver violações de validação");
    }

    @Test
    void shouldNotAllowUsernameWithSpaces() {
        updateCompanyDTO.setUsername("tech solutions");
        Set<ConstraintViolation<UpdateCompanyDTO>> violations = validator.validate(updateCompanyDTO);
        
        assertFalse(violations.isEmpty(), "Deve haver violações de validação");
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getPropertyPath().toString().equals("username") &&
                violation.getMessage().equals("O username não deve conter espaço")));
    }

    @Test
    void shouldNotAllowInvalidEmail() {
        updateCompanyDTO.setEmail("invalid-email");
        Set<ConstraintViolation<UpdateCompanyDTO>> violations = validator.validate(updateCompanyDTO);
        
        assertFalse(violations.isEmpty(), "Deve haver violações de validação");
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getPropertyPath().toString().equals("email") &&
                violation.getMessage().equals("O email deve ser válido")));
    }

    @Test
    void shouldCreateUpdateCompanyDTOWithBuilder() {
        UpdateCompanyDTO dto = UpdateCompanyDTO.builder()
            .name(NAME)
            .username(USERNAME)
            .email(EMAIL)
            .website(WEBSITE)
            .description(DESCRIPTION)
            .build();

        assertNotNull(dto);
        assertEquals(NAME, dto.getName());
        assertEquals(USERNAME, dto.getUsername());
        assertEquals(EMAIL, dto.getEmail());
        assertEquals(WEBSITE, dto.getWebsite());
        assertEquals(DESCRIPTION, dto.getDescription());
    }

    @Test
    void shouldCreateUpdateCompanyDTOWithAllArgsConstructor() {
        UpdateCompanyDTO dto = new UpdateCompanyDTO(NAME, USERNAME, EMAIL, WEBSITE, DESCRIPTION);

        assertNotNull(dto);
        assertEquals(NAME, dto.getName());
        assertEquals(USERNAME, dto.getUsername());
        assertEquals(EMAIL, dto.getEmail());
        assertEquals(WEBSITE, dto.getWebsite());
        assertEquals(DESCRIPTION, dto.getDescription());
    }

    @Test
    void shouldCreateUpdateCompanyDTOWithNoArgsConstructor() {
        UpdateCompanyDTO dto = new UpdateCompanyDTO();
        dto.setName(NAME);
        dto.setUsername(USERNAME);
        dto.setEmail(EMAIL);
        dto.setWebsite(WEBSITE);
        dto.setDescription(DESCRIPTION);

        assertNotNull(dto);
        assertEquals(NAME, dto.getName());
        assertEquals(USERNAME, dto.getUsername());
        assertEquals(EMAIL, dto.getEmail());
        assertEquals(WEBSITE, dto.getWebsite());
        assertEquals(DESCRIPTION, dto.getDescription());
    }

    @Test
    void shouldHaveCorrectToString() {
        String toString = updateCompanyDTO.toString();

        assertTrue(toString.contains("name=" + NAME));
        assertTrue(toString.contains("username=" + USERNAME));
        assertTrue(toString.contains("email=" + EMAIL));
        assertTrue(toString.contains("website=" + WEBSITE));
        assertTrue(toString.contains("description=" + DESCRIPTION));
    }

    @Test
    void shouldHaveCorrectEqualsAndHashCode() {
        UpdateCompanyDTO dto1 = UpdateCompanyDTO.builder()
            .name(NAME)
            .username(USERNAME)
            .email(EMAIL)
            .website(WEBSITE)
            .description(DESCRIPTION)
            .build();

        UpdateCompanyDTO dto2 = UpdateCompanyDTO.builder()
            .name(NAME)
            .username(USERNAME)
            .email(EMAIL)
            .website(WEBSITE)
            .description(DESCRIPTION)
            .build();

        UpdateCompanyDTO dto3 = UpdateCompanyDTO.builder()
            .name("Different")
            .username("different")
            .email("different@email.com")
            .website("https://different.com")
            .description("Different description")
            .build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1, dto3);
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    void shouldHandleNullValues() {
        UpdateCompanyDTO dto = new UpdateCompanyDTO(null, null, null, null, null);

        assertNull(dto.getName());
        assertNull(dto.getUsername());
        assertNull(dto.getEmail());
        assertNull(dto.getWebsite());
        assertNull(dto.getDescription());
    }
}
