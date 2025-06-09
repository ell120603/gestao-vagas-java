package br.com.eliel.gestao_vagas.modules.candidate.useCases;

import br.com.eliel.gestao_vagas.modules.candidate.entites.CandidateEntity;
import br.com.eliel.gestao_vagas.modules.candidate.repositories.CandidateRepository;
import br.com.eliel.gestao_vagas.modules.candidate.useCases.DeactivateCandidateUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeactivateCandidateUseCaseTest {

    @Mock
    private CandidateRepository candidateRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DeactivateCandidateUseCase deactivateCandidateUseCase;

    private UUID candidateId;
    private CandidateEntity candidate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        candidateId = UUID.randomUUID();
        candidate = new CandidateEntity();
        candidate.setId(candidateId);
        candidate.setPassword("senha123");
        candidate.setActive(true);
    }

    @Test
    void execute_ShouldDeactivateCandidate_WhenPasswordIsCorrect() {
        when(candidateRepository.findById(candidateId)).thenReturn(Optional.of(candidate));
        when(passwordEncoder.matches("senha123", "senha123")).thenReturn(true);
        when(candidateRepository.save(any(CandidateEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertDoesNotThrow(() -> deactivateCandidateUseCase.execute(candidateId, "senha123"));
        assertFalse(candidate.isActive());
        verify(candidateRepository, times(1)).findById(candidateId);
        verify(passwordEncoder, times(1)).matches("senha123", "senha123");
        verify(candidateRepository, times(1)).save(candidate);
    }

    @Test
    void execute_ShouldThrowException_WhenPasswordIsIncorrect() {
        when(candidateRepository.findById(candidateId)).thenReturn(Optional.of(candidate));
        when(passwordEncoder.matches("senhaErrada", "senha123")).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> deactivateCandidateUseCase.execute(candidateId, "senhaErrada"));
        assertEquals("Senha incorreta", exception.getMessage());
        verify(candidateRepository, times(1)).findById(candidateId);
        verify(passwordEncoder, times(1)).matches("senhaErrada", "senha123");
        verify(candidateRepository, never()).save(any(CandidateEntity.class));
    }

    @Test
    void execute_ShouldThrowException_WhenCandidateNotFound() {
        when(candidateRepository.findById(candidateId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> deactivateCandidateUseCase.execute(candidateId, "senha123"));
        assertEquals("Candidato não encontrado", exception.getMessage());
        verify(candidateRepository, times(1)).findById(candidateId);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(candidateRepository, never()).save(any(CandidateEntity.class));
    }
}
