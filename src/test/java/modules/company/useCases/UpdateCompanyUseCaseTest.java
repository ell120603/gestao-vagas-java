package br.com.eliel.gestao_vagas.modules.company.useCases;

import br.com.eliel.gestao_vagas.modules.company.dto.UpdateCompanyDTO;
import br.com.eliel.gestao_vagas.modules.company.entites.CompanyEntity;
import br.com.eliel.gestao_vagas.modules.company.repositories.CompanyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateCompanyUseCaseTest {

    @InjectMocks
    private UpdateCompanyUseCase updateCompanyUseCase;

    @Mock
    private CompanyRepository companyRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void execute_UpdatesCompany_WhenFieldsArePresent() {
        UUID companyId = UUID.randomUUID();
        CompanyEntity company = new CompanyEntity();
        company.setId(companyId);
        company.setName("Old Name");
        company.setUsername("olduser");
        company.setEmail("old@email.com");
        company.setWebsite("https://old.com");
        company.setDescription("Old desc");

        UpdateCompanyDTO dto = UpdateCompanyDTO.builder()
                .name("New Name")
                .username("newuser")
                .email("new@email.com")
                .website("https://new.com")
                .description("New desc")
                .build();

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(companyRepository.save(any(CompanyEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CompanyEntity updated = updateCompanyUseCase.execute(companyId, dto);

        assertEquals("New Name", updated.getName());
        assertEquals("newuser", updated.getUsername());
        assertEquals("new@email.com", updated.getEmail());
        assertEquals("https://new.com", updated.getWebsite());
        assertEquals("New desc", updated.getDescription());
    }

    @Test
    void execute_UpdatesOnlyNonNullFields() {
        UUID companyId = UUID.randomUUID();
        CompanyEntity company = new CompanyEntity();
        company.setId(companyId);
        company.setName("Old Name");
        company.setUsername("olduser");
        company.setEmail("old@email.com");
        company.setWebsite("https://old.com");
        company.setDescription("Old desc");

        UpdateCompanyDTO dto = UpdateCompanyDTO.builder()
                .name(null)
                .username("newuser")
                .email(null)
                .website(null)
                .description("New desc")
                .build();

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(companyRepository.save(any(CompanyEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CompanyEntity updated = updateCompanyUseCase.execute(companyId, dto);

        assertEquals("Old Name", updated.getName());
        assertEquals("newuser", updated.getUsername());
        assertEquals("old@email.com", updated.getEmail());
        assertEquals("https://old.com", updated.getWebsite());
        assertEquals("New desc", updated.getDescription());
    }

    @Test
    void execute_ThrowsException_WhenCompanyNotFound() {
        UUID companyId = UUID.randomUUID();
        UpdateCompanyDTO dto = new UpdateCompanyDTO();

        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                updateCompanyUseCase.execute(companyId, dto));
        assertEquals("Empresa não encontrada", ex.getMessage());
    }
}