package br.com.eliel.gestao_vagas.modules.candidate.useCases;

import br.com.eliel.gestao_vagas.modules.candidate.dto.ProfileCandidateResponseDTO;
import br.com.eliel.gestao_vagas.modules.candidate.entites.CandidateEntity;
import br.com.eliel.gestao_vagas.modules.candidate.repositories.CandidateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfileCandidateUseCaseTest {

    @InjectMocks
    private ProfileCandidateUseCase profileCandidateUseCase;

    @Mock
    private CandidateRepository candidateRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void execute_ReturnsProfile_WhenCandidateExists() {
        UUID candidateId = UUID.randomUUID();
        CandidateEntity candidate = new CandidateEntity();
        candidate.setId(candidateId);
        candidate.setName("João Silva");
        candidate.setUsername("joaosilva");
        candidate.setEmail("joao@email.com");
        candidate.setDescription("Desenvolvedor Java");

        when(candidateRepository.findById(candidateId)).thenReturn(Optional.of(candidate));

        ProfileCandidateResponseDTO result = profileCandidateUseCase.execute(candidateId);

        assertEquals("João Silva", result.getName());
        assertEquals("joaosilva", result.getUsername());
        assertEquals("joao@email.com", result.getEmail());
        assertEquals("Desenvolvedor Java", result.getDescription());
    }

    @Test
    void execute_ThrowsException_WhenCandidateNotFound() {
        UUID candidateId = UUID.randomUUID();
        when(candidateRepository.findById(candidateId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> profileCandidateUseCase.execute(candidateId));
        assertEquals("Candidato não encontrado", ex.getMessage());
    }
}