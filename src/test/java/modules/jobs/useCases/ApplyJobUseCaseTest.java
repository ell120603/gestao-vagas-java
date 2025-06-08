package modules.jobs.useCases;

import br.com.eliel.gestao_vagas.modules.candidate.entites.CandidateEntity;
import br.com.eliel.gestao_vagas.modules.candidate.repositories.CandidateRepository;
import br.com.eliel.gestao_vagas.modules.jobs.entites.CandidateJobEntity;
import br.com.eliel.gestao_vagas.modules.jobs.entites.JobEntity;
import br.com.eliel.gestao_vagas.modules.jobs.repositories.CandidateJobRepository;
import br.com.eliel.gestao_vagas.modules.jobs.repositories.JobsRepository;
import br.com.eliel.gestao_vagas.modules.jobs.useCases.ApplyJobUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplyJobUseCaseTest {

    @InjectMocks
    private ApplyJobUseCase applyJobUseCase;

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private JobsRepository jobsRepository;

    @Mock
    private CandidateJobRepository candidateJobRepository;

    private UUID candidateId;
    private UUID jobId;
    private CandidateEntity candidate;
    private JobEntity job;

    @BeforeEach
    void setUp() {
        candidateId = UUID.randomUUID();
        jobId = UUID.randomUUID();
        
        candidate = new CandidateEntity();
        candidate.setId(candidateId);
        candidate.setName("Candidato Teste");
        candidate.setUsername("candidato.teste");
        candidate.setEmail("candidato@teste.com");
        candidate.setPassword("senha123456");
        candidate.setDescription("Descrição do candidato");
            
        job = JobEntity.builder()
            .id(jobId)
            .titulo("Vaga Teste")
            .descricao("Descrição da vaga")
            .build();
    }

    @Test
    void shouldApplyJobSuccessfully() {
        when(candidateRepository.findById(candidateId)).thenReturn(Optional.of(candidate));
        when(jobsRepository.findById(jobId)).thenReturn(Optional.of(job));
        when(candidateJobRepository.findByCandidateIdAndJobId(candidateId, jobId)).thenReturn(Optional.empty());

        CandidateJobEntity savedEntity = CandidateJobEntity.builder()
                .id(UUID.randomUUID())
                .candidateId(candidateId)
                .jobId(jobId)
                .build();

        when(candidateJobRepository.save(any(CandidateJobEntity.class))).thenReturn(savedEntity);

        CandidateJobEntity result = applyJobUseCase.execute(candidateId, jobId);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(candidateId, result.getCandidateId());
        assertEquals(jobId, result.getJobId());
        
        verify(candidateRepository).findById(candidateId);
        verify(jobsRepository).findById(jobId);
        verify(candidateJobRepository).findByCandidateIdAndJobId(candidateId, jobId);
        verify(candidateJobRepository).save(any(CandidateJobEntity.class));
    }

    @Test
    void shouldThrowExceptionWhenCandidateNotFound() {
        when(candidateRepository.findById(candidateId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            applyJobUseCase.execute(candidateId, jobId);
        });

        assertEquals("Candidato não encontrado", exception.getMessage());
        verify(candidateRepository).findById(candidateId);
        verifyNoMoreInteractions(jobsRepository, candidateJobRepository);
    }

    @Test
    void shouldThrowExceptionWhenJobNotFound() {
        when(candidateRepository.findById(candidateId)).thenReturn(Optional.of(candidate));
        when(jobsRepository.findById(jobId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            applyJobUseCase.execute(candidateId, jobId);
        });

        assertEquals("Vaga não encontrada", exception.getMessage());
        verify(candidateRepository).findById(candidateId);
        verify(jobsRepository).findById(jobId);
        verifyNoMoreInteractions(candidateJobRepository);
    }

    @Test
    void shouldThrowExceptionWhenCandidateAlreadyApplied() {
        when(candidateRepository.findById(candidateId)).thenReturn(Optional.of(candidate));
        when(jobsRepository.findById(jobId)).thenReturn(Optional.of(job));
        
        CandidateJobEntity existingApplication = CandidateJobEntity.builder()
            .id(UUID.randomUUID())
            .candidateId(candidateId)
            .jobId(jobId)
            .build();
            
        when(candidateJobRepository.findByCandidateIdAndJobId(candidateId, jobId))
                .thenReturn(Optional.of(existingApplication));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            applyJobUseCase.execute(candidateId, jobId);
        });

        assertEquals("Candidato já se candidatou para esta vaga", exception.getMessage());
        verify(candidateJobRepository, never()).save(any());
    }
}