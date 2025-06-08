package modules.jobs.useCases;

import br.com.eliel.gestao_vagas.modules.company.entites.CompanyEntity;
import br.com.eliel.gestao_vagas.modules.jobs.dto.JobsResponseDTO;
import br.com.eliel.gestao_vagas.modules.jobs.entites.JobEntity;
import br.com.eliel.gestao_vagas.modules.jobs.entites.TipoContrato;
import br.com.eliel.gestao_vagas.modules.jobs.repositories.JobsRepository;
import br.com.eliel.gestao_vagas.modules.jobs.useCases.ListAllJobsUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListAllJobsUseCaseTest {

    @InjectMocks
    private ListAllJobsUseCase listAllJobsUseCase;

    @Mock
    private JobsRepository jobsRepository;

    private JobEntity activeJob1;
    private JobEntity activeJob2;

    @BeforeEach
    void setUp() {
        CompanyEntity company = new CompanyEntity();
        company.setId(UUID.randomUUID());
        company.setName("Tech Solutions");

        activeJob1 = JobEntity.builder()
                .id(UUID.randomUUID())
                .titulo("Desenvolvedor Backend")
                .descricao("Desenvolvimento de APIs")
                .areaAtuacao("TI")
                .requisitos(List.of("Java", "Spring Boot"))
                .tipoContrato(TipoContrato.CLT)
                .localizacao("Remoto")
                .company(company)
                .salario(new BigDecimal("8000"))
                .beneficios("VR, VT, Plano de Saúde")
                .createdAt(LocalDateTime.now())
                .active(true)
                .build();

        activeJob2 = JobEntity.builder()
                .id(UUID.randomUUID())
                .titulo("Analista de Dados")
                .descricao("Análise de dados corporativos")
                .areaAtuacao("TI")
                .requisitos(List.of("Python", "SQL"))
                .tipoContrato(TipoContrato.PJ)
                .localizacao("Híbrido")
                .company(company)
                .salario(new BigDecimal("6000"))
                .beneficios("Plano Odontológico")
                .createdAt(LocalDateTime.now())
                .active(true)
                .build();
    }

    @Test
    void shouldReturnListOfActiveJobs() {
        when(jobsRepository.findByActiveTrue()).thenReturn(List.of(activeJob1, activeJob2));

        List<JobsResponseDTO> result = listAllJobsUseCase.execute();

        assertEquals(2, result.size());
        assertJobDTOMatchesEntity(activeJob1, result.get(0));
        assertJobDTOMatchesEntity(activeJob2, result.get(1));
    }

    @Test
    void shouldReturnEmptyListWhenNoActiveJobs() {
        when(jobsRepository.findByActiveTrue()).thenReturn(List.of());

        List<JobsResponseDTO> result = listAllJobsUseCase.execute();

        assertTrue(result.isEmpty());
    }

    private void assertJobDTOMatchesEntity(JobEntity job, JobsResponseDTO dto) {
        assertEquals(job.getId(), dto.getId());
        assertEquals(job.getTitulo(), dto.getTitulo());
        assertEquals(job.getDescricao(), dto.getDescricao());
        assertEquals(job.getAreaAtuacao(), dto.getAreaAtuacao());
        assertEquals(job.getRequisitos(), dto.getRequisitos());
        assertEquals(job.getTipoContrato(), dto.getTipoContrato());
        assertEquals(job.getLocalizacao(), dto.getLocalizacao());
        assertEquals(job.getCompany().getName(), dto.getCompanyName());
        assertEquals(job.getSalario(), dto.getSalario());
        assertEquals(job.getBeneficios(), dto.getBeneficios());
        assertEquals(job.getCreatedAt(), dto.getCreatedAt());
    }
}