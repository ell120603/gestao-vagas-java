package modules.jobs.entites;
import br.com.eliel.gestao_vagas.modules.jobs.entites.CandidateJobEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CandidateJobEntityTest {

    @Test
    void testBuilderAndGetters() {
        UUID id = UUID.randomUUID();
        UUID candidateId = UUID.randomUUID();
        UUID jobId = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();

        CandidateJobEntity entity = CandidateJobEntity.builder()
                .id(id)
                .candidateId(candidateId)
                .jobId(jobId)
                .createdAt(createdAt)
                .build();

        assertEquals(id, entity.getId());
        assertEquals(candidateId, entity.getCandidateId());
        assertEquals(jobId, entity.getJobId());
        assertEquals(createdAt, entity.getCreatedAt());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        CandidateJobEntity entity = new CandidateJobEntity();
        UUID id = UUID.randomUUID();
        UUID candidateId = UUID.randomUUID();
        UUID jobId = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();

        entity.setId(id);
        entity.setCandidateId(candidateId);
        entity.setJobId(jobId);
        entity.setCreatedAt(createdAt);

        assertEquals(id, entity.getId());
        assertEquals(candidateId, entity.getCandidateId());
        assertEquals(jobId, entity.getJobId());
        assertEquals(createdAt, entity.getCreatedAt());
    }

    @Test
    void testAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        UUID candidateId = UUID.randomUUID();
        UUID jobId = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();

        CandidateJobEntity entity = new CandidateJobEntity(id, candidateId, jobId, createdAt);

        assertEquals(id, entity.getId());
        assertEquals(candidateId, entity.getCandidateId());
        assertEquals(jobId, entity.getJobId());
        assertEquals(createdAt, entity.getCreatedAt());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        CandidateJobEntity entity1 = CandidateJobEntity.builder().id(id).build();
        CandidateJobEntity entity2 = CandidateJobEntity.builder().id(id).build();

        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    void testToString() {
        CandidateJobEntity entity = CandidateJobEntity.builder()
                .id(UUID.randomUUID())
                .candidateId(UUID.randomUUID())
                .jobId(UUID.randomUUID())
                .build();

        String str = entity.toString();
        assertTrue(str.contains("CandidateJobEntity"));
    }
}