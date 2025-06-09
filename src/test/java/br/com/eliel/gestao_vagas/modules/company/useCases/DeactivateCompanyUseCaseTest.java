package br.com.eliel.gestao_vagas.modules.company.useCases;

import br.com.eliel.gestao_vagas.modules.company.entites.CompanyEntity;
import br.com.eliel.gestao_vagas.modules.company.repositories.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeactivateCompanyUseCaseTest {

    @InjectMocks
    private DeactivateCompanyUseCase deactivateCompanyUseCase;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private CompanyEntity company;
    private UUID companyId;
    private static final String PASSWORD = "senha123";

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
    void shouldDeactivateCompanySuccessfully() {
        when(companyRepository.findById(companyId))
            .thenReturn(Optional.of(company));
        when(passwordEncoder.matches(PASSWORD, company.getPassword()))
            .thenReturn(true);
        when(companyRepository.save(any(CompanyEntity.class)))
            .thenReturn(company);

        assertDoesNotThrow(() -> deactivateCompanyUseCase.execute(companyId, PASSWORD));

        assertFalse(company.isActive());
        verify(companyRepository).findById(companyId);
        verify(passwordEncoder).matches(PASSWORD, company.getPassword());
        verify(companyRepository).save(company);
    }

    @Test
    void shouldThrowExceptionWhenCompanyNotFound() {
        when(companyRepository.findById(companyId))
            .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> deactivateCompanyUseCase.execute(companyId, PASSWORD));

        assertEquals("Empresa não encontrada", exception.getMessage());
        assertTrue(company.isActive());

        verify(companyRepository).findById(companyId);
        verifyNoInteractions(passwordEncoder);
        verifyNoMoreInteractions(companyRepository);
    }

    @Test
    void shouldThrowExceptionWhenPasswordDoesNotMatch() {
        when(companyRepository.findById(companyId))
            .thenReturn(Optional.of(company));
        when(passwordEncoder.matches(PASSWORD, company.getPassword()))
            .thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> deactivateCompanyUseCase.execute(companyId, PASSWORD));

        assertEquals("Senha incorreta", exception.getMessage());
        assertTrue(company.isActive());

        verify(companyRepository).findById(companyId);
        verify(passwordEncoder).matches(PASSWORD, company.getPassword());
        verifyNoMoreInteractions(companyRepository);
    }

    @Test
    void shouldThrowExceptionWhenCompanyIsAlreadyInactive() {
        company.setActive(false);
        when(companyRepository.findById(companyId))
            .thenReturn(Optional.of(company));
        when(passwordEncoder.matches(PASSWORD, company.getPassword()))
            .thenReturn(true);

        assertDoesNotThrow(() -> deactivateCompanyUseCase.execute(companyId, PASSWORD));

        assertFalse(company.isActive());
        verify(companyRepository).findById(companyId);
        verify(passwordEncoder).matches(PASSWORD, company.getPassword());
        verify(companyRepository).save(company);
    }
}
