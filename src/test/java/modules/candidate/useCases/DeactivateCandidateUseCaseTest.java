package br.com.eliel.gestao_vagas.modules.candidate.useCases;

import br.com.eliel.gestao_vagas.modules.candidate.entites.CandidateEntity;
import br.com.eliel.gestao_vagas.modules.candidate.repositories.CandidateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeactivateCandidateUseCaseTest {

    @InjectMocks
    private DeactivateCandidateUseCase deactivateCandidateUseCase;

    @Mock
    private CandidateRepository candidateRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void execute_DeactivatesCandidate_WhenPasswordIsCorrect() {
        UUID candidateId = UUID.randomUUID();
        CandidateEntity candidate = new CandidateEntity();
        candidate.setId(candidateId);
        candidate.setPassword("hashedPassword");
        candidate.setActive(true);

        when(candidateRepository.findById(candidateId)).thenReturn(Optional.of(candidate));
        when(passwordEncoder.matches("senha123", "hashedPassword")).thenReturn(true);
        when(candidateRepository.save(any(CandidateEntity.class))).thenReturn(candidate);

        deactivateCandidateUseCase.execute(candidateId, "senha123");

        assertFalse(candidate.isActive());
        verify(candidateRepository).save(candidate);
    }

    @Test
    void execute_ThrowsException_WhenCandidateNotFound() {
        UUID candidateId = UUID.randomUUID();
        when(candidateRepository.findById(candidateId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> 
            deactivateCandidateUseCase.execute(candidateId, "senha123")
        );
        assertEquals("Candidato não encontrada", ex.getMessage());
    }

    @Test
    void execute_ThrowsException_WhenPasswordIsIncorrect() {
        UUID candidateId = UUID.randomUUID();
        CandidateEntity candidate = new CandidateEntity();
        candidate.setId(candidateId);
        candidate.setPassword("hashedPassword");
        candidate.setActive(true);

        when(candidateRepository.findById(candidateId)).thenReturn(Optional.of(candidate));
        when(passwordEncoder.matches("senhaErrada", "hashedPassword")).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> 
            deactivateCandidateUseCase.execute(candidateId, "senhaErrada")
        );
        assertEquals("Senha incorreta", ex.getMessage());
    }
}