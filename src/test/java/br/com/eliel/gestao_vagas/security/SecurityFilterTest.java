package br.com.eliel.gestao_vagas.security;

import br.com.eliel.gestao_vagas.providers.JWTProvider;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityFilterTest {

    private SecurityFilter securityFilter;

    @Mock
    private JWTProvider jwtProvider;

    @Mock
    private FilterChain filterChain;

    @Mock
    private DecodedJWT decodedJWT;

    @Mock
    private Claim rolesClaim;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        securityFilter = new SecurityFilter();
        securityFilter.jwtProvider = jwtProvider;

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        SecurityContextHolder.clearContext();
    }

    @Test
    void testDoFilterInternal_NoAuthorizationHeader() throws ServletException, IOException {
        request.addHeader("Authorization", null);

        securityFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testDoFilterInternal_ValidAuthorizationHeader() throws ServletException, IOException {
        String token = "validToken";
        request.addHeader("Authorization", "Bearer " + token);

        when(jwtProvider.validateToken(token)).thenReturn("subject");
        when(jwtProvider.getDecodedJWT(token)).thenReturn(decodedJWT);
        when(decodedJWT.getClaim("roles")).thenReturn(rolesClaim);
        when(rolesClaim.asList(String.class)).thenReturn(List.of("USER", "ADMIN"));

        securityFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);

        var auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertTrue(auth instanceof UsernamePasswordAuthenticationToken);
        assertEquals("subject", auth.getPrincipal());
        assertTrue(auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
        assertTrue(auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Test
    void testDoFilterInternal_InvalidToken() throws ServletException, IOException {
        String token = "invalidToken";
        request.addHeader("Authorization", "Bearer " + token);

        when(jwtProvider.validateToken(token)).thenThrow(new RuntimeException("Token inválido"));

        securityFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, never()).doFilter(request, response);
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
