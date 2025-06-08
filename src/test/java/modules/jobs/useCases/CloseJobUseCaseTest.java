package br.com.eliel.gestao_vagas.modules.jobs.useCases;

import br.com.eliel.gestao_vagas.modules.company.entites.CompanyEntity;
import br.com.eliel.gestao_vagas.modules.jobs.entites.JobEntity;
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
class CloseJobUseCaseTest {

    @InjectMocks
    private CloseJobUseCase closeJobUseCase;

    @Mock
    private JobsRepository jobsRepository;

    private UUID jobId;
    private UUID companyId;
    private JobEntity activeJob;
    private CompanyEntity company;

    @BeforeEach
    void setUp() {
        jobId = UUID.randomUUID();
        companyId = UUID.randomUUID();
        company = CompanyEntity.builder().id(companyId).build();
        activeJob = JobEntity.builder()
                .id(jobId)
                .company(company)
                .active(true)
                .build();
    }

    @Test
    void shouldCloseJobSuccessfully() {
        when(jobsRepository.findById(jobId)).thenReturn(Optional.of(activeJob));
        when(jobsRepository.save(any(JobEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        JobEntity result = closeJobUseCase.execute(jobId, companyId);

        assertFalse(result.isActive());
        verify(jobsRepository).save(activeJob);
    }

    @Test
    void shouldThrowExceptionWhenJobNotFound() {
        when(jobsRepository.findById(jobId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            closeJobUseCase.execute(jobId, companyId);
        });

        assertEquals("Vaga não encontrada.", exception.getMessage());
        verify(jobsRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenCompanyNotAuthorized() {
        UUID otherCompanyId = UUID.randomUUID();
        CompanyEntity otherCompany = CompanyEntity.builder().id(otherCompanyId).build();
        JobEntity job = JobEntity.builder()
                .id(jobId)
                .company(otherCompany)
                .active(true)
                .build();

        when(jobsRepository.findById(jobId)).thenReturn(Optional.of(job));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            closeJobUseCase.execute(jobId, companyId);
        });

        assertEquals("Você não tem permissão para fechar esta vaga.", exception.getMessage());
        verify(jobsRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenJobAlreadyClosed() {
        activeJob.setActive(false);
        when(jobsRepository.findById(jobId)).thenReturn(Optional.of(activeJob));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            closeJobUseCase.execute(jobId, companyId);
        });

        assertEquals("A vaga já está fechada.", exception.getMessage());
        verify(jobsRepository, never()).save(any());
    }
}