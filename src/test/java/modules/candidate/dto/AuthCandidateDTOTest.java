package br.com.eliel.gestao_vagas.modules.candidate.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthCandidateDTOTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        AuthCandidateDTO dto = new AuthCandidateDTO();
        dto.setUsername("joaosilva");
        dto.setPassword("senha123");

        assertEquals("joaosilva", dto.getUsername());
        assertEquals("senha123", dto.getPassword());
    }

    @Test
    void testAllArgsConstructor() {
        AuthCandidateDTO dto = new AuthCandidateDTO("joaosilva", "senha123");
        assertEquals("joaosilva", dto.getUsername());
        assertEquals("senha123", dto.getPassword());
    }

    @Test
    void testBuilder() {
        AuthCandidateDTO dto = AuthCandidateDTO.builder()
                .username("joaosilva")
                .password("senha123")
                .build();

        assertEquals("joaosilva", dto.getUsername());
        assertEquals("senha123", dto.getPassword());
    }

    @Test
    void testEqualsAndHashCode() {
        AuthCandidateDTO dto1 = new AuthCandidateDTO("joaosilva", "senha123");
        AuthCandidateDTO dto2 = new AuthCandidateDTO("joaosilva", "senha123");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AuthCandidateDTO dto = new AuthCandidateDTO("joaosilva", "senha123");
        String str = dto.toString();
        assertTrue(str.contains("joaosilva"));
        assertTrue(str.contains("senha123"));
    }
}