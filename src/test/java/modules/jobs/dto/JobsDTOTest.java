package br.com.eliel.gestao_vagas.modules.jobs.dto;

import br.com.eliel.gestao_vagas.modules.jobs.entites.TipoContrato;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JobsDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    

    @Test
    void testValidations() {
        JobsDTO dto = new JobsDTO();
        
        Set<ConstraintViolation<JobsDTO>> violations = validator.validate(dto);
        assertEquals(5, violations.size()); // 5 campos obrigatórios
        
        // Verifica mensagens de erro
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("O título é obrigatório")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("A descrição é obrigatória")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("A área de atuação é obrigatória")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("O tipo de contrato é obrigatório")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("A localização é obrigatória")));
    }

    @Test
    void testValidObject() {
        JobsDTO dto = JobsDTO.builder()
                .titulo("Dev")
                .descricao("Desc")
                .areaAtuacao("TI")
                .tipoContrato(TipoContrato.CLT)
                .localizacao("SP")
                .build();
                
        Set<ConstraintViolation<JobsDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }
}