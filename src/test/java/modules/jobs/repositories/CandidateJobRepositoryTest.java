package br.com.eliel.gestao_vagas.modules.jobs.repositories;

import br.com.eliel.gestao_vagas.modules.jobs.entites.CandidateJobEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CandidateJobRepositoryTest {

    @Mock
    private CandidateJobRepository repository;

    @Test
    void testFindByCandidateIdAndJobId() {
        UUID candidateId = UUID.randomUUID();
        UUID jobId = UUID.randomUUID();
        CandidateJobEntity entity = CandidateJobEntity.builder()
                .candidateId(candidateId)
                .jobId(jobId)
                .build();

        when(repository.findByCandidateIdAndJobId(candidateId, jobId))
                .thenReturn(Optional.of(entity));

        Optional<CandidateJobEntity> result = repository.findByCandidateIdAndJobId(candidateId, jobId);

        assertTrue(result.isPresent());
        assertEquals(candidateId, result.get().getCandidateId());
        assertEquals(jobId, result.get().getJobId());
    }

    @Test
    void testFindByCandidateIdAndJobId_NotFound() {
        UUID candidateId = UUID.randomUUID();
        UUID jobId = UUID.randomUUID();

        when(repository.findByCandidateIdAndJobId(candidateId, jobId))
                .thenReturn(Optional.empty());

        Optional<CandidateJobEntity> result = repository.findByCandidateIdAndJobId(candidateId, jobId);

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByCandidateId() {
        UUID candidateId = UUID.randomUUID();
        CandidateJobEntity entity1 = CandidateJobEntity.builder().candidateId(candidateId).build();
        CandidateJobEntity entity2 = CandidateJobEntity.builder().candidateId(candidateId).build();
        List<CandidateJobEntity> list = List.of(entity1, entity2);

        when(repository.findByCandidateId(candidateId)).thenReturn(list);

        List<CandidateJobEntity> result = repository.findByCandidateId(candidateId);

        assertEquals(2, result.size());
        assertEquals(candidateId, result.get(0).getCandidateId());
    }

    @Test
    void testFindByJobId() {
        UUID jobId = UUID.randomUUID();
        CandidateJobEntity entity1 = CandidateJobEntity.builder().jobId(jobId).build();
        CandidateJobEntity entity2 = CandidateJobEntity.builder().jobId(jobId).build();
        List<CandidateJobEntity> list = List.of(entity1, entity2);

        when(repository.findByJobId(jobId)).thenReturn(list);

        List<CandidateJobEntity> result = repository.findByJobId(jobId);

        assertEquals(2, result.size());
        assertEquals(jobId, result.get(0).getJobId());
    }
}