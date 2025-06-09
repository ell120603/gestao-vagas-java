package br.com.eliel.gestao_vagas.modules.company.controllers;

import br.com.eliel.gestao_vagas.modules.company.dto.AuthCompanyDTO;
import br.com.eliel.gestao_vagas.modules.company.useCases.AuthCompanyUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName; 
import org.junit.jupiter.api.Test;
import org.mockito.*; 
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*; 

class AuthCompanyControllerTest {

    @InjectMocks
    private AuthCompanyController authCompanyController;

    @Mock
    private AuthCompanyUseCase authCompanyUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve retornar status OK quando a autenticação for bem-sucedida")
    void auth_ReturnsOk_WhenSuccess() {
        AuthCompanyDTO dto = new AuthCompanyDTO();
        dto.setUsername("techsolutions");
        dto.setPassword("senha123456");

        Object responseMock = new Object(); 

        when(authCompanyUseCase.execute(any(AuthCompanyDTO.class))).thenReturn(responseMock);

        ResponseEntity<Object> response = authCompanyController.auth(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "O status HTTP deve ser 200 OK");
        assertEquals(responseMock, response.getBody(), "O corpo da resposta deve ser o retornado pelo Use Case");

        verify(authCompanyUseCase, times(1)).execute(eq(dto));
        verifyNoMoreInteractions(authCompanyUseCase);
    }

    @Test
    @DisplayName("Deve retornar status UNAUTHORIZED quando a autenticação falhar")
    void auth_ReturnsUnauthorized_WhenException() {
        AuthCompanyDTO dto = new AuthCompanyDTO();
        dto.setUsername("techsolutions");
        dto.setPassword("wrongpass");

        when(authCompanyUseCase.execute(any(AuthCompanyDTO.class)))
            .thenThrow(new RuntimeException("Usuário ou senha incorretos"));

        ResponseEntity<Object> response = authCompanyController.auth(dto);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(), "O status HTTP deve ser 401 UNAUTHORIZED");
        assertEquals("Usuário ou senha incorretos", response.getBody(), "A mensagem de erro deve corresponder à mensagem da exceção");

        verify(authCompanyUseCase, times(1)).execute(eq(dto));
        verifyNoMoreInteractions(authCompanyUseCase);
    }
}