package br.com.eliel.gestao_vagas.modules.company.useCases;

import br.com.eliel.gestao_vagas.modules.company.dto.ProfileCompanyResponseDTO;
import br.com.eliel.gestao_vagas.modules.company.entites.CompanyEntity;
import br.com.eliel.gestao_vagas.modules.company.repositories.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileCompanyUseCaseTest {

    @InjectMocks
    private ProfileCompanyUseCase profileCompanyUseCase;

    @Mock
    private CompanyRepository companyRepository;

    private CompanyEntity company;
    private UUID companyId;

    @BeforeEach
    void setUp() {
        companyId = UUID.randomUUID();
        company = new CompanyEntity();
        company.setId(companyId);
        company.setName("Tech Solutions");
        company.setUsername("techsolutions");
        company.setEmail("contato@techsolutions.com");
        company.setPassword("encoded_password");
        company.setWebsite("https://techsolutions.com");
        company.setDescription("Empresa de tecnologia");
        company.setActive(true);
    }

    @Test
    void shouldReturnCompanyProfileSuccessfully() {
        when(companyRepository.findById(companyId))
            .thenReturn(Optional.of(company));

        ProfileCompanyResponseDTO response = profileCompanyUseCase.execute(companyId);

        assertNotNull(response);
        assertEquals(company.getName(), response.getName());
        assertEquals(company.getUsername(), response.getUsername());
        assertEquals(company.getEmail(), response.getEmail());
        assertEquals(company.getWebsite(), response.getWebsite());
        assertEquals(company.getDescription(), response.getDescription());

        verify(companyRepository).findById(companyId);
    }

    @Test
    void shouldThrowExceptionWhenCompanyNotFound() {
        when(companyRepository.findById(companyId))
            .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> profileCompanyUseCase.execute(companyId));

        assertEquals("Empresa não encontrada", exception.getMessage());
        verify(companyRepository).findById(companyId);
    }

    @Test
    void shouldReturnProfileWithNullFieldsWhenCompanyHasNullFields() {
        company.setWebsite(null);
        company.setDescription(null);
        when(companyRepository.findById(companyId))
            .thenReturn(Optional.of(company));

        ProfileCompanyResponseDTO response = profileCompanyUseCase.execute(companyId);

        assertNotNull(response);
        assertEquals(company.getName(), response.getName());
        assertEquals(company.getUsername(), response.getUsername());
        assertEquals(company.getEmail(), response.getEmail());
        assertNull(response.getWebsite());
        assertNull(response.getDescription());

        verify(companyRepository).findById(companyId);
    }
}
