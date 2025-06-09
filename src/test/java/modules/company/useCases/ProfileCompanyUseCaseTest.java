package br.com.eliel.gestao_vagas.modules.company.useCases;

import br.com.eliel.gestao_vagas.modules.company.dto.ProfileCompanyResponseDTO;
import br.com.eliel.gestao_vagas.modules.company.entites.CompanyEntity;
import br.com.eliel.gestao_vagas.modules.company.repositories.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfileCompanyUseCaseTest {

    @InjectMocks
    private ProfileCompanyUseCase profileCompanyUseCase;

    @Mock
    private CompanyRepository companyRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void execute_ReturnsProfile_WhenCompanyExists() {
        UUID companyId = UUID.randomUUID();
        CompanyEntity company = new CompanyEntity();
        company.setId(companyId);
        company.setName("Tech Solutions");
        company.setUsername("techsolutions");
        company.setEmail("contato@techsolutions.com");
        company.setWebsite("https://techsolutions.com");
        company.setDescription("Empresa especializada em desenvolvimento de software");

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));

        ProfileCompanyResponseDTO result = profileCompanyUseCase.execute(companyId);

        assertEquals("Tech Solutions", result.getName());
        assertEquals("techsolutions", result.getUsername());
        assertEquals("contato@techsolutions.com", result.getEmail());
        assertEquals("https://techsolutions.com", result.getWebsite());
        assertEquals("Empresa especializada em desenvolvimento de software", result.getDescription());
    }

    @Test
    void execute_ThrowsException_WhenCompanyNotFound() {
        UUID companyId = UUID.randomUUID();
        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> profileCompanyUseCase.execute(companyId));
        assertEquals("Empresa não encontrada", ex.getMessage());
    }
}