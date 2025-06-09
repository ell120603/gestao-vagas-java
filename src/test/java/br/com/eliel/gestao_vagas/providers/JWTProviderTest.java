package br.com.eliel.gestao_vagas.providers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JWTProviderTest {

    @InjectMocks
    private JWTProvider jwtProvider;

    private static final String SECRET_KEY = "secret_key_test";
    private static final String SUBJECT = "123e4567-e89b-12d3-a456-426614174000";
    private static final List<String> ROLES = Arrays.asList("COMPANY", "ADMIN");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(jwtProvider, "secretKey", SECRET_KEY);
    }

    @Test
    void generateToken_ShouldGenerateValidToken() {
        String token = jwtProvider.generateToken(SUBJECT, ROLES);

        assertNotNull(token);
        assertTrue(token.split("\\.").length == 3, "Token deve ter 3 partes separadas por ponto");

        DecodedJWT decodedJWT = JWT.decode(token);
        assertEquals("gestao_vagas", decodedJWT.getIssuer());
        assertEquals(SUBJECT, decodedJWT.getSubject());
        assertTrue(decodedJWT.getExpiresAt().toInstant().isAfter(Instant.now()));
        assertEquals(ROLES, decodedJWT.getClaim("roles").asList(String.class));
    }

    @Test
    void validateToken_ShouldReturnSubject_WhenTokenIsValid() {
        String token = jwtProvider.generateToken(SUBJECT, ROLES);

        String subject = jwtProvider.validateToken(token);

        assertEquals(SUBJECT, subject);
    }

    @Test
    void validateToken_ShouldThrowException_WhenTokenIsInvalid() {
        String invalidToken = "invalid.token.here";

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> jwtProvider.validateToken(invalidToken));

        assertEquals("Token JWT inválido ou expirado", exception.getMessage());
    }

    @Test
    void validateToken_ShouldThrowException_WhenTokenIsExpired() {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        String expiredToken = JWT.create()
            .withIssuer("gestao_vagas")
            .withSubject(SUBJECT)
            .withClaim("roles", ROLES)
            .withExpiresAt(Instant.now().minus(Duration.ofHours(1)))
            .sign(algorithm);

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> jwtProvider.validateToken(expiredToken));

        assertEquals("Token JWT inválido ou expirado", exception.getMessage());
    }

    @Test
    void getDecodedJWT_ShouldReturnDecodedJWT_WhenTokenIsValid() {
        String token = jwtProvider.generateToken(SUBJECT, ROLES);

        DecodedJWT decodedJWT = jwtProvider.getDecodedJWT(token);

        assertNotNull(decodedJWT);
        assertEquals("gestao_vagas", decodedJWT.getIssuer());
        assertEquals(SUBJECT, decodedJWT.getSubject());
        assertEquals(ROLES, decodedJWT.getClaim("roles").asList(String.class));
    }

    @Test
    void getDecodedJWT_ShouldThrowException_WhenTokenIsInvalid() {
        String invalidToken = "invalid.token.here";

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> jwtProvider.getDecodedJWT(invalidToken));

        assertEquals("Token JWT inválido ou expirado", exception.getMessage());
    }

    @Test
    void getDecodedJWT_ShouldThrowException_WhenTokenIsExpired() {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        String expiredToken = JWT.create()
            .withIssuer("gestao_vagas")
            .withSubject(SUBJECT)
            .withClaim("roles", ROLES)
            .withExpiresAt(Instant.now().minus(Duration.ofHours(1)))
            .sign(algorithm);

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> jwtProvider.getDecodedJWT(expiredToken));

        assertEquals("Token JWT inválido ou expirado", exception.getMessage());
    }
}