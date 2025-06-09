package br.com.eliel.gestao_vagas.modules.candidate.useCases;

import br.com.eliel.gestao_vagas.modules.candidate.dto.ProfileCandidateResponseDTO;
import br.com.eliel.gestao_vagas.modules.candidate.entites.CandidateEntity;
import br.com.eliel.gestao_vagas.modules.candidate.repositories.CandidateRepository;
import br.com.eliel.gestao_vagas.modules.candidate.useCases.ProfileCandidateUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfileCandidateUseCaseTest {

    @Mock
    private CandidateRepository candidateRepository;

    @InjectMocks
    private ProfileCandidateUseCase profileCandidateUseCase;

    private UUID candidateId;
    private CandidateEntity candidate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        candidateId = UUID.randomUUID();
        candidate = new CandidateEntity();
        candidate.setId(candidateId);
        candidate.setUsername("joaosilva");
        candidate.setEmail("joao@email.com");
        candidate.setName("João Silva");
        candidate.setDescription("Desenvolvedor Java");
    }

    @Test
    void execute_ShouldReturnCandidate_WhenCandidateExists() {
        when(candidateRepository.findById(candidateId)).thenReturn(Optional.of(candidate));

        ProfileCandidateResponseDTO result = profileCandidateUseCase.execute(candidateId);

        assertNotNull(result);
        assertEquals("joaosilva", result.getUsername());
        assertEquals("joao@email.com", result.getEmail());
        assertEquals("João Silva", result.getName());
        assertEquals("Desenvolvedor Java", result.getDescription());
        verify(candidateRepository, times(1)).findById(candidateId);
    }

    @Test
    void execute_ShouldThrowException_WhenCandidateNotFound() {
        when(candidateRepository.findById(candidateId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> profileCandidateUseCase.execute(candidateId));
        assertEquals("Candidato não encontrado", exception.getMessage());
        verify(candidateRepository, times(1)).findById(candidateId);
    }
}
