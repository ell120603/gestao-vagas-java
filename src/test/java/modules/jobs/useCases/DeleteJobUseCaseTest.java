package br.com.eliel.gestao_vagas.modules.jobs.useCases;

import br.com.eliel.gestao_vagas.exceptions.AuthenticationException;
import br.com.eliel.gestao_vagas.modules.company.entites.CompanyEntity;
import br.com.eliel.gestao_vagas.modules.company.repositories.CompanyRepository;
import br.com.eliel.gestao_vagas.modules.jobs.entites.JobEntity;
import br.com.eliel.gestao_vagas.modules.jobs.repositories.JobsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteJobUseCaseTest {

    @InjectMocks
    private DeleteJobUseCase deleteJobUseCase;

    @Mock
    private JobsRepository jobsRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UUID jobId;
    private UUID companyId;
    private String correctPassword;
    private String wrongPassword;
    private CompanyEntity company;
    private JobEntity validJob;
    private JobEntity unauthorizedJob;

    @BeforeEach
    void setUp() {
        jobId = UUID.randomUUID();
        companyId = UUID.randomUUID();
        correctPassword = "senha123";
        wrongPassword = "senhaErrada";
        company = CompanyEntity.builder()
                .id(companyId)
                .password("encodedPassword")
                .build();
        validJob = JobEntity.builder()
                .id(jobId)
                .company(company)
                .build();
        unauthorizedJob = JobEntity.builder()
                .id(jobId)
                .company(CompanyEntity.builder().id(UUID.randomUUID()).build())
                .build();
    }

    @Test
    void shouldDeleteJobSuccessfully() {
        when(jobsRepository.findById(jobId)).thenReturn(Optional.of(validJob));
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(passwordEncoder.matches(correctPassword, company.getPassword())).thenReturn(true);

        assertDoesNotThrow(() -> deleteJobUseCase.execute(jobId, companyId, correctPassword));
        verify(jobsRepository).delete(validJob);
    }

    @Test
    void shouldThrowExceptionWhenJobNotFound() {
        when(jobsRepository.findById(jobId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> 
            deleteJobUseCase.execute(jobId, companyId, correctPassword));
        verify(jobsRepository, never()).delete(any());
    }

    @Test
    void shouldThrowExceptionWhenCompanyNotAuthorized() {
        when(jobsRepository.findById(jobId)).thenReturn(Optional.of(unauthorizedJob));

        AuthenticationException exception = assertThrows(AuthenticationException.class, () ->
            deleteJobUseCase.execute(jobId, companyId, correctPassword));
        assertEquals("Você não tem permissão para deletar esta vaga", exception.getMessage());
        verify(jobsRepository, never()).delete(any());
    }

    @Test
    void shouldThrowExceptionWhenCompanyNotFound() {
        when(jobsRepository.findById(jobId)).thenReturn(Optional.of(validJob));
        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
            deleteJobUseCase.execute(jobId, companyId, correctPassword));
        verify(jobsRepository, never()).delete(any());
    }

    @Test
    void shouldThrowExceptionWhenPasswordIncorrect() {
        when(jobsRepository.findById(jobId)).thenReturn(Optional.of(validJob));
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(passwordEncoder.matches(wrongPassword, company.getPassword())).thenReturn(false);

        AuthenticationException exception = assertThrows(AuthenticationException.class, () ->
            deleteJobUseCase.execute(jobId, companyId, wrongPassword));
        assertEquals("Senha incorreta", exception.getMessage());
        verify(jobsRepository, never()).delete(any());
    }
}