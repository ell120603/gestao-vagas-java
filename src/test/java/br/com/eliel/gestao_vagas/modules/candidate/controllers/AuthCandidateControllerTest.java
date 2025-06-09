package br.com.eliel.gestao_vagas.modules.candidate.controllers;

import br.com.eliel.gestao_vagas.modules.candidate.dto.AuthCandidateDTO;
import br.com.eliel.gestao_vagas.modules.candidate.dto.AuthCandidateResponseDTO;
import br.com.eliel.gestao_vagas.modules.candidate.useCases.AuthCandidateUseCase;
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

class AuthCandidateControllerTest {

    @InjectMocks
    private AuthCandidateController authCandidateController;

    @Mock
    private AuthCandidateUseCase authCandidateUseCase;

    private AuthCandidateDTO authCandidateDTO;
    private AuthCandidateResponseDTO authCandidateResponseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        authCandidateDTO = new AuthCandidateDTO("senha123", "joaosilva");

        UUID candidateId = UUID.randomUUID();
        authCandidateResponseDTO = AuthCandidateResponseDTO.builder()
            .id(candidateId)
            .username("joaosilva")
            .token("token123")
            .build();
    }

    @Test
    void auth_ShouldReturnOk_WhenCredentialsAreValid() {
        when(authCandidateUseCase.execute(authCandidateDTO)).thenReturn(authCandidateResponseDTO);

        ResponseEntity<Object> response = authCandidateController.auth(authCandidateDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        AuthCandidateResponseDTO responseBody = (AuthCandidateResponseDTO) response.getBody();
        assertEquals(authCandidateResponseDTO.getId(), responseBody.getId());
        assertEquals(authCandidateResponseDTO.getUsername(), responseBody.getUsername());
        assertEquals(authCandidateResponseDTO.getToken(), responseBody.getToken());
        verify(authCandidateUseCase, times(1)).execute(authCandidateDTO);
    }

    @Test
    void auth_ShouldReturnUnauthorized_WhenCredentialsAreInvalid() {
        when(authCandidateUseCase.execute(authCandidateDTO))
            .thenThrow(new RuntimeException("Credenciais inválidas"));

        ResponseEntity<Object> response = authCandidateController.auth(authCandidateDTO);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Credenciais inválidas", response.getBody());
        verify(authCandidateUseCase, times(1)).execute(authCandidateDTO);
    }

    @Test
    void auth_ShouldReturnUnauthorized_WhenCandidateNotFound() {
        when(authCandidateUseCase.execute(authCandidateDTO))
            .thenThrow(new RuntimeException("Candidato não encontrado"));

        ResponseEntity<Object> response = authCandidateController.auth(authCandidateDTO);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Candidato não encontrado", response.getBody());
        verify(authCandidateUseCase, times(1)).execute(authCandidateDTO);
    }
}
