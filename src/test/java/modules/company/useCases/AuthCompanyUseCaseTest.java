package br.com.eliel.gestao_vagas.modules.company.useCases;

import br.com.eliel.gestao_vagas.exceptions.AuthenticationException;
import br.com.eliel.gestao_vagas.modules.company.dto.AuthCompanyDTO;
import br.com.eliel.gestao_vagas.modules.company.dto.AuthCompanyResponseDTO;
import br.com.eliel.gestao_vagas.modules.company.entites.CompanyEntity;
import br.com.eliel.gestao_vagas.modules.company.repositories.CompanyRepository;
import br.com.eliel.gestao_vagas.providers.JWTProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName; 
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;
import java.util.Arrays; 

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthCompanyUseCaseTest {

    @InjectMocks 
    private AuthCompanyUseCase authCompanyUseCase;

    @Mock 
    private CompanyRepository companyRepository;
    @Mock 
    private PasswordEncoder passwordEncoder;
    @Mock 
    private JWTProvider jwtProvider;

    private AuthCompanyDTO validAuthDTO;
    private CompanyEntity activeCompany;
    private UUID companyId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        companyId = UUID.randomUUID();
        validAuthDTO = new AuthCompanyDTO("senha_valida_123", "empresa_teste_user");

        activeCompany = new CompanyEntity();
        activeCompany.setId(companyId);
        activeCompany.setUsername("empresa_teste_user");
        activeCompany.setPassword("hashedPasswordDoBanco"); 
        activeCompany.setActive(true);
    }

    @Test
    @DisplayName("Deve retornar uma resposta de autenticação quando as credenciais são válidas e a empresa está ativa")
    void execute_ReturnsResponse_WhenCredentialsAreValidAndCompanyIsActive() {
        when(companyRepository.findByUsernameOrEmail(validAuthDTO.getUsername(), validAuthDTO.getUsername()))
                .thenReturn(Optional.of(activeCompany));
        when(passwordEncoder.matches(validAuthDTO.getPassword(), activeCompany.getPassword())).thenReturn(true);
        when(jwtProvider.generateToken(companyId.toString(), Arrays.asList("COMPANY")))
                .thenReturn("token_jwt_gerado_com_sucesso");

        AuthCompanyResponseDTO response = authCompanyUseCase.execute(validAuthDTO);

        assertNotNull(response, "A resposta não deve ser nula.");
        assertEquals("token_jwt_gerado_com_sucesso", response.getToken(), "O token gerado deve ser o esperado.");
        assertEquals(companyId, response.getId(), "O ID da empresa na resposta deve corresponder.");
        assertEquals(validAuthDTO.getUsername(), response.getUsername(), "O username na resposta deve corresponder.");

        verify(companyRepository, times(1)).findByUsernameOrEmail(validAuthDTO.getUsername(), validAuthDTO.getUsername());
        verify(passwordEncoder, times(1)).matches(validAuthDTO.getPassword(), activeCompany.getPassword());
        verify(jwtProvider, times(1)).generateToken(companyId.toString(), Arrays.asList("COMPANY"));
    }

    @Test
    @DisplayName("Deve lançar AuthenticationException quando a empresa não é encontrada")
    void execute_ThrowsAuthenticationException_WhenCompanyNotFound() {
        when(companyRepository.findByUsernameOrEmail(validAuthDTO.getUsername(), validAuthDTO.getUsername()))
                .thenReturn(Optional.empty());

        assertThrows(AuthenticationException.class, () -> authCompanyUseCase.execute(validAuthDTO),
                "Deve lançar AuthenticationException quando a empresa não é encontrada.");

        verify(companyRepository, times(1)).findByUsernameOrEmail(validAuthDTO.getUsername(), validAuthDTO.getUsername());
        verifyNoInteractions(passwordEncoder, jwtProvider); 
    }

    @Test
    @DisplayName("Deve lançar AuthenticationException quando a senha não corresponde")
    void execute_ThrowsAuthenticationException_WhenPasswordDoesNotMatch() {
        when(companyRepository.findByUsernameOrEmail(validAuthDTO.getUsername(), validAuthDTO.getUsername()))
                .thenReturn(Optional.of(activeCompany)); 
        when(passwordEncoder.matches(validAuthDTO.getPassword(), activeCompany.getPassword())).thenReturn(false); 

        assertThrows(AuthenticationException.class, () -> authCompanyUseCase.execute(validAuthDTO),
                "Deve lançar AuthenticationException quando a senha não corresponde.");

        verify(companyRepository, times(1)).findByUsernameOrEmail(validAuthDTO.getUsername(), validAuthDTO.getUsername());
        verify(passwordEncoder, times(1)).matches(validAuthDTO.getPassword(), activeCompany.getPassword());
        verifyNoInteractions(jwtProvider);
    }

    @Test
    @DisplayName("Deve lançar AuthenticationException com mensagem 'Empresa desativada' quando a empresa está inativa")
    void execute_ThrowsAuthenticationException_WhenCompanyIsInactive() {
        CompanyEntity inactiveCompany = new CompanyEntity();
        inactiveCompany.setId(companyId);
        inactiveCompany.setUsername("empresa_teste_user");
        inactiveCompany.setPassword("hashedPasswordDoBanco");
        inactiveCompany.setActive(false); 

        when(companyRepository.findByUsernameOrEmail(validAuthDTO.getUsername(), validAuthDTO.getUsername()))
                .thenReturn(Optional.of(inactiveCompany));
        when(passwordEncoder.matches(validAuthDTO.getPassword(), inactiveCompany.getPassword())).thenReturn(true);

        AuthenticationException thrownException = assertThrows(AuthenticationException.class,
                () -> authCompanyUseCase.execute(validAuthDTO),
                "Deve lançar AuthenticationException quando a empresa está desativada.");
        
        assertEquals("Empresa desativada", thrownException.getMessage(), 
                     "A mensagem da exceção deve ser 'Empresa desativada'.");

        verify(companyRepository, times(1)).findByUsernameOrEmail(validAuthDTO.getUsername(), validAuthDTO.getUsername());
        verify(passwordEncoder, times(1)).matches(validAuthDTO.getPassword(), inactiveCompany.getPassword());
        verifyNoInteractions(jwtProvider);
    }
}