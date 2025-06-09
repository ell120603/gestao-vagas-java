package br.com.eliel.gestao_vagas.modules.company.entites;

import br.com.eliel.gestao_vagas.modules.company.entites.CompanyEntity;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import jakarta.validation.ConstraintViolation;
import static org.junit.jupiter.api.Assertions.*;

class CompanyEntityTest {

    private Validator validator;
    private CompanyEntity company;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        company = new CompanyEntity();
        company.setId(UUID.randomUUID());
        company.setName("Tech Solutions");
        company.setUsername("techsolutions");
        company.setEmail("contato@techsolutions.com");
        company.setPassword("senha123456");
        company.setWebsite("https://techsolutions.com");
        company.setDescription("Empresa de tecnologia");
        company.setCreatedAt(LocalDateTime.now());
        company.setActive(true);
    }

    @Test
    void shouldCreateValidCompany() {
        Set<ConstraintViolation<CompanyEntity>> violations = validator.validate(company);
        assertTrue(violations.isEmpty(), "Não deve haver violações de validação");
    }

    @Test
    void shouldNotAllowEmptyName() {
        company.setName("");
        Set<ConstraintViolation<CompanyEntity>> violations = validator.validate(company);
        
        assertFalse(violations.isEmpty(), "Deve haver violações de validação");
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getPropertyPath().toString().equals("name") &&
                violation.getMessage().equals("O nome da empresa é obrigatório")));
    }

    @Test
    void shouldNotAllowEmptyUsername() {
        company.setUsername("");
        Set<ConstraintViolation<CompanyEntity>> violations = validator.validate(company);
        
        assertFalse(violations.isEmpty(), "Deve haver violações de validação");
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getPropertyPath().toString().equals("username") &&
                violation.getMessage().equals("O username é obrigatório")));
    }

    @Test
    void shouldNotAllowUsernameWithSpaces() {
        company.setUsername("tech solutions");
        Set<ConstraintViolation<CompanyEntity>> violations = validator.validate(company);
        
        assertFalse(violations.isEmpty(), "Deve haver violações de validação");
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getPropertyPath().toString().equals("username") &&
                violation.getMessage().equals("O username não deve conter espaço")));
    }

    @Test
    void shouldNotAllowEmptyEmail() {
        company.setEmail("");
        Set<ConstraintViolation<CompanyEntity>> violations = validator.validate(company);
        
        assertFalse(violations.isEmpty(), "Deve haver violações de validação");
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getPropertyPath().toString().equals("email") &&
                violation.getMessage().equals("O email é obrigatório")));
    }

    @Test
    void shouldNotAllowInvalidEmail() {
        company.setEmail("invalid-email");
        Set<ConstraintViolation<CompanyEntity>> violations = validator.validate(company);
        
        assertFalse(violations.isEmpty(), "Deve haver violações de validação");
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getPropertyPath().toString().equals("email") &&
                violation.getMessage().equals("O email deve ser válido")));
    }

    @Test
    void shouldNotAllowEmptyPassword() {
        company.setPassword("");
        Set<ConstraintViolation<CompanyEntity>> violations = validator.validate(company);
        
        assertFalse(violations.isEmpty(), "Deve haver violações de validação");
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getPropertyPath().toString().equals("password") &&
                violation.getMessage().equals("A senha é obrigatória")));
    }

    @Test
    void shouldNotAllowShortPassword() {
        company.setPassword("123456");
        Set<ConstraintViolation<CompanyEntity>> violations = validator.validate(company);
        
        assertFalse(violations.isEmpty(), "Deve haver violações de validação");
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getPropertyPath().toString().equals("password") &&
                violation.getMessage().equals("A senha deve ter entre 10 e 100 caracteres")));
    }

    @Test
    void shouldNotAllowLongPassword() {
        company.setPassword("a".repeat(101));
        Set<ConstraintViolation<CompanyEntity>> violations = validator.validate(company);
        
        assertFalse(violations.isEmpty(), "Deve haver violações de validação");
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getPropertyPath().toString().equals("password") &&
                violation.getMessage().equals("A senha deve ter entre 10 e 100 caracteres")));
    }

    @Test
    void shouldAllowOptionalFieldsToBeNull() {
        company.setWebsite(null);
        company.setDescription(null);
        
        Set<ConstraintViolation<CompanyEntity>> violations = validator.validate(company);
        assertTrue(violations.isEmpty(), "Não deve haver violações de validação para campos opcionais");
    }

    @Test
    void shouldSetDefaultActiveValue() {
        CompanyEntity newCompany = new CompanyEntity();
        assertTrue(newCompany.isActive(), "O valor padrão de active deve ser true");
    }
}
