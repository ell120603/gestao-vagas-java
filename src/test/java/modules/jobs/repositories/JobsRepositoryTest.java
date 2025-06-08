package modules.jobs.repositories;

import br.com.eliel.gestao_vagas.modules.jobs.entites.JobEntity;
import br.com.eliel.gestao_vagas.modules.jobs.repositories.JobsRepository;
import br.com.eliel.gestao_vagas.modules.company.entites.CompanyEntity;
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
        CompanyEntity company = new CompanyEntity();
        company.setId(companyId);

        JobEntity job1 = JobEntity.builder().id(UUID.randomUUID()).company(company).build();
        JobEntity job2 = JobEntity.builder().id(UUID.randomUUID()).company(company).build();
        List<JobEntity> jobs = List.of(job1, job2);

        when(jobsRepository.findByCompanyId(companyId)).thenReturn(jobs);

        List<JobEntity> result = jobsRepository.findByCompanyId(companyId);

        assertEquals(2, result.size());
        assertEquals(companyId, result.get(0).getCompany().getId());
        assertEquals(companyId, result.get(1).getCompany().getId());
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