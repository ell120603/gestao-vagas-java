package modules.jobs.useCases;

import br.com.eliel.gestao_vagas.exceptions.AuthenticationException;
import br.com.eliel.gestao_vagas.modules.candidate.entites.CandidateEntity;
import br.com.eliel.gestao_vagas.modules.candidate.repositories.CandidateRepository;
import br.com.eliel.gestao_vagas.modules.company.entites.CompanyEntity;
import br.com.eliel.gestao_vagas.modules.jobs.dto.CandidateJobResponseDTO;
import br.com.eliel.gestao_vagas.modules.jobs.entites.CandidateJobEntity;
import br.com.eliel.gestao_vagas.modules.jobs.entites.JobEntity;
import br.com.eliel.gestao_vagas.modules.jobs.repositories.CandidateJobRepository;
import br.com.eliel.gestao_vagas.modules.jobs.repositories.JobsRepository;
import br.com.eliel.gestao_vagas.modules.jobs.useCases.ListJobCandidatesUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class ListJobCandidatesUseCaseTest {

    @InjectMocks
    private ListJobCandidatesUseCase listJobCandidatesUseCase;

    @Mock
    private CandidateJobRepository candidateJobRepository;
    @Mock
    private JobsRepository jobsRepository;
    @Mock
    private CandidateRepository candidateRepository;

    private UUID jobId;
    private UUID companyId;
    private UUID candidateId;
    private CompanyEntity company;
    private JobEntity jobEntity;
    private CandidateJobEntity candidateJobEntity;
    private CandidateEntity candidateEntity;

    @BeforeEach
    void setUp() {
        jobId = UUID.randomUUID();
        companyId = UUID.randomUUID();
        candidateId = UUID.randomUUID();
        company = new CompanyEntity();
        company.setId(companyId);
        company.setName("Empresa X");
        jobEntity = JobEntity.builder()
                .id(jobId)
                .company(company)
                .build();
        candidateJobEntity = CandidateJobEntity.builder()
                .id(UUID.randomUUID())
                .candidateId(candidateId)
                .jobId(jobId)
                .build();
        candidateEntity = new CandidateEntity();
        candidateEntity.setId(candidateId);
        candidateEntity.setName("Renata");
        candidateEntity.setEmail("renata@email.com");
        candidateEntity.setUsername("renata123");
        candidateEntity.setDescription("Descrição");
    }

    @Test
    void testExecuteReturnsCandidateJobResponseDTOList() {
        when(jobsRepository.findById(jobId)).thenReturn(Optional.of(jobEntity));
        when(candidateJobRepository.findByJobId(jobId)).thenReturn(List.of(candidateJobEntity));
        when(candidateRepository.findById(candidateId)).thenReturn(Optional.of(candidateEntity));

        List<CandidateJobResponseDTO> result = listJobCandidatesUseCase.execute(jobId, companyId);

        assertEquals(1, result.size());
        CandidateJobResponseDTO dto = result.get(0);
        assertEquals(candidateEntity.getId(), dto.getId());
        assertEquals(candidateEntity.getName(), dto.getName());
        assertEquals(candidateEntity.getEmail(), dto.getEmail());
        assertEquals(candidateEntity.getUsername(), dto.getUsername());
        assertEquals(candidateEntity.getDescription(), dto.getDescription());

        verify(jobsRepository).findById(jobId);
        verify(candidateJobRepository).findByJobId(jobId);
        verify(candidateRepository).findById(candidateId);
    }

    @Test
    void testExecuteJobNotFoundThrowsException() {
        when(jobsRepository.findById(jobId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                listJobCandidatesUseCase.execute(jobId, companyId));
        assertEquals("Vaga não encontrada", ex.getMessage());
        verify(jobsRepository).findById(jobId);
        verify(candidateJobRepository, never()).findByJobId(any());
        verify(candidateRepository, never()).findById(any());
    }

    @Test
    void testExecuteNoPermissionThrowsAuthenticationException() {
        CompanyEntity otherCompany = new CompanyEntity();
        otherCompany.setId(UUID.randomUUID());
        JobEntity jobOther = JobEntity.builder().id(jobId).company(otherCompany).build();
        when(jobsRepository.findById(jobId)).thenReturn(Optional.of(jobOther));

        AuthenticationException ex = assertThrows(AuthenticationException.class, () ->
                listJobCandidatesUseCase.execute(jobId, companyId));
        assertEquals("Você não tem permissão para ver os candidatos desta vaga", ex.getMessage());
        verify(jobsRepository).findById(jobId);
        verify(candidateJobRepository, never()).findByJobId(any());
        verify(candidateRepository, never()).findById(any());
    }

    @Test
    void testExecuteCandidateNotFoundThrowsException() {
        when(jobsRepository.findById(jobId)).thenReturn(Optional.of(jobEntity));
        when(candidateJobRepository.findByJobId(jobId)).thenReturn(List.of(candidateJobEntity));
        when(candidateRepository.findById(candidateId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                listJobCandidatesUseCase.execute(jobId, companyId));
        assertEquals("Candidato não encontrado", ex.getMessage());
        verify(jobsRepository).findById(jobId);
        verify(candidateJobRepository).findByJobId(jobId);
        verify(candidateRepository).findById(candidateId);
    }

    @Test
    void testExecuteWithNoCandidatesReturnsEmptyList() {
        when(jobsRepository.findById(jobId)).thenReturn(Optional.of(jobEntity));
        when(candidateJobRepository.findByJobId(jobId)).thenReturn(Collections.emptyList());

        List<CandidateJobResponseDTO> result = listJobCandidatesUseCase.execute(jobId, companyId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(jobsRepository).findById(jobId);
        verify(candidateJobRepository).findByJobId(jobId);
        verify(candidateRepository, never()).findById(any());
    }
}
