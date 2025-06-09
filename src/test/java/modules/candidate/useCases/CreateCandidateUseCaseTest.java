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

class CreateCandidateUseCaseTest {

    @InjectMocks
    private CreateCandidateUseCase createCandidateUseCase;

    @Mock
    private CandidateRepository candidateRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void execute_ReturnsCandidate_WhenSuccess() {
        CandidateEntity candidate = new CandidateEntity();
        candidate.setId(UUID.randomUUID());
        candidate.setUsername("joaosilva");
        candidate.setEmail("joao@email.com");
        candidate.setPassword("senha123456");

        when(candidateRepository.findByUsernameOrEmail("joaosilva", "joao@email.com"))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode("senha123456")).thenReturn("hashedPassword");

        CandidateEntity savedCandidate = new CandidateEntity();
        savedCandidate.setId(candidate.getId());
        savedCandidate.setUsername("joaosilva");
        savedCandidate.setEmail("joao@email.com");
        savedCandidate.setPassword("hashedPassword");

        when(candidateRepository.save(any(CandidateEntity.class))).thenReturn(savedCandidate);

        CandidateEntity result = createCandidateUseCase.execute(candidate);

        assertEquals(savedCandidate, result);
        assertEquals("hashedPassword", result.getPassword());
    }

    @Test
    void execute_ThrowsException_WhenUserExists() {
        CandidateEntity candidate = new CandidateEntity();
        candidate.setUsername("joaosilva");
        candidate.setEmail("joao@email.com");

        when(candidateRepository.findByUsernameOrEmail("joaosilva", "joao@email.com"))
                .thenReturn(Optional.of(candidate));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> createCandidateUseCase.execute(candidate));
        assertEquals("Usuário já existe", ex.getMessage());
    }
}