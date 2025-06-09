package br.com.eliel.gestao_vagas.modules.candidate.useCases;

import br.com.eliel.gestao_vagas.exceptions.AuthenticationException;
import br.com.eliel.gestao_vagas.modules.candidate.dto.AuthCandidateDTO;
import br.com.eliel.gestao_vagas.modules.candidate.dto.AuthCandidateResponseDTO;
import br.com.eliel.gestao_vagas.modules.candidate.entites.CandidateEntity;
import br.com.eliel.gestao_vagas.modules.candidate.repositories.CandidateRepository;
import br.com.eliel.gestao_vagas.providers.JWTProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthCandidateUseCaseTest {

    @InjectMocks
    private AuthCandidateUseCase authCandidateUseCase;

    @Mock
    private CandidateRepository candidateRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JWTProvider jwtProvider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void execute_ReturnsResponse_WhenCredentialsAreValid() {
        AuthCandidateDTO dto = new AuthCandidateDTO("joaosilva", "senha123456");
        CandidateEntity candidate = new CandidateEntity();
        UUID id = UUID.randomUUID();
        candidate.setId(id);
        candidate.setUsername("joaosilva");
        candidate.setPassword("hashedPassword");
        candidate.setActive(true);

        when(candidateRepository.findByUsernameOrEmail("joaosilva", "joaosilva"))
                .thenReturn(Optional.of(candidate));
        when(passwordEncoder.matches("senha123456", "hashedPassword")).thenReturn(true);
        when(jwtProvider.generateToken(id.toString(), java.util.Arrays.asList("CANDIDATE")))
                .thenReturn("token123");

        AuthCandidateResponseDTO response = authCandidateUseCase.execute(dto);

        assertEquals("token123", response.getToken());
        assertEquals(id, response.getId());
        assertEquals("joaosilva", response.getUsername());
    }

    @Test
    void execute_ThrowsAuthenticationException_WhenUserNotFound() {
        AuthCandidateDTO dto = new AuthCandidateDTO("joaosilva", "senha123456");
        when(candidateRepository.findByUsernameOrEmail("joaosilva", "joaosilva"))
                .thenReturn(Optional.empty());

        assertThrows(AuthenticationException.class, () -> authCandidateUseCase.execute(dto));
    }

    @Test
    void execute_ThrowsAuthenticationException_WhenPasswordDoesNotMatch() {
        AuthCandidateDTO dto = new AuthCandidateDTO("joaosilva", "senhaerrada");
        CandidateEntity candidate = new CandidateEntity();
        candidate.setId(UUID.randomUUID());
        candidate.setUsername("joaosilva");
        candidate.setPassword("hashedPassword");
        candidate.setActive(true);

        when(candidateRepository.findByUsernameOrEmail("joaosilva", "joaosilva"))
                .thenReturn(Optional.of(candidate));
        when(passwordEncoder.matches("senhaerrada", "hashedPassword")).thenReturn(false);

        assertThrows(AuthenticationException.class, () -> authCandidateUseCase.execute(dto));
    }

    @Test
    void execute_ThrowsAuthenticationException_WhenCandidateIsInactive() {
        AuthCandidateDTO dto = new AuthCandidateDTO("joaosilva", "senha123456");
        CandidateEntity candidate = new CandidateEntity();
        candidate.setId(UUID.randomUUID());
        candidate.setUsername("joaosilva");
        candidate.setPassword("hashedPassword");
        candidate.setActive(false);

        when(candidateRepository.findByUsernameOrEmail("joaosilva", "joaosilva"))
                .thenReturn(Optional.of(candidate));
        when(passwordEncoder.matches("senha123456", "hashedPassword")).thenReturn(true);

        AuthenticationException ex = assertThrows(AuthenticationException.class, () -> authCandidateUseCase.execute(dto));
        assertEquals("Candidato desativado", ex.getMessage());
    }
}