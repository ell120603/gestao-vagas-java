package br.com.eliel.gestao_vagas.modules.candidate.controllers;

import br.com.eliel.gestao_vagas.modules.candidate.dto.UpdateCandidateDTO;
import br.com.eliel.gestao_vagas.modules.candidate.entites.CandidateEntity;
import br.com.eliel.gestao_vagas.modules.candidate.useCases.CreateCandidateUseCase;
import br.com.eliel.gestao_vagas.modules.candidate.useCases.UpdateCandidateUseCase;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CandidateControllerTest {

    @InjectMocks
    private CandidateController candidateController;

    @Mock
    private CreateCandidateUseCase createCandidateUseCase;

    @Mock
    private UpdateCandidateUseCase updateCandidateUseCase;

    @Mock
    private Authentication authentication;

    private CandidateEntity candidate;
    private UpdateCandidateDTO updateCandidateDTO;
    private UUID candidateId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        candidateId = UUID.randomUUID();
        
        candidate = new CandidateEntity();
        candidate.setId(candidateId);
        candidate.setName("João Silva");
        candidate.setUsername("joaosilva");
        candidate.setEmail("joao@email.com");
        candidate.setPassword("senha123456");
        candidate.setDescription("Desenvolvedor Java");

        updateCandidateDTO = new UpdateCandidateDTO();
        updateCandidateDTO.setName("João Silva Atualizado");
        updateCandidateDTO.setEmail("novo@email.com");
        updateCandidateDTO.setDescription("Novo desenvolvedor Java");
    }

    @Test
    void create_ShouldReturnOk_WhenCandidateIsCreated() {
        when(createCandidateUseCase.execute(any(CandidateEntity.class)))
            .thenReturn(candidate);

        ResponseEntity<Object> response = candidateController.create(candidate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(createCandidateUseCase, times(1)).execute(candidate);
    }

    @Test
    void create_ShouldReturnBadRequest_WhenCandidateAlreadyExists() {
        when(createCandidateUseCase.execute(any(CandidateEntity.class)))
            .thenThrow(new RuntimeException("Username já existe"));

        ResponseEntity<Object> response = candidateController.create(candidate);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Username já existe", response.getBody());
        verify(createCandidateUseCase, times(1)).execute(candidate);
    }

    @Test
    void update_ShouldReturnOk_WhenCandidateIsUpdated() {
        when(authentication.getName()).thenReturn(candidateId.toString());
        when(updateCandidateUseCase.execute(any(UUID.class), any(UpdateCandidateDTO.class)))
            .thenReturn(candidate);

        ResponseEntity<Object> response = candidateController.update(updateCandidateDTO, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(updateCandidateUseCase, times(1)).execute(candidateId, updateCandidateDTO);
    }

    @Test
    void update_ShouldReturnNotFound_WhenCandidateDoesNotExist() {
        when(authentication.getName()).thenReturn(candidateId.toString());
        when(updateCandidateUseCase.execute(any(UUID.class), any(UpdateCandidateDTO.class)))
            .thenThrow(new EntityNotFoundException("Candidato não encontrado"));

        ResponseEntity<Object> response = candidateController.update(updateCandidateDTO, authentication);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Candidato não encontrado", response.getBody());
        verify(updateCandidateUseCase, times(1)).execute(candidateId, updateCandidateDTO);
    }
}
