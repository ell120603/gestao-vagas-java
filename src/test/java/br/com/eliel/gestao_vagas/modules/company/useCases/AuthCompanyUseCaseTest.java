package br.com.eliel.gestao_vagas.modules.company.useCases;

import br.com.eliel.gestao_vagas.exceptions.AuthenticationException;
import br.com.eliel.gestao_vagas.modules.company.dto.AuthCompanyDTO;
import br.com.eliel.gestao_vagas.modules.company.dto.AuthCompanyResponseDTO;
import br.com.eliel.gestao_vagas.modules.company.entites.CompanyEntity;
import br.com.eliel.gestao_vagas.modules.company.repositories.CompanyRepository;
import br.com.eliel.gestao_vagas.providers.JWTProvider;
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
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthCompanyUseCaseTest {

    @InjectMocks
    private AuthCompanyUseCase authCompanyUseCase;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTProvider jwtProvider;

    private CompanyEntity company;
    private AuthCompanyDTO authCompanyDTO;
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";

    @BeforeEach
    void setUp() {
        company = new CompanyEntity();
        company.setId(UUID.randomUUID());
        company.setName("Tech Solutions");
        company.setUsername("techsolutions");
        company.setEmail("contato@techsolutions.com");
        company.setPassword("encoded_password");
        company.setWebsite("https://techsolutions.com");
        company.setDescription("Empresa de tecnologia");
        company.setActive(true);

        authCompanyDTO = new AuthCompanyDTO("senha123", "techsolutions");
    }

    @Test
    void shouldAuthenticateCompanyWithValidCredentials() {
        when(companyRepository.findByUsernameOrEmail(anyString(), anyString()))
            .thenReturn(Optional.of(company));
        when(passwordEncoder.matches(anyString(), anyString()))
            .thenReturn(true);
        when(jwtProvider.generateToken(anyString(), anyList()))
            .thenReturn("valid_token");

        var result = authCompanyUseCase.execute(authCompanyDTO);

        assertNotNull(result);
        assertEquals("valid_token", result.getToken());
        assertEquals(company.getId(), result.getId());
        assertEquals(company.getUsername(), result.getUsername());
        verify(companyRepository, times(1)).findByUsernameOrEmail(anyString(), anyString());
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
        verify(jwtProvider, times(1)).generateToken(anyString(), anyList());
    }

    @Test
    void shouldThrowExceptionWhenCompanyNotFound() {
        when(companyRepository.findByUsernameOrEmail(authCompanyDTO.getUsername(), authCompanyDTO.getUsername()))
            .thenReturn(Optional.empty());

        assertThrows(AuthenticationException.class, () -> authCompanyUseCase.execute(authCompanyDTO));

        verify(companyRepository).findByUsernameOrEmail(authCompanyDTO.getUsername(), authCompanyDTO.getUsername());
        verifyNoInteractions(passwordEncoder, jwtProvider);
    }

    @Test
    void shouldThrowExceptionWhenPasswordDoesNotMatch() {
        when(companyRepository.findByUsernameOrEmail(authCompanyDTO.getUsername(), authCompanyDTO.getUsername()))
            .thenReturn(Optional.of(company));
        when(passwordEncoder.matches(authCompanyDTO.getPassword(), company.getPassword()))
            .thenReturn(false);

        assertThrows(AuthenticationException.class, () -> authCompanyUseCase.execute(authCompanyDTO));

        verify(companyRepository).findByUsernameOrEmail(authCompanyDTO.getUsername(), authCompanyDTO.getUsername());
        verify(passwordEncoder).matches(authCompanyDTO.getPassword(), company.getPassword());
        verifyNoInteractions(jwtProvider);
    }

    @Test
    void shouldThrowExceptionWhenCompanyIsInactive() {
        company.setActive(false);
        when(companyRepository.findByUsernameOrEmail(authCompanyDTO.getUsername(), authCompanyDTO.getUsername()))
            .thenReturn(Optional.of(company));
        when(passwordEncoder.matches(authCompanyDTO.getPassword(), company.getPassword()))
            .thenReturn(true);

        AuthenticationException exception = assertThrows(AuthenticationException.class,
            () -> authCompanyUseCase.execute(authCompanyDTO));

        assertEquals("Empresa desativada", exception.getMessage());

        verify(companyRepository).findByUsernameOrEmail(authCompanyDTO.getUsername(), authCompanyDTO.getUsername());
        verify(passwordEncoder).matches(authCompanyDTO.getPassword(), company.getPassword());
        verifyNoInteractions(jwtProvider);
    }
} 