package br.com.eliel.gestao_vagas.modules.candidate.controllers;

import br.com.eliel.gestao_vagas.modules.candidate.dto.ProfileCandidateResponseDTO;
import br.com.eliel.gestao_vagas.modules.candidate.useCases.ProfileCandidateUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void profile_ReturnsOk_WhenSuccess() {
        UUID candidateId = UUID.randomUUID();
        ProfileCandidateResponseDTO dto = new ProfileCandidateResponseDTO();
        dto.setId(candidateId);
        dto.setName("João Silva");
        dto.setUsername("joaosilva");
        dto.setEmail("joao@email.com");
        dto.setCurriculum("https://linkedin.com/in/joaosilva");
        dto.setDescription("Desenvolvedor Java com 5 anos de experiência");
        dto.setCreatedAt("2024-03-20T10:00:00Z");

        when(request.getAttribute("candidate_id")).thenReturn(candidateId.toString());
        when(profileCandidateUseCase.execute(candidateId)).thenReturn(dto);

        ResponseEntity<ProfileCandidateResponseDTO> response = profileCandidateController.profile(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dto, response.getBody());
    }

    @Test
    void profile_ThrowsException_WhenCandidateIdIsNull() {
        when(request.getAttribute("candidate_id")).thenReturn(null);

        assertThrows(NullPointerException.class, () -> {
            profileCandidateController.profile(request);
        });
    }
}