import br.com.eliel.gestao_vagas.modules.candidate.dto.AuthCandidateDTO;
import br.com.eliel.gestao_vagas.modules.candidate.useCases.AuthCandidateUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Java
package br.com.eliel.gestao_vagas.modules.candidate.controllers;



class AuthCandidateControllerTest {

    @InjectMocks
    private AuthCandidateController authCandidateController;

    @Mock
    private AuthCandidateUseCase authCandidateUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthSuccess() {
        AuthCandidateDTO dto = new AuthCandidateDTO();
        // Set fields if needed, e.g. dto.setUsername("user"); dto.setPassword("pass");
        Object expectedResult = new Object(); // Replace with actual expected result type if known

        when(authCandidateUseCase.execute(dto)).thenReturn(expectedResult);

        ResponseEntity<Object> response = authCandidateController.auth(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
        verify(authCandidateUseCase).execute(dto);
    }

    @Test
    void testAuthFailure() {
        AuthCandidateDTO dto = new AuthCandidateDTO();
        String errorMsg = "Usuário ou senha incorretos";

        when(authCandidateUseCase.execute(dto)).thenThrow(new RuntimeException(errorMsg));

        ResponseEntity<Object> response = authCandidateController.auth(dto);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(errorMsg, response.getBody());
        verify(authCandidateUseCase).execute(dto);
    }
}