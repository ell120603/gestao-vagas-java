package br.com.eliel.gestao_vagas.modules.company.controllers;

import br.com.eliel.gestao_vagas.modules.company.controllers.CompanyController;
import br.com.eliel.gestao_vagas.modules.company.dto.DeactivateCompanyDTO;
import br.com.eliel.gestao_vagas.modules.company.dto.UpdateCompanyDTO;
import br.com.eliel.gestao_vagas.modules.company.entites.CompanyEntity;
import br.com.eliel.gestao_vagas.modules.company.useCases.CreateCompanyUseCase;
import br.com.eliel.gestao_vagas.modules.company.useCases.DeactivateCompanyUseCase;
import br.com.eliel.gestao_vagas.modules.company.useCases.UpdateCompanyUseCase;
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

class CompanyControllerTest {

    @InjectMocks
    private CompanyController companyController;

    @Mock
    private CreateCompanyUseCase createCompanyUseCase;

    @Mock
    private UpdateCompanyUseCase updateCompanyUseCase;

    @Mock
    private DeactivateCompanyUseCase deactivateCompanyUseCase;

    @Mock
    private Authentication authentication;

    private CompanyEntity company;
    private UpdateCompanyDTO updateCompanyDTO;
    private DeactivateCompanyDTO deactivateCompanyDTO;
    private UUID companyId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        companyId = UUID.randomUUID();
        
        company = new CompanyEntity();
        company.setId(companyId);
        company.setName("Tech Solutions");
        company.setUsername("techsolutions");
        company.setEmail("contato@techsolutions.com");
        company.setPassword("senha123456");
        company.setWebsite("https://techsolutions.com");
        company.setDescription("Empresa de tecnologia");
        company.setActive(true);

        updateCompanyDTO = new UpdateCompanyDTO();
        updateCompanyDTO.setName("Tech Solutions Atualizada");
        updateCompanyDTO.setEmail("novo@techsolutions.com");
        updateCompanyDTO.setWebsite("https://novo.techsolutions.com");
        updateCompanyDTO.setDescription("Nova descrição");

        deactivateCompanyDTO = new DeactivateCompanyDTO();
        deactivateCompanyDTO.setPassword("senha123456");
    }

    @Test
    void create_ShouldReturnOk_WhenCompanyIsCreated() {
        when(createCompanyUseCase.execute(any(CompanyEntity.class)))
            .thenReturn(ResponseEntity.ok().body(company));

        ResponseEntity<Object> response = companyController.create(company);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(createCompanyUseCase, times(1)).execute(company);
    }

    @Test
    void create_ShouldReturnBadRequest_WhenCompanyAlreadyExists() {
        when(createCompanyUseCase.execute(any(CompanyEntity.class)))
            .thenReturn(ResponseEntity.badRequest().body("Username já existe"));

        ResponseEntity<Object> response = companyController.create(company);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Username já existe", response.getBody());
        verify(createCompanyUseCase, times(1)).execute(company);
    }

    @Test
    void update_ShouldReturnOk_WhenCompanyIsUpdated() {
        when(authentication.getName()).thenReturn(companyId.toString());
        when(updateCompanyUseCase.execute(any(UUID.class), any(UpdateCompanyDTO.class))).thenReturn(company);

        ResponseEntity<Object> response = companyController.update(updateCompanyDTO, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(updateCompanyUseCase, times(1)).execute(companyId, updateCompanyDTO);
    }

    @Test
    void update_ShouldReturnBadRequest_WhenCompanyNotFound() {
        when(authentication.getName()).thenReturn(companyId.toString());
        when(updateCompanyUseCase.execute(any(UUID.class), any(UpdateCompanyDTO.class)))
            .thenThrow(new RuntimeException("Empresa não encontrada"));

        ResponseEntity<Object> response = companyController.update(updateCompanyDTO, authentication);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Empresa não encontrada", response.getBody());
        verify(updateCompanyUseCase, times(1)).execute(companyId, updateCompanyDTO);
    }

    @Test
    void deactivate_ShouldReturnNoContent_WhenCompanyIsDeactivated() {
        when(authentication.getName()).thenReturn(companyId.toString());
        doNothing().when(deactivateCompanyUseCase).execute(any(UUID.class), anyString());

        ResponseEntity<Object> response = companyController.deactivate(deactivateCompanyDTO, authentication);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(deactivateCompanyUseCase, times(1)).execute(companyId, deactivateCompanyDTO.getPassword());
    }

    @Test
    void deactivate_ShouldReturnBadRequest_WhenPasswordIsIncorrect() {
        when(authentication.getName()).thenReturn(companyId.toString());
        doThrow(new RuntimeException("Senha incorreta"))
            .when(deactivateCompanyUseCase).execute(any(UUID.class), anyString());

        ResponseEntity<Object> response = companyController.deactivate(deactivateCompanyDTO, authentication);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Senha incorreta", response.getBody());
        verify(deactivateCompanyUseCase, times(1)).execute(companyId, deactivateCompanyDTO.getPassword());
    }
}
