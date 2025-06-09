package br.com.eliel.gestao_vagas.modules.candidate.useCases;

import br.com.eliel.gestao_vagas.exceptions.AuthenticationException;
import br.com.eliel.gestao_vagas.modules.candidate.dto.AuthCandidateDTO;
import br.com.eliel.gestao_vagas.modules.candidate.dto.AuthCandidateResponseDTO;
import br.com.eliel.gestao_vagas.modules.candidate.entites.CandidateEntity;
import br.com.eliel.gestao_vagas.modules.candidate.repositories.CandidateRepository;
import br.com.eliel.gestao_vagas.modules.candidate.useCases.AuthCandidateUseCase;
import br.com.eliel.gestao_vagas.providers.JWTProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthCandidateUseCaseTest {

    @Mock
    private CandidateRepository candidateRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JWTProvider jwtProvider;

    @InjectMocks
    private AuthCandidateUseCase authCandidateUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void execute_ShouldReturnResponse_WhenAuthenticationIsSuccessful() {
        AuthCandidateDTO dto = new AuthCandidateDTO();
        dto.setUsername("joaosilva");
        dto.setPassword("senha123");
        CandidateEntity candidate = new CandidateEntity();
        candidate.setId(UUID.randomUUID());
        candidate.setUsername("joaosilva");
        candidate.setPassword("senha123");
        candidate.setActive(true);

        when(candidateRepository.findByUsernameOrEmail("joaosilva", "joaosilva")).thenReturn(Optional.of(candidate));
        when(passwordEncoder.matches("senha123", "senha123")).thenReturn(true);
        when(jwtProvider.generateToken(anyString(), anyList())).thenReturn("token-jwt");

        AuthCandidateResponseDTO response = authCandidateUseCase.execute(dto);

        assertNotNull(response);
        assertEquals(candidate.getId(), response.getId());
        assertEquals(candidate.getUsername(), response.getUsername());
        assertEquals("token-jwt", response.getToken());
        verify(candidateRepository, times(1)).findByUsernameOrEmail("joaosilva", "joaosilva");
        verify(passwordEncoder, times(1)).matches("senha123", "senha123");
        verify(jwtProvider, times(1)).generateToken(anyString(), anyList());
    }

    @Test
    void execute_ShouldThrowException_WhenPasswordIsIncorrect() {
        AuthCandidateDTO dto = new AuthCandidateDTO();
        dto.setUsername("joaosilva");
        dto.setPassword("senhaErrada");
        CandidateEntity candidate = new CandidateEntity();
        candidate.setId(UUID.randomUUID());
        candidate.setUsername("joaosilva");
        candidate.setPassword("senha123");
        candidate.setActive(true);

        when(candidateRepository.findByUsernameOrEmail("joaosilva", "joaosilva")).thenReturn(Optional.of(candidate));
        when(passwordEncoder.matches("senhaErrada", "senha123")).thenReturn(false);

        assertThrows(AuthenticationException.class, () -> authCandidateUseCase.execute(dto));
        verify(candidateRepository, times(1)).findByUsernameOrEmail("joaosilva", "joaosilva");
        verify(passwordEncoder, times(1)).matches("senhaErrada", "senha123");
    }

    @Test
    void execute_ShouldThrowException_WhenCandidateNotFound() {
        AuthCandidateDTO dto = new AuthCandidateDTO();
        dto.setUsername("naoexiste");
        dto.setPassword("senha123");

        when(candidateRepository.findByUsernameOrEmail("naoexiste", "naoexiste")).thenReturn(Optional.empty());

        assertThrows(AuthenticationException.class, () -> authCandidateUseCase.execute(dto));
        verify(candidateRepository, times(1)).findByUsernameOrEmail("naoexiste", "naoexiste");
    }

    @Test
    void execute_ShouldThrowException_WhenCandidateIsInactive() {
        AuthCandidateDTO dto = new AuthCandidateDTO();
        dto.setUsername("joaosilva");
        dto.setPassword("senha123");
        CandidateEntity candidate = new CandidateEntity();
        candidate.setId(UUID.randomUUID());
        candidate.setUsername("joaosilva");
        candidate.setPassword("senha123");
        candidate.setActive(false);

        when(candidateRepository.findByUsernameOrEmail("joaosilva", "joaosilva")).thenReturn(Optional.of(candidate));
        when(passwordEncoder.matches("senha123", "senha123")).thenReturn(true);

        assertThrows(AuthenticationException.class, () -> authCandidateUseCase.execute(dto));
        verify(candidateRepository, times(1)).findByUsernameOrEmail("joaosilva", "joaosilva");
        verify(passwordEncoder, times(1)).matches("senha123", "senha123");
    }
}
