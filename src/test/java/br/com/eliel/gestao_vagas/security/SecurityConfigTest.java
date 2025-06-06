package br.com.eliel.gestao_vagas.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityConfigTest {

    private SecurityConfig securityConfig;

    @Mock
    private HttpSecurity httpSecurity;

    @Mock
    private SecurityFilter securityFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        securityConfig = new SecurityConfig();
        securityConfig.securityFilter = securityFilter;
    }

    @Test
    void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    void testCorsConfigurationSource() {
        CorsConfigurationSource source = securityConfig.corsConfigurationSource();
        assertNotNull(source);
        assertTrue(source instanceof UrlBasedCorsConfigurationSource);

        UrlBasedCorsConfigurationSource urlSource = (UrlBasedCorsConfigurationSource) source;
        CorsConfiguration config = urlSource.getCorsConfigurations().get("/**");

        assertNotNull(config);
        assertEquals(List.of("*"), config.getAllowedOrigins());
        assertEquals(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"), config.getAllowedMethods());
        assertEquals(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"), config.getAllowedHeaders());
        assertEquals(List.of("Authorization"), config.getExposedHeaders());
    }

    @Test
    void testSecurityFilterChainNotNull() throws Exception {
        HttpSecurity http = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);
        when(http.csrf(any())).thenReturn(http);
        when(http.cors(any())).thenReturn(http);
        when(http.sessionManagement(any())).thenReturn(http);
        when(http.authorizeHttpRequests(any())).thenReturn(http);
        when(http.addFilterBefore(any(), any())).thenReturn(http);
        when(http.build()).thenReturn(mock(DefaultSecurityFilterChain.class));

        SecurityFilterChain chain = securityConfig.securityFilterChain(http);
        assertNotNull(chain);
    }
}
