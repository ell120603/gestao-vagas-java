package br.com.eliel.gestao_vagas.modules.candidate.useCases;

import br.com.eliel.gestao_vagas.modules.candidate.dto.UpdateCandidateDTO;
import br.com.eliel.gestao_vagas.modules.candidate.entites.CandidateEntity;
import br.com.eliel.gestao_vagas.modules.candidate.repositories.CandidateRepository;
import br.com.eliel.gestao_vagas.modules.candidate.useCases.UpdateCandidateUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateCandidateUseCaseTest {

    @Mock
    private CandidateRepository candidateRepository;

    @InjectMocks
    private UpdateCandidateUseCase updateCandidateUseCase;

    private UUID candidateId;
    private CandidateEntity candidate;
    private UpdateCandidateDTO updateCandidateDTO;

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
        
        updateCandidateDTO = new UpdateCandidateDTO();
        updateCandidateDTO.setUsername("joaosilva_novo");
        updateCandidateDTO.setEmail("joao_novo@email.com");
        updateCandidateDTO.setName("João Silva Atualizado");
        updateCandidateDTO.setDescription("Desenvolvedor Java Senior");
    }

    @Test
    void execute_ShouldUpdateCandidate_WhenValidData() {
        when(candidateRepository.findById(candidateId)).thenReturn(Optional.of(candidate));
        when(candidateRepository.findByUsernameOrEmail(updateCandidateDTO.getUsername(), updateCandidateDTO.getEmail()))
            .thenReturn(Optional.empty());
        when(candidateRepository.save(any(CandidateEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CandidateEntity result = updateCandidateUseCase.execute(candidateId, updateCandidateDTO);

        assertNotNull(result);
        assertEquals(updateCandidateDTO.getUsername(), result.getUsername());
        assertEquals(updateCandidateDTO.getEmail(), result.getEmail());
        assertEquals(updateCandidateDTO.getName(), result.getName());
        assertEquals(updateCandidateDTO.getDescription(), result.getDescription());
        verify(candidateRepository, times(1)).findById(candidateId);
        verify(candidateRepository, times(1)).findByUsernameOrEmail(updateCandidateDTO.getUsername(), updateCandidateDTO.getEmail());
        verify(candidateRepository, times(1)).save(any(CandidateEntity.class));
    }

    @Test
    void execute_ShouldThrowException_WhenCandidateNotFound() {
        when(candidateRepository.findById(candidateId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> updateCandidateUseCase.execute(candidateId, updateCandidateDTO));
        
        assertEquals("Candidato não encontrado", exception.getMessage());
        verify(candidateRepository, times(1)).findById(candidateId);
        verify(candidateRepository, never()).findByUsernameOrEmail(anyString(), anyString());
        verify(candidateRepository, never()).save(any(CandidateEntity.class));
    }

    @Test
    void execute_ShouldThrowException_WhenUsernameAlreadyExists() {
        CandidateEntity existingCandidate = new CandidateEntity();
        existingCandidate.setId(UUID.randomUUID());
        existingCandidate.setUsername(updateCandidateDTO.getUsername());

        when(candidateRepository.findById(candidateId)).thenReturn(Optional.of(candidate));
        when(candidateRepository.findByUsernameOrEmail(updateCandidateDTO.getUsername(), updateCandidateDTO.getEmail()))
            .thenReturn(Optional.of(existingCandidate));

        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> updateCandidateUseCase.execute(candidateId, updateCandidateDTO));
        
        assertEquals("Usuário já existe", exception.getMessage());
        verify(candidateRepository, times(1)).findById(candidateId);
        verify(candidateRepository, times(1)).findByUsernameOrEmail(updateCandidateDTO.getUsername(), updateCandidateDTO.getEmail());
        verify(candidateRepository, never()).save(any(CandidateEntity.class));
    }

    @Test
    void execute_ShouldThrowException_WhenEmailAlreadyExists() {
        CandidateEntity existingCandidate = new CandidateEntity();
        existingCandidate.setId(UUID.randomUUID());
        existingCandidate.setEmail(updateCandidateDTO.getEmail());

        when(candidateRepository.findById(candidateId)).thenReturn(Optional.of(candidate));
        when(candidateRepository.findByUsernameOrEmail(updateCandidateDTO.getUsername(), updateCandidateDTO.getEmail()))
            .thenReturn(Optional.of(existingCandidate));

        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> updateCandidateUseCase.execute(candidateId, updateCandidateDTO));
        
        assertEquals("Usuário já existe", exception.getMessage());
        verify(candidateRepository, times(1)).findById(candidateId);
        verify(candidateRepository, times(1)).findByUsernameOrEmail(updateCandidateDTO.getUsername(), updateCandidateDTO.getEmail());
        verify(candidateRepository, never()).save(any(CandidateEntity.class));
    }
}
