package br.com.eliel.gestao_vagas.modules.company.controllers;

import br.com.eliel.gestao_vagas.modules.company.controllers.ProfileCompanyController;
import br.com.eliel.gestao_vagas.modules.company.dto.ProfileCompanyResponseDTO;
import br.com.eliel.gestao_vagas.modules.company.useCases.ProfileCompanyUseCase;
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

class ProfileCompanyControllerTest {

    @InjectMocks
    private ProfileCompanyController profileCompanyController;

    @Mock
    private ProfileCompanyUseCase profileCompanyUseCase;

    @Mock
    private HttpServletRequest request;

    private UUID companyId;
    private ProfileCompanyResponseDTO profileCompanyResponseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        companyId = UUID.randomUUID();
        
        profileCompanyResponseDTO = ProfileCompanyResponseDTO.builder()
            .name("Tech Solutions")
            .username("techsolutions")
            .email("contato@techsolutions.com")
            .website("https://techsolutions.com")
            .description("Empresa de tecnologia")
            .build();
    }

    @Test
    void profile_ShouldReturnOk_WhenCompanyExists() {
        when(request.getAttribute("company_id")).thenReturn(companyId);
        when(profileCompanyUseCase.execute(any(UUID.class))).thenReturn(profileCompanyResponseDTO);

        ResponseEntity<ProfileCompanyResponseDTO> response = profileCompanyController.profile(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(profileCompanyResponseDTO.getName(), response.getBody().getName());
        assertEquals(profileCompanyResponseDTO.getUsername(), response.getBody().getUsername());
        assertEquals(profileCompanyResponseDTO.getEmail(), response.getBody().getEmail());
        assertEquals(profileCompanyResponseDTO.getWebsite(), response.getBody().getWebsite());
        assertEquals(profileCompanyResponseDTO.getDescription(), response.getBody().getDescription());
        verify(profileCompanyUseCase, times(1)).execute(companyId);
    }

    @Test
    void profile_ShouldThrowException_WhenCompanyNotFound() {
        when(request.getAttribute("company_id")).thenReturn(companyId);
        when(profileCompanyUseCase.execute(any(UUID.class)))
            .thenThrow(new RuntimeException("Empresa não encontrada"));

        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> profileCompanyController.profile(request));
        
        assertEquals("Empresa não encontrada", exception.getMessage());
        verify(profileCompanyUseCase, times(1)).execute(companyId);
    }
}
