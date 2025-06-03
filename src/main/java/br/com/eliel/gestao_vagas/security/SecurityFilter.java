package br.com.eliel.gestao_vagas.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.eliel.gestao_vagas.providers.JWTProvider;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

    @Autowired
    private JWTProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            try {
                String subject = jwtProvider.validateToken(token);
                DecodedJWT decodedJWT = jwtProvider.getDecodedJWT(token);

                var roles = decodedJWT.getClaim("roles").asList(String.class);
                var authorities = roles != null ? roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .toList()
                        : Collections.<SimpleGrantedAuthority>emptyList();

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(subject, null,
                        authorities);

                SecurityContextHolder.getContext().setAuthentication(auth);

                request.setAttribute("candidate_id", subject);
                request.setAttribute("company_id", subject);
            } catch (Exception e) {
                logger.error("Erro ao processar token JWT: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
