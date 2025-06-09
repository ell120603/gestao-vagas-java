package br.com.eliel.gestao_vagas.modules.candidate.entites;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CandidateEntityTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        CandidateEntity entity = new CandidateEntity();
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        entity.setId(id);
        entity.setName("João Silva");
        entity.setUsername("joaosilva");
        entity.setEmail("joao@email.com");
        entity.setPassword("senha123456");
        entity.setDescription("Desenvolvedor Java");
        entity.setCreatedAt(now);
        entity.setActive(false);

        assertEquals(id, entity.getId());
        assertEquals("João Silva", entity.getName());
        assertEquals("joaosilva", entity.getUsername());
        assertEquals("joao@email.com", entity.getEmail());
        assertEquals("senha123456", entity.getPassword());
        assertEquals("Desenvolvedor Java", entity.getDescription());
        assertEquals(now, entity.getCreatedAt());
        assertFalse(entity.isActive());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        CandidateEntity entity1 = new CandidateEntity();
        entity1.setId(id);
        entity1.setUsername("joaosilva");
        entity1.setEmail("joao@email.com"); 

        CandidateEntity entity2 = new CandidateEntity();
        entity2.setId(id);
        entity2.setUsername("joaosilva");
        entity2.setEmail("joao@email.com");
        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    void testToString() {
        CandidateEntity entity = new CandidateEntity();
        entity.setName("João Silva");
        entity.setUsername("joaosilva");
        entity.setEmail("joao@email.com");
        entity.setPassword("senha123456");
        entity.setDescription("Desenvolvedor Java");

        String str = entity.toString();
        assertTrue(str.contains("João Silva"));
        assertTrue(str.contains("joaosilva"));
        assertTrue(str.contains("joao@email.com"));
        assertTrue(str.contains("senha123456"));
        assertTrue(str.contains("Desenvolvedor Java"));
    }

    @Test
    void testUsernameNotBlankAndPatternValidation() {
        CandidateEntity entity = new CandidateEntity();
        entity.setPassword("ValidPassword123");
        entity.setEmail("valid@email.com");

        entity.setUsername("");
        Set<ConstraintViolation<CandidateEntity>> violations = validator.validate(entity);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("username")));

        entity.setUsername("user name");
        violations = validator.validate(entity);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("username")));
    }

    @Test
    void testEmailValidation() {
        CandidateEntity entity = new CandidateEntity();
        entity.setUsername("validusername");
        entity.setPassword("ValidPassword123");

        entity.setEmail("");
        Set<ConstraintViolation<CandidateEntity>> violations = validator.validate(entity);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));

        entity.setEmail("invalid-email");
        violations = validator.validate(entity);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void testPasswordLengthAndNotBlankValidation() {
        CandidateEntity entity = new CandidateEntity();
        entity.setUsername("validusername");
        entity.setEmail("valid@email.com");

        entity.setPassword("");
        Set<ConstraintViolation<CandidateEntity>> violations = validator.validate(entity);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));

        entity.setPassword("short");
        violations = validator.validate(entity);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));

        entity.setPassword("a".repeat(101));
        violations = validator.validate(entity);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void testDefaultActiveValue() {
        CandidateEntity entity = new CandidateEntity();
        assertTrue(entity.isActive());
    }

    @Test
    void testValidCandidateEntity() {
        CandidateEntity entity = new CandidateEntity();
        entity.setName("Valid Name");
        entity.setUsername("validuser");
        entity.setEmail("valid@example.com");
        entity.setPassword("verySecurePassword123");
        entity.setDescription("Valid description.");

        Set<ConstraintViolation<CandidateEntity>> violations = validator.validate(entity);
        assertTrue(violations.isEmpty());
    }
}