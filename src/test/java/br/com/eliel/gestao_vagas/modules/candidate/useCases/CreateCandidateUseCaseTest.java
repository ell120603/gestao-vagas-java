package br.com.eliel.gestao_vagas.modules.candidate.useCases;

import br.com.eliel.gestao_vagas.modules.candidate.entites.CandidateEntity;
import br.com.eliel.gestao_vagas.modules.candidate.repositories.CandidateRepository;
import br.com.eliel.gestao_vagas.modules.candidate.useCases.CreateCandidateUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateCandidateUseCaseTest {

    @Mock
    private CandidateRepository candidateRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CreateCandidateUseCase createCandidateUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void execute_ShouldCreateCandidate_WhenUserDoesNotExist() {
        CandidateEntity candidate = new CandidateEntity();
        candidate.setUsername("joaosilva");
        candidate.setEmail("joao@email.com");
        candidate.setPassword("senha123");

        when(candidateRepository.findByUsernameOrEmail("joaosilva", "joao@email.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("senha123")).thenReturn("senha123-encoded");
        when(candidateRepository.save(any(CandidateEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CandidateEntity result = createCandidateUseCase.execute(candidate);

        assertNotNull(result);
        assertEquals("joaosilva", result.getUsername());
        assertEquals("joao@email.com", result.getEmail());
        assertEquals("senha123-encoded", result.getPassword());
        verify(candidateRepository, times(1)).findByUsernameOrEmail("joaosilva", "joao@email.com");
        verify(passwordEncoder, times(1)).encode("senha123");
        verify(candidateRepository, times(1)).save(any(CandidateEntity.class));
    }

    @Test
    void execute_ShouldThrowException_WhenUserAlreadyExists() {
        CandidateEntity candidate = new CandidateEntity();
        candidate.setUsername("joaosilva");
        candidate.setEmail("joao@email.com");
        candidate.setPassword("senha123");

        when(candidateRepository.findByUsernameOrEmail("joaosilva", "joao@email.com")).thenReturn(Optional.of(candidate));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> createCandidateUseCase.execute(candidate));
        assertEquals("Usuário já existe", exception.getMessage());
        verify(candidateRepository, times(1)).findByUsernameOrEmail("joaosilva", "joao@email.com");
        verify(passwordEncoder, never()).encode(anyString());
        verify(candidateRepository, never()).save(any(CandidateEntity.class));
    }
}
