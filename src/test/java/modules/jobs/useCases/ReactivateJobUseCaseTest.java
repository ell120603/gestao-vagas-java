package br.com.eliel.gestao_vagas.modules.jobs.useCases;

import br.com.eliel.gestao_vagas.exceptions.AuthenticationException;
import br.com.eliel.gestao_vagas.modules.company.entites.CompanyEntity;
import br.com.eliel.gestao_vagas.modules.jobs.entites.JobEntity;
import br.com.eliel.gestao_vagas.modules.jobs.repositories.JobsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class ReactivateJobUseCaseTest {

    @InjectMocks
    private ReactivateJobUseCase reactivateJobUseCase;

    @Mock
    private JobsRepository jobsRepository;

    private UUID jobId;
    private UUID companyId;
    private CompanyEntity company;
    private JobEntity jobEntity;

    @BeforeEach
    void setUp() {
        jobId = UUID.randomUUID();
        companyId = UUID.randomUUID();
        company = CompanyEntity.builder().id(companyId).build();
        jobEntity = JobEntity.builder()
                .id(jobId)
                .company(company)
                .active(false)
                .build();
    }

    @Test
    void testExecuteSuccess() {
        when(jobsRepository.findById(jobId)).thenReturn(Optional.of(jobEntity));
        when(jobsRepository.save(any(JobEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        JobEntity result = reactivateJobUseCase.execute(jobId, companyId);

        assertNotNull(result);
        assertTrue(result.isActive());
        verify(jobsRepository).findById(jobId);
        verify(jobsRepository).save(jobEntity);
    }

    @Test
    void testExecuteJobNotFound() {
        when(jobsRepository.findById(jobId)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                reactivateJobUseCase.execute(jobId, companyId));
        assertEquals("Vaga não encontrada", ex.getMessage());
        verify(jobsRepository).findById(jobId);
        verify(jobsRepository, never()).save(any());
    }

    @Test
    void testExecuteNoPermission() {
        CompanyEntity otherCompany = CompanyEntity.builder().id(UUID.randomUUID()).build();
        JobEntity jobOther = JobEntity.builder()
                .id(jobId)
                .company(otherCompany)
                .active(false)
                .build();

        when(jobsRepository.findById(jobId)).thenReturn(Optional.of(jobOther));

        AuthenticationException ex = assertThrows(AuthenticationException.class, () ->
                reactivateJobUseCase.execute(jobId, companyId));
        assertEquals("Você não tem permissão para reativar esta vaga", ex.getMessage());
        verify(jobsRepository).findById(jobId);
        verify(jobsRepository, never()).save(any());
    }

    @Test
    void testExecuteAlreadyActive() {
        jobEntity.setActive(true);
        when(jobsRepository.findById(jobId)).thenReturn(Optional.of(jobEntity));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                reactivateJobUseCase.execute(jobId, companyId));
        assertEquals("A vaga já está ativa", ex.getMessage());
        verify(jobsRepository).findById(jobId);
        verify(jobsRepository, never()).save(any());
    }
}
