package br.com.eliel.gestao_vagas.modules.jobs.repositories;

import br.com.eliel.gestao_vagas.modules.jobs.entites.JobEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobsRepositoryTest {

    @Mock
    private JobsRepository jobsRepository;

    @Test
    void testFindByCompanyId() {
        UUID companyId = UUID.randomUUID();
        JobEntity job1 = JobEntity.builder().id(UUID.randomUUID()).companyId(companyId).build();
        JobEntity job2 = JobEntity.builder().id(UUID.randomUUID()).companyId(companyId).build();
        List<JobEntity> jobs = List.of(job1, job2);

        when(jobsRepository.findByCompanyId(companyId)).thenReturn(jobs);

        List<JobEntity> result = jobsRepository.findByCompanyId(companyId);

        assertEquals(2, result.size());
        assertEquals(companyId, result.get(0).getCompanyId());
        assertEquals(companyId, result.get(1).getCompanyId());
    }

    @Test
    void testFindByActiveTrue() {
        JobEntity job1 = JobEntity.builder().id(UUID.randomUUID()).active(true).build();
        JobEntity job2 = JobEntity.builder().id(UUID.randomUUID()).active(true).build();
        List<JobEntity> jobs = List.of(job1, job2);

        when(jobsRepository.findByActiveTrue()).thenReturn(jobs);

        List<JobEntity> result = jobsRepository.findByActiveTrue();

        assertEquals(2, result.size());
        assertTrue(result.get(0).isActive());
        assertTrue(result.get(1).isActive());
    }
}