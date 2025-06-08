package modules.jobs.useCases;

import br.com.eliel.gestao_vagas.modules.company.entites.CompanyEntity;
import br.com.eliel.gestao_vagas.modules.jobs.dto.JobFilterDTO;
import br.com.eliel.gestao_vagas.modules.jobs.dto.JobsResponseDTO;
import br.com.eliel.gestao_vagas.modules.jobs.entites.JobEntity;
import br.com.eliel.gestao_vagas.modules.jobs.entites.TipoContrato;
import br.com.eliel.gestao_vagas.modules.jobs.repositories.JobsRepository;
import br.com.eliel.gestao_vagas.modules.jobs.useCases.FilterJobsUseCase;
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
class FilterJobsUseCaseTest {

    @InjectMocks
    private FilterJobsUseCase filterJobsUseCase;

    @Mock
    private JobsRepository jobsRepository;

    private JobEntity javaJob;
    private JobEntity rhJob;
    private CompanyEntity company;

    @BeforeEach
    void setUp() {
        company = new CompanyEntity();
        company.setId(UUID.randomUUID());
        company.setName("Empresa X");

        javaJob = JobEntity.builder()
                .id(UUID.randomUUID())
                .titulo("Desenvolvedor Java")
                .descricao("Desenvolve sistemas")
                .areaAtuacao("TI")
                .requisitos(List.of("Java", "Spring"))
                .tipoContrato(TipoContrato.CLT)
                .localizacao("São Paulo")
                .company(company)
                .salario(new BigDecimal("5000"))
                .beneficios("VR, VT, Plano de Saúde")
                .createdAt(LocalDateTime.now())
                .active(true)
                .build();

        rhJob = JobEntity.builder()
                .id(UUID.randomUUID())
                .titulo("Analista RH")
                .descricao("Recrutamento e seleção")
                .areaAtuacao("Recursos Humanos")
                .requisitos(List.of("Excel", "Dinamismo"))
                .tipoContrato(TipoContrato.PJ)
                .localizacao("Rio de Janeiro")
                .company(company)
                .salario(new BigDecimal("3500"))
                .beneficios("Plano Odontológico")
                .createdAt(LocalDateTime.now())
                .active(true)
                .build();
    }

    @Test
    void shouldReturnAllActiveJobsWhenFilterIsNull() {
        when(jobsRepository.findByActiveTrue()).thenReturn(List.of(javaJob, rhJob));

        List<JobsResponseDTO> result = filterJobsUseCase.execute(null);

        assertEquals(2, result.size());
    }

    @Test
    void shouldFilterByAreaAtuacao() {
        when(jobsRepository.findByActiveTrue()).thenReturn(List.of(javaJob, rhJob));
        JobFilterDTO filter = JobFilterDTO.builder().areaAtuacao("TI").build();

        List<JobsResponseDTO> result = filterJobsUseCase.execute(filter);

        assertEquals(1, result.size());
        assertEquals("Desenvolvedor Java", result.get(0).getTitulo());
    }

    @Test
    void shouldFilterByRequisitos() {
        when(jobsRepository.findByActiveTrue()).thenReturn(List.of(javaJob, rhJob));
        JobFilterDTO filter = JobFilterDTO.builder().requisitos(List.of("Spring")).build();

        List<JobsResponseDTO> result = filterJobsUseCase.execute(filter);

        assertEquals(1, result.size());
        assertEquals("Desenvolvedor Java", result.get(0).getTitulo());
    }

    @Test
    void shouldFilterByTipoContrato() {
        when(jobsRepository.findByActiveTrue()).thenReturn(List.of(javaJob, rhJob));
        JobFilterDTO filter = JobFilterDTO.builder().tipoContrato(TipoContrato.PJ).build();

        List<JobsResponseDTO> result = filterJobsUseCase.execute(filter);

        assertEquals(1, result.size());
        assertEquals("Analista RH", result.get(0).getTitulo());
    }

    @Test
    void shouldFilterByLocalizacao() {
        when(jobsRepository.findByActiveTrue()).thenReturn(List.of(javaJob, rhJob));
        JobFilterDTO filter = JobFilterDTO.builder().localizacao("Rio").build();

        List<JobsResponseDTO> result = filterJobsUseCase.execute(filter);

        assertEquals(1, result.size());
        assertEquals("Analista RH", result.get(0).getTitulo());
    }

    @Test
    void shouldFilterByBeneficios() {
        when(jobsRepository.findByActiveTrue()).thenReturn(List.of(javaJob, rhJob));
        JobFilterDTO filter = JobFilterDTO.builder().beneficios(List.of("VT")).build();

        List<JobsResponseDTO> result = filterJobsUseCase.execute(filter);

        assertEquals(1, result.size());
        assertEquals("Desenvolvedor Java", result.get(0).getTitulo());
    }

    @Test
    void shouldFilterBySalarioRange() {
        when(jobsRepository.findByActiveTrue()).thenReturn(List.of(javaJob, rhJob));
        JobFilterDTO filter = JobFilterDTO.builder()
                .salarioMinimo(new BigDecimal("4000"))
                .salarioMaximo(new BigDecimal("6000"))
                .build();

        List<JobsResponseDTO> result = filterJobsUseCase.execute(filter);

        assertEquals(1, result.size());
        assertEquals("Desenvolvedor Java", result.get(0).getTitulo());
    }

    @Test
    void shouldReturnEmptyWhenNoMatches() {
        when(jobsRepository.findByActiveTrue()).thenReturn(List.of(javaJob, rhJob));
        JobFilterDTO filter = JobFilterDTO.builder()
                .areaAtuacao("Marketing")
                .build();

        List<JobsResponseDTO> result = filterJobsUseCase.execute(filter);

        assertTrue(result.isEmpty());
    }
}