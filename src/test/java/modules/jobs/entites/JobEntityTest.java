package modules.jobs.entites;

import br.com.eliel.gestao_vagas.modules.company.entites.CompanyEntity;
import br.com.eliel.gestao_vagas.modules.jobs.entites.JobEntity;
import br.com.eliel.gestao_vagas.modules.jobs.entites.TipoContrato;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JobEntityTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void testBuilderAndGetters() {
        UUID id = UUID.randomUUID();
        CompanyEntity company = new CompanyEntity();
        company.setId(UUID.randomUUID());
        LocalDateTime now = LocalDateTime.now();

        JobEntity job = JobEntity.builder()
                .id(id)
                .titulo("Desenvolvedor")
                .descricao("Desenvolve sistemas")
                .areaAtuacao("TI")
                .requisitos(List.of("Java", "Spring"))
                .tipoContrato(TipoContrato.CLT)
                .localizacao("SP")
                .company(company)
                .salario(new BigDecimal("5000"))
                .beneficios("VR, VT")
                .createdAt(now)
                .active(true)
                .build();

        assertEquals(id, job.getId());
        assertEquals("Desenvolvedor", job.getTitulo());
        assertEquals("Desenvolve sistemas", job.getDescricao());
        assertEquals("TI", job.getAreaAtuacao());
        assertEquals(List.of("Java", "Spring"), job.getRequisitos());
        assertEquals(TipoContrato.CLT, job.getTipoContrato());
        assertEquals("SP", job.getLocalizacao());
        assertEquals(company, job.getCompany());
        assertEquals(new BigDecimal("5000"), job.getSalario());
        assertEquals("VR, VT", job.getBeneficios());
        assertEquals(now, job.getCreatedAt());
        assertTrue(job.isActive());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        JobEntity job = new JobEntity();
        UUID id = UUID.randomUUID();
        CompanyEntity company = new CompanyEntity();
        company.setId(UUID.randomUUID());
        LocalDateTime now = LocalDateTime.now();

        job.setId(id);
        job.setTitulo("Analista");
        job.setDescricao("Analisa sistemas");
        job.setAreaAtuacao("RH");
        job.setRequisitos(List.of("Excel"));
        job.setTipoContrato(TipoContrato.PJ);
        job.setLocalizacao("RJ");
        job.setCompany(company);
        job.setSalario(new BigDecimal("7000"));
        job.setBeneficios("Plano de Saúde");
        job.setCreatedAt(now);
        job.setActive(false);

        assertEquals(id, job.getId());
        assertEquals("Analista", job.getTitulo());
        assertEquals("Analisa sistemas", job.getDescricao());
        assertEquals("RH", job.getAreaAtuacao());
        assertEquals(List.of("Excel"), job.getRequisitos());
        assertEquals(TipoContrato.PJ, job.getTipoContrato());
        assertEquals("RJ", job.getLocalizacao());
        assertEquals(company, job.getCompany());
        assertEquals(new BigDecimal("7000"), job.getSalario());
        assertEquals("Plano de Saúde", job.getBeneficios());
        assertEquals(now, job.getCreatedAt());
        assertFalse(job.isActive());
    }

    @Test
    void testAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        CompanyEntity company = new CompanyEntity();
        company.setId(UUID.randomUUID());
        LocalDateTime now = LocalDateTime.now();

        JobEntity job = new JobEntity(
                id,
                "QA",
                "Testa sistemas",
                "Qualidade",
                List.of("Testes"),
                TipoContrato.CLT,
                "MG",
                company,
                new BigDecimal("4000"),
                "VR",
                now,
                true
        );

        assertEquals(id, job.getId());
        assertEquals("QA", job.getTitulo());
        assertEquals("Testa sistemas", job.getDescricao());
        assertEquals("Qualidade", job.getAreaAtuacao());
        assertEquals(List.of("Testes"), job.getRequisitos());
        assertEquals(TipoContrato.CLT, job.getTipoContrato());
        assertEquals("MG", job.getLocalizacao());
        assertEquals(company, job.getCompany());
        assertEquals(new BigDecimal("4000"), job.getSalario());
        assertEquals("VR", job.getBeneficios());
        assertEquals(now, job.getCreatedAt());
        assertTrue(job.isActive());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        JobEntity job1 = JobEntity.builder().id(id).titulo("Dev").build();
        JobEntity job2 = JobEntity.builder().id(id).titulo("Dev").build();

        assertEquals(job1, job2);
        assertEquals(job1.hashCode(), job2.hashCode());
    }

    @Test
    void testToString() {
        JobEntity job = JobEntity.builder()
                .titulo("Dev")
                .descricao("Desc")
                .areaAtuacao("TI")
                .localizacao("SP")
                .build();

        String str = job.toString();
        assertTrue(str.contains("Dev"));
        assertTrue(str.contains("Desc"));
        assertTrue(str.contains("TI"));
        assertTrue(str.contains("SP"));
    }

    @Test
    void shouldValidateRequiredFields() {
        JobEntity job = new JobEntity();
        Set<ConstraintViolation<JobEntity>> violations = validator.validate(job);
        assertThat(violations).hasSize(5);
    }

    @Test
    void shouldHaveActiveTrueByDefault() {
        JobEntity job = JobEntity.builder().build();
        assertTrue(job.isActive());
    }

    @Test
    void shouldValidateCompanyIsRequired() {
        JobEntity job = JobEntity.builder().build();
        Set<ConstraintViolation<JobEntity>> violations = validator.validate(job);
        assertThat(violations).extracting("message").contains("não deve ser nulo");
    }
}