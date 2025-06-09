package br.com.eliel.gestao_vagas.modules.company.useCases;

import br.com.eliel.gestao_vagas.modules.company.dto.UpdateCompanyDTO;
import br.com.eliel.gestao_vagas.modules.company.entites.CompanyEntity;
import br.com.eliel.gestao_vagas.modules.company.repositories.CompanyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateCompanyUseCaseTest {

    @InjectMocks
    private UpdateCompanyUseCase updateCompanyUseCase;

    @Mock
    private CompanyRepository companyRepository;

    private CompanyEntity company;
    private UpdateCompanyDTO updateCompanyDTO;
    private UUID companyId;

    @BeforeEach
    void setUp() {
        companyId = UUID.randomUUID();
        company = new CompanyEntity();
        company.setId(companyId);
        company.setName("Tech Solutions");
        company.setUsername("techsolutions");
        company.setEmail("contato@techsolutions.com");
        company.setWebsite("https://techsolutions.com");
        company.setDescription("Empresa de tecnologia");

        updateCompanyDTO = new UpdateCompanyDTO();
    }

    @Test
    void shouldUpdateCompanySuccessfully() {
        updateCompanyDTO.setName("New Tech Solutions");
        updateCompanyDTO.setUsername("newtechsolutions");
        updateCompanyDTO.setEmail("novo@techsolutions.com");
        updateCompanyDTO.setWebsite("https://newtechsolutions.com");
        updateCompanyDTO.setDescription("Nova descrição da empresa");

        when(companyRepository.findById(companyId))
            .thenReturn(Optional.of(company));
        when(companyRepository.save(any(CompanyEntity.class)))
            .thenReturn(company);

        CompanyEntity updatedCompany = updateCompanyUseCase.execute(companyId, updateCompanyDTO);

        assertNotNull(updatedCompany);
        assertEquals(updateCompanyDTO.getName(), updatedCompany.getName());
        assertEquals(updateCompanyDTO.getUsername(), updatedCompany.getUsername());
        assertEquals(updateCompanyDTO.getEmail(), updatedCompany.getEmail());
        assertEquals(updateCompanyDTO.getWebsite(), updatedCompany.getWebsite());
        assertEquals(updateCompanyDTO.getDescription(), updatedCompany.getDescription());

        verify(companyRepository).findById(companyId);
        verify(companyRepository).save(company);
    }

    @Test
    void shouldThrowExceptionWhenCompanyNotFound() {
        when(companyRepository.findById(companyId))
            .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
            () -> updateCompanyUseCase.execute(companyId, updateCompanyDTO));

        assertEquals("Empresa não encontrada", exception.getMessage());
        verify(companyRepository).findById(companyId);
        verifyNoMoreInteractions(companyRepository);
    }

    @Test
    void shouldUpdateOnlyProvidedFields() {
        updateCompanyDTO.setName("New Tech Solutions");
        updateCompanyDTO.setEmail("novo@techsolutions.com");

        when(companyRepository.findById(companyId))
            .thenReturn(Optional.of(company));
        when(companyRepository.save(any(CompanyEntity.class)))
            .thenReturn(company);

        CompanyEntity updatedCompany = updateCompanyUseCase.execute(companyId, updateCompanyDTO);

        assertNotNull(updatedCompany);
        assertEquals(updateCompanyDTO.getName(), updatedCompany.getName());
        assertEquals(updateCompanyDTO.getEmail(), updatedCompany.getEmail());
        assertEquals(company.getUsername(), updatedCompany.getUsername());
        assertEquals(company.getWebsite(), updatedCompany.getWebsite());
        assertEquals(company.getDescription(), updatedCompany.getDescription());

        verify(companyRepository).findById(companyId);
        verify(companyRepository).save(company);
    }

    @Test
    void shouldNotUpdateWhenDTOIsEmpty() {
        when(companyRepository.findById(companyId))
            .thenReturn(Optional.of(company));
        when(companyRepository.save(any(CompanyEntity.class)))
            .thenReturn(company);

        CompanyEntity updatedCompany = updateCompanyUseCase.execute(companyId, updateCompanyDTO);

        assertNotNull(updatedCompany);
        assertEquals(company.getName(), updatedCompany.getName());
        assertEquals(company.getUsername(), updatedCompany.getUsername());
        assertEquals(company.getEmail(), updatedCompany.getEmail());
        assertEquals(company.getWebsite(), updatedCompany.getWebsite());
        assertEquals(company.getDescription(), updatedCompany.getDescription());

        verify(companyRepository).findById(companyId);
        verify(companyRepository).save(company);
    }
}
