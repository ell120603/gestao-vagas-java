package br.com.eliel.gestao_vagas.modules.jobs.useCases;

import br.com.eliel.gestao_vagas.modules.candidate.repositories.CandidateRepository;
import br.com.eliel.gestao_vagas.modules.jobs.entites.CandidateJobEntity;
import br.com.eliel.gestao_vagas.modules.jobs.repositories.CandidateJobRepository;
import br.com.eliel.gestao_vagas.modules.jobs.repositories.JobsRepository;
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

    @BeforeEach
    void setUp() {
        candidateId = UUID.randomUUID();
        jobId = UUID.randomUUID();
    }

    @Test
    void shouldApplyJobSuccessfully() {
        when(candidateRepository.findById(candidateId)).thenReturn(Optional.of(new Object()));
        when(jobsRepository.findById(jobId)).thenReturn(Optional.of(new Object()));
        when(candidateJobRepository.findByCandidateIdAndJobId(candidateId, jobId)).thenReturn(Optional.empty());

        CandidateJobEntity savedEntity = CandidateJobEntity.builder()
                .id(UUID.randomUUID())
                .candidateId(candidateId)
                .jobId(jobId)
                .build();

        when(candidateJobRepository.save(any(CandidateJobEntity.class))).thenReturn(savedEntity);

        CandidateJobEntity result = applyJobUseCase.execute(candidateId, jobId);

        assertNotNull(result.getId());
        assertEquals(candidateId, result.getCandidateId());
        assertEquals(jobId, result.getJobId());
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
        when(candidateRepository.findById(candidateId)).thenReturn(Optional.of(new Object()));
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
        when(candidateRepository.findById(candidateId)).thenReturn(Optional.of(new Object()));
        when(jobsRepository.findById(jobId)).thenReturn(Optional.of(new Object()));
        when(candidateJobRepository.findByCandidateIdAndJobId(candidateId, jobId))
                .thenReturn(Optional.of(new CandidateJobEntity()));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            applyJobUseCase.execute(candidateId, jobId);
        });

        assertEquals("Candidato já se candidatou para esta vaga", exception.getMessage());
        verify(candidateJobRepository, never()).save(any());
    }
}