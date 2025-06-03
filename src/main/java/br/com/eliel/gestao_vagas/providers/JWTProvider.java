package br.com.eliel.gestao_vagas.providers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class JWTProvider {

    private static final Logger logger = LoggerFactory.getLogger(JWTProvider.class);

    @Value("${security.token.secret}")
    private String secretKey;

    public String generateToken(String subject, List<String> roles) {
        try {
            logger.info("Generating token for subject: {} with roles: {}", subject, roles);
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            String token = JWT.create()
                    .withIssuer("gestao_vagas")
                    .withSubject(subject)
                    .withClaim("roles", roles)
                    .withExpiresAt(Instant.now().plus(Duration.ofHours(2)))
                    .sign(algorithm);
            logger.info("Token generated successfully");
            return token;
        } catch (Exception e) {
            logger.error("Error generating token: {}", e.getMessage());
            throw new RuntimeException("Erro ao gerar token JWT", e);
        }
    }

    public String validateToken(String token) {
        try {
            logger.info("Validating token");
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            String subject = JWT.require(algorithm)
                    .withIssuer("gestao_vagas")
                    .build()
                    .verify(token)
                    .getSubject();
            logger.info("Token validated successfully for subject: {}", subject);
            return subject;
        } catch (JWTVerificationException e) {
            logger.error("Token validation failed: {}", e.getMessage());
            throw new RuntimeException("Token JWT inválido ou expirado");
        }
    }

    public DecodedJWT getDecodedJWT(String token) {
        try {
            logger.info("Decoding JWT token");
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .withIssuer("gestao_vagas")
                    .build()
                    .verify(token);
            logger.info("Token decoded successfully");
            return decodedJWT;
        } catch (JWTVerificationException e) {
            logger.error("Token decoding failed: {}", e.getMessage());
            throw new RuntimeException("Token JWT inválido ou expirado");
        }
    }
}
