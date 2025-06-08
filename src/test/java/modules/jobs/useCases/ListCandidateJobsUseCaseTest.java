package modules.jobs.useCases;

import br.com.eliel.gestao_vagas.modules.company.entites.CompanyEntity;
import br.com.eliel.gestao_vagas.modules.jobs.dto.JobsResponseDTO;
import br.com.eliel.gestao_vagas.modules.jobs.entites.CandidateJobEntity;
import br.com.eliel.gestao_vagas.modules.jobs.entites.JobEntity;
import br.com.eliel.gestao_vagas.modules.jobs.entites.TipoContrato;
import br.com.eliel.gestao_vagas.modules.jobs.repositories.CandidateJobRepository;
import br.com.eliel.gestao_vagas.modules.jobs.repositories.JobsRepository;
import br.com.eliel.gestao_vagas.modules.jobs.useCases.ListCandidateJobsUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListCandidateJobsUseCaseTest {

    @InjectMocks
    private ListCandidateJobsUseCase listCandidateJobsUseCase;

    @Mock
    private CandidateJobRepository candidateJobRepository;

    @Mock
    private JobsRepository jobsRepository;

    private UUID candidateId;
    private UUID jobId;
    private CompanyEntity company;
    private JobEntity jobEntity;
    private CandidateJobEntity candidateJobEntity;

    @BeforeEach
    void setUp() {
        candidateId = UUID.randomUUID();
        jobId = UUID.randomUUID();
        company = new CompanyEntity();
        company.setId(UUID.randomUUID());
        company.setName("Empresa X");
        jobEntity = JobEntity.builder()
                .id(jobId)
                .titulo("Desenvolvedor")
                .descricao("Desenvolve sistemas")
                .areaAtuacao("TI")
                .requisitos(List.of("Java", "Spring"))
                .tipoContrato(TipoContrato.CLT)
                .localizacao("SP")
                .company(company)
                .salario(new BigDecimal("5000"))
                .beneficios("VR, VT")
                .createdAt(LocalDateTime.now())
                .active(true)
                .build();
        candidateJobEntity = CandidateJobEntity.builder()
                .id(UUID.randomUUID())
                .candidateId(candidateId)
                .jobId(jobId)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testExecuteReturnsJobsResponseDTOList() {
        when(candidateJobRepository.findByCandidateId(candidateId)).thenReturn(List.of(candidateJobEntity));
        when(jobsRepository.findById(jobId)).thenReturn(Optional.of(jobEntity));

        List<JobsResponseDTO> result = listCandidateJobsUseCase.execute(candidateId);

        assertEquals(1, result.size());
        JobsResponseDTO dto = result.get(0);
        assertEquals(jobEntity.getId(), dto.getId());
        assertEquals(jobEntity.getTitulo(), dto.getTitulo());
        assertEquals(jobEntity.getDescricao(), dto.getDescricao());
        assertEquals(jobEntity.getAreaAtuacao(), dto.getAreaAtuacao());
        assertEquals(jobEntity.getRequisitos(), dto.getRequisitos());
        assertEquals(jobEntity.getTipoContrato(), dto.getTipoContrato());
        assertEquals(jobEntity.getLocalizacao(), dto.getLocalizacao());
        assertEquals(jobEntity.getCompany().getName(), dto.getCompanyName());
        assertEquals(jobEntity.getSalario(), dto.getSalario());
        assertEquals(jobEntity.getBeneficios(), dto.getBeneficios());
        verify(candidateJobRepository).findByCandidateId(candidateId);
        verify(jobsRepository).findById(jobId);
    }

    @Test
    void testExecuteJobNotFoundThrowsException() {
        when(candidateJobRepository.findByCandidateId(candidateId)).thenReturn(List.of(candidateJobEntity));
        when(jobsRepository.findById(jobId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                listCandidateJobsUseCase.execute(candidateId));
        assertEquals("Vaga não encontrada", ex.getMessage());
        verify(candidateJobRepository).findByCandidateId(candidateId);
        verify(jobsRepository).findById(jobId);
    }

    @Test
    void testExecuteWithNoJobsReturnsEmptyList() {
        when(candidateJobRepository.findByCandidateId(candidateId)).thenReturn(Collections.emptyList());

        List<JobsResponseDTO> result = listCandidateJobsUseCase.execute(candidateId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(candidateJobRepository).findByCandidateId(candidateId);
        verify(jobsRepository, never()).findById(any());
    }
}
