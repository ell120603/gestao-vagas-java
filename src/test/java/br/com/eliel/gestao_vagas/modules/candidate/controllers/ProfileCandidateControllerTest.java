package br.com.eliel.gestao_vagas.modules.candidate.controllers;

import br.com.eliel.gestao_vagas.modules.candidate.dto.ProfileCandidateResponseDTO;
import br.com.eliel.gestao_vagas.modules.candidate.useCases.ProfileCandidateUseCase;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfileCandidateControllerTest {

    @InjectMocks
    private ProfileCandidateController profileCandidateController;

    @Mock
    private ProfileCandidateUseCase profileCandidateUseCase;

    @Mock
    private HttpServletRequest request;

    private UUID candidateId;
    private ProfileCandidateResponseDTO profileDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        candidateId = UUID.randomUUID();
        
        profileDTO = ProfileCandidateResponseDTO.builder()
            .name("João Silva")
            .username("joaosilva")
            .email("joao@email.com")
            .description("Desenvolvedor Java")
            .build();
    }

    @Test
    void profile_ShouldReturnOk_WhenProfileIsFound() {
        when(request.getAttribute("candidate_id")).thenReturn(candidateId.toString());
        when(profileCandidateUseCase.execute(candidateId)).thenReturn(profileDTO);

        ResponseEntity<Object> response = profileCandidateController.profile(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ProfileCandidateResponseDTO);
        assertEquals(profileDTO, response.getBody());
        verify(profileCandidateUseCase, times(1)).execute(candidateId);
    }

    @Test
    void profile_ShouldReturnNotFound_WhenCandidateDoesNotExist() {
        when(request.getAttribute("candidate_id")).thenReturn(candidateId.toString());
        when(profileCandidateUseCase.execute(candidateId))
            .thenThrow(new RuntimeException("Candidato não encontrado"));

        ResponseEntity<Object> response = profileCandidateController.profile(request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Candidato não encontrado", response.getBody());
        verify(profileCandidateUseCase, times(1)).execute(candidateId);
    }

    @Test
    void profile_ShouldReturnBadRequest_WhenCandidateIdIsInvalid() {
        when(request.getAttribute("candidate_id")).thenReturn("invalid-uuid");

        ResponseEntity<Object> response = profileCandidateController.profile(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("ID do candidato inválido", response.getBody());
        verify(profileCandidateUseCase, never()).execute(any());
    }
}
