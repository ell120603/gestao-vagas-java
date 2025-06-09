package br.com.eliel.gestao_vagas.modules.company.useCases;

import br.com.eliel.gestao_vagas.modules.company.entites.CompanyEntity;
import br.com.eliel.gestao_vagas.modules.company.repositories.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCompanyUseCaseTest {

    @InjectMocks
    private CreateCompanyUseCase createCompanyUseCase;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private CompanyEntity company;
    private static final String ENCODED_PASSWORD = "encoded_password";

    @BeforeEach
    void setUp() {
        company = new CompanyEntity();
        company.setId(UUID.randomUUID());
        company.setName("Tech Solutions");
        company.setUsername("techsolutions");
        company.setEmail("contato@techsolutions.com");
        company.setPassword("senha123");
        company.setWebsite("https://techsolutions.com");
        company.setDescription("Empresa de tecnologia");
    }

    @Test
    void shouldCreateCompanySuccessfully() {
        when(companyRepository.findByUsernameOrEmail(anyString(), anyString()))
            .thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString()))
            .thenReturn("encoded_password");
        when(companyRepository.save(any(CompanyEntity.class)))
            .thenAnswer(invocation -> {
                CompanyEntity savedCompany = invocation.getArgument(0);
                assertEquals("encoded_password", savedCompany.getPassword());
                return savedCompany;
            });

        var result = createCompanyUseCase.execute(company);

        assertNotNull(result);
        verify(companyRepository, times(1)).findByUsernameOrEmail(anyString(), anyString());
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(companyRepository, times(1)).save(any(CompanyEntity.class));
    }

    @Test
    void shouldReturnBadRequestWhenUsernameAlreadyExists() {
        when(companyRepository.findByUsernameOrEmail(company.getUsername(), company.getEmail()))
            .thenReturn(Optional.of(company));

        ResponseEntity<Object> response = createCompanyUseCase.execute(company);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Username or email already exists", response.getBody());

        verify(companyRepository).findByUsernameOrEmail(company.getUsername(), company.getEmail());
        verifyNoInteractions(passwordEncoder);
        verifyNoMoreInteractions(companyRepository);
    }

    @Test
    void shouldReturnBadRequestWhenExceptionOccurs() {
        when(companyRepository.findByUsernameOrEmail(company.getUsername(), company.getEmail()))
            .thenThrow(new RuntimeException("Database error"));

        ResponseEntity<Object> response = createCompanyUseCase.execute(company);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Database error", response.getBody());

        verify(companyRepository).findByUsernameOrEmail(company.getUsername(), company.getEmail());
        verifyNoInteractions(passwordEncoder);
        verifyNoMoreInteractions(companyRepository);
    }
}
