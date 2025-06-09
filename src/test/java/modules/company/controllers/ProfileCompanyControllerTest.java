package br.com.eliel.gestao_vagas.modules.company.controllers;

import br.com.eliel.gestao_vagas.modules.company.dto.ProfileCompanyResponseDTO;
import br.com.eliel.gestao_vagas.modules.company.useCases.ProfileCompanyUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName; 
import org.junit.jupiter.api.Test;
import org.mockito.*; 
import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve retornar status OK e o perfil da empresa quando a busca for bem-sucedida")
    void profile_ReturnsOk_WhenSuccess() {
        UUID companyId = UUID.randomUUID();
        ProfileCompanyResponseDTO expectedDto = new ProfileCompanyResponseDTO();
        expectedDto.setId(companyId);
        expectedDto.setName("Tech Solutions");
        expectedDto.setUsername("techsolutions");
        expectedDto.setEmail("contato@techsolutions.com");
        expectedDto.setWebsite("https://techsolutions.com");
        expectedDto.setDescription("Empresa especializada em desenvolvimento de software");
        expectedDto.setCreatedAt("2024-03-20T10:00:00Z");

        when(request.getAttribute("company_id")).thenReturn(companyId.toString());
        when(profileCompanyUseCase.execute(eq(companyId))).thenReturn(expectedDto);

        ResponseEntity<ProfileCompanyResponseDTO> response = profileCompanyController.profile(request);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue(), "O status HTTP deve ser 200 OK");
        assertEquals(expectedDto, response.getBody(), "O corpo da resposta deve ser o DTO do perfil da empresa");

        verify(request, times(1)).getAttribute("company_id");
        verify(profileCompanyUseCase, times(1)).execute(eq(companyId));
        verifyNoMoreInteractions(request);
        verifyNoMoreInteractions(profileCompanyUseCase);
    }

    @Test
    @DisplayName("Deve lançar NullPointerException quando 'company_id' for nulo no request")
    void profile_ThrowsNullPointerException_WhenCompanyIdIsNull() {
        when(request.getAttribute("company_id")).thenReturn(null);

        assertThrows(NullPointerException.class, () -> {
            profileCompanyController.profile(request);
        }, "Deve lançar NullPointerException se 'company_id' for nulo");

        verify(request, times(1)).getAttribute("company_id");
        verify(profileCompanyUseCase, never()).execute(any(UUID.class));
        verifyNoMoreInteractions(request);
        verifyNoMoreInteractions(profileCompanyUseCase);
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando 'company_id' não for um UUID válido")
    void profile_ThrowsIllegalArgumentException_WhenCompanyIdIsInvalid() {
        when(request.getAttribute("company_id")).thenReturn("nao-eh-um-uuid-valido");

        assertThrows(IllegalArgumentException.class, () -> {
            profileCompanyController.profile(request);
        }, "Deve lançar IllegalArgumentException se 'company_id' não for um UUID válido");

        verify(request, times(1)).getAttribute("company_id");
        verify(profileCompanyUseCase, never()).execute(any(UUID.class));
        verifyNoMoreInteractions(request);
        verifyNoMoreInteractions(profileCompanyUseCase);
    }

    @Test
    @DisplayName("Deve propagar exceção do Use Case se a empresa não for encontrada")
    void profile_PropagatesException_WhenCompanyNotFoundInUseCase() {
        UUID companyId = UUID.randomUUID();
        when(request.getAttribute("company_id")).thenReturn(companyId.toString());
        when(profileCompanyUseCase.execute(eq(companyId))).thenThrow(new RuntimeException("Empresa não encontrada"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            profileCompanyController.profile(request);
        }, "Deve propagar a exceção do Use Case");

        assertEquals("Empresa não encontrada", thrown.getMessage(), "A mensagem da exceção deve ser a do Use Case");

        verify(request, times(1)).getAttribute("company_id");
        verify(profileCompanyUseCase, times(1)).execute(eq(companyId));
        verifyNoMoreInteractions(request);
        verifyNoMoreInteractions(profileCompanyUseCase);
    }
}