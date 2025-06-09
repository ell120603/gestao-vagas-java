package br.com.eliel.gestao_vagas.modules.company.controllers;

import br.com.eliel.gestao_vagas.modules.company.dto.DeactivateCompanyDTO;
import br.com.eliel.gestao_vagas.modules.company.dto.UpdateCompanyDTO;
import br.com.eliel.gestao_vagas.modules.company.entites.CompanyEntity;
import br.com.eliel.gestao_vagas.modules.company.useCases.CreateCompanyUseCase;
import br.com.eliel.gestao_vagas.modules.company.useCases.DeactivateCompanyUseCase;
import br.com.eliel.gestao_vagas.modules.company.useCases.UpdateCompanyUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName; 
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*; 

class CompanyControllerTest {

    @InjectMocks
    private CompanyController companyController;

    @Mock
    private CreateCompanyUseCase createCompanyUseCase;
    @Mock
    private DeactivateCompanyUseCase deactivateCompanyUseCase;
    @Mock
    private UpdateCompanyUseCase updateCompanyUseCase;
    @Mock
    private Authentication authentication; 

    @BeforeEach
    void setUp() {
        
        MockitoAnnotations.openMocks(this);
    }

    

    @Test
    @DisplayName("Deve retornar status OK ao criar empresa com sucesso")
    void create_ReturnsOk_WhenSuccess() {
    
        CompanyEntity company = new CompanyEntity();
        company.setName("Tech Solutions Test");
        company.setUsername("techsolutionstest");
        company.setEmail("contato@techsolutionstest.com");
        company.setWebsite("https://techsolutionstest.com");
        company.setDescription("Empresa de teste especializada em software");
        when(createCompanyUseCase.execute(any(CompanyEntity.class))).thenReturn(company);

    
        ResponseEntity<Object> response = companyController.create(company);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue(), "O status deve ser 200 OK");
        assertEquals(company, response.getBody(), "O corpo da resposta deve ser a entidade da empresa criada");

        verify(createCompanyUseCase, times(1)).execute(eq(company));
        verifyNoMoreInteractions(createCompanyUseCase);
    }

    @Test
    @DisplayName("Deve retornar status BAD_REQUEST ao falhar na criação (ex: username existente)")
    void create_ReturnsBadRequest_WhenException() {
        CompanyEntity company = new CompanyEntity();
        company.setUsername("existing_username"); 

        when(createCompanyUseCase.execute(any(CompanyEntity.class))).thenThrow(new RuntimeException("Username já existe"));

        ResponseEntity<Object> response = companyController.create(company);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue(), "O status deve ser 400 BAD REQUEST");
        assertEquals("Username já existe", response.getBody(), "A mensagem de erro deve corresponder à exceção");

        verify(createCompanyUseCase, times(1)).execute(eq(company));
        verifyNoMoreInteractions(createCompanyUseCase);
    }


    @Test
    @DisplayName("Deve retornar status NO_CONTENT ao desativar empresa com sucesso")
    void deactivate_ReturnsNoContent_WhenSuccess() {
        DeactivateCompanyDTO dto = new DeactivateCompanyDTO();
        dto.setPassword("senha123456");
        UUID companyId = UUID.randomUUID();

        when(authentication.getName()).thenReturn(companyId.toString());
        doNothing().when(deactivateCompanyUseCase).execute(eq(companyId), eq("senha123456"));

        ResponseEntity<Object> response = companyController.deactivate(dto, authentication);

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCodeValue(), "O status deve ser 204 NO CONTENT");
        assertNull(response.getBody(), "O corpo da resposta deve ser nulo para 204 No Content");

        verify(authentication, times(1)).getName(); 
        verify(deactivateCompanyUseCase, times(1)).execute(eq(companyId), eq("senha123456"));
        verifyNoMoreInteractions(deactivateCompanyUseCase);
        verifyNoMoreInteractions(authentication); 
    }

    @Test
    @DisplayName("Deve retornar status BAD_REQUEST ao falhar na desativação (ex: senha incorreta)")
    void deactivate_ReturnsBadRequest_WhenException() {
        DeactivateCompanyDTO dto = new DeactivateCompanyDTO();
        dto.setPassword("wrongpass"); 
        UUID companyId = UUID.randomUUID();

        when(authentication.getName()).thenReturn(companyId.toString());
        doThrow(new RuntimeException("Senha incorreta")).when(deactivateCompanyUseCase).execute(eq(companyId), eq("wrongpass"));

        ResponseEntity<Object> response = companyController.deactivate(dto, authentication);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue(), "O status deve ser 400 BAD REQUEST");
        assertEquals("Senha incorreta", response.getBody(), "A mensagem de erro deve corresponder à exceção");

        verify(authentication, times(1)).getName();
        verify(deactivateCompanyUseCase, times(1)).execute(eq(companyId), eq("wrongpass"));
        verifyNoMoreInteractions(deactivateCompanyUseCase);
        verifyNoMoreInteractions(authentication);
    }


    @Test
    @DisplayName("Deve retornar status OK ao atualizar empresa com sucesso")
    void update_ReturnsOk_WhenSuccess() {
        UpdateCompanyDTO dto = UpdateCompanyDTO.builder()
                .name("Tech Solutions Atualizada")
                .email("novo@techsolutions.com")
                .website("https://techsolutions.com.br")
                .description("Empresa atualizada especializada em desenvolvimento de software")
                .build();
        UUID companyId = UUID.randomUUID();

        CompanyEntity updatedCompany = new CompanyEntity();
        updatedCompany.setId(companyId);
        updatedCompany.setName(dto.getName());
        updatedCompany.setEmail(dto.getEmail());
        updatedCompany.setWebsite(dto.getWebsite());
        updatedCompany.setDescription(dto.getDescription());
        updatedCompany.setUsername("original_username"); 
        when(authentication.getName()).thenReturn(companyId.toString());
        when(updateCompanyUseCase.execute(eq(companyId), any(UpdateCompanyDTO.class))).thenReturn(updatedCompany);

        ResponseEntity<Object> response = companyController.update(dto, authentication);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue(), "O status deve ser 200 OK");
        assertEquals(updatedCompany, response.getBody(), "O corpo da resposta deve ser a entidade da empresa atualizada");

        verify(authentication, times(1)).getName();
        verify(updateCompanyUseCase, times(1)).execute(eq(companyId), eq(dto));
        verifyNoMoreInteractions(updateCompanyUseCase);
        verifyNoMoreInteractions(authentication);
    }

    @Test
    @DisplayName("Deve retornar status BAD_REQUEST ao falhar na atualização")
    void update_ReturnsBadRequest_WhenException() {
        UpdateCompanyDTO dto = new UpdateCompanyDTO();
        UUID companyId = UUID.randomUUID();

        when(authentication.getName()).thenReturn(companyId.toString());
        when(updateCompanyUseCase.execute(eq(companyId), any(UpdateCompanyDTO.class)))
            .thenThrow(new RuntimeException("Dados inválidos para atualização"));

        ResponseEntity<Object> response = companyController.update(dto, authentication);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue(), "O status deve ser 400 BAD REQUEST");
        assertEquals("Dados inválidos para atualização", response.getBody(), "A mensagem de erro deve corresponder à exceção");

        verify(authentication, times(1)).getName();
        verify(updateCompanyUseCase, times(1)).execute(eq(companyId), eq(dto));
        verifyNoMoreInteractions(updateCompanyUseCase);
        verifyNoMoreInteractions(authentication);
    }
}