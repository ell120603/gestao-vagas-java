package br.com.eliel.gestao_vagas.modules.company.controllers;

import br.com.eliel.gestao_vagas.modules.company.controllers.AuthCompanyController;
import br.com.eliel.gestao_vagas.modules.company.dto.AuthCompanyDTO;
import br.com.eliel.gestao_vagas.modules.company.dto.AuthCompanyResponseDTO;
import br.com.eliel.gestao_vagas.modules.company.useCases.AuthCompanyUseCase;
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

class AuthCompanyControllerTest {

    @InjectMocks
    private AuthCompanyController authCompanyController;

    @Mock
    private AuthCompanyUseCase authCompanyUseCase;

    private AuthCompanyDTO authCompanyDTO;
    private AuthCompanyResponseDTO authCompanyResponseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        authCompanyDTO = new AuthCompanyDTO("senha123", "techsolutions");

        UUID companyId = UUID.randomUUID();
        authCompanyResponseDTO = AuthCompanyResponseDTO.builder()
            .id(companyId)
            .username("techsolutions")
            .token("token123")
            .build();
    }

    @Test
    void auth_ShouldReturnOk_WhenCredentialsAreValid() {
        when(authCompanyUseCase.execute(authCompanyDTO)).thenReturn(authCompanyResponseDTO);

        ResponseEntity<Object> response = authCompanyController.auth(authCompanyDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        AuthCompanyResponseDTO responseBody = (AuthCompanyResponseDTO) response.getBody();
        assertEquals(authCompanyResponseDTO.getId(), responseBody.getId());
        assertEquals(authCompanyResponseDTO.getUsername(), responseBody.getUsername());
        assertEquals(authCompanyResponseDTO.getToken(), responseBody.getToken());
        verify(authCompanyUseCase, times(1)).execute(authCompanyDTO);
    }

    @Test
    void auth_ShouldReturnUnauthorized_WhenCredentialsAreInvalid() {
        when(authCompanyUseCase.execute(authCompanyDTO))
            .thenThrow(new RuntimeException("Credenciais inválidas"));

        ResponseEntity<Object> response = authCompanyController.auth(authCompanyDTO);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Credenciais inválidas", response.getBody());
        verify(authCompanyUseCase, times(1)).execute(authCompanyDTO);
    }

    @Test
    void auth_ShouldReturnUnauthorized_WhenCompanyNotFound() {
        when(authCompanyUseCase.execute(authCompanyDTO))
            .thenThrow(new RuntimeException("Empresa não encontrada"));

        ResponseEntity<Object> response = authCompanyController.auth(authCompanyDTO);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Empresa não encontrada", response.getBody());
        verify(authCompanyUseCase, times(1)).execute(authCompanyDTO);
    }
} 