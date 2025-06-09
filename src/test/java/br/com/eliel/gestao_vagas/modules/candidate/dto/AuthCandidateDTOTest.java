package br.com.eliel.gestao_vagas.modules.candidate.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthCandidateDTOTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        AuthCandidateDTO dto = new AuthCandidateDTO();
        dto.setUsername("joaosilva");
        dto.setPassword("senha123456");

        assertEquals("joaosilva", dto.getUsername());
        assertEquals("senha123456", dto.getPassword());
    }

    @Test
    void testBuilder() {
        AuthCandidateDTO dto = AuthCandidateDTO.builder()
                .username("joaosilva")
                .password("senha123456")
                .build();

        assertEquals("joaosilva", dto.getUsername());
        assertEquals("senha123456", dto.getPassword());
    }

    @Test
    void testAllArgsConstructor() {
        AuthCandidateDTO dto = new AuthCandidateDTO("joaosilva", "senha123456");

        assertEquals("joaosilva", dto.getUsername());
        assertEquals("senha123456", dto.getPassword());
    }

    @Test
    void testEqualsAndHashCode() {
        AuthCandidateDTO dto1 = new AuthCandidateDTO("joaosilva", "senha123456");
        AuthCandidateDTO dto2 = new AuthCandidateDTO("joaosilva", "senha123456");
        AuthCandidateDTO dto3 = new AuthCandidateDTO("maria", "senha123456");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1, dto3);
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    void testToString() {
        AuthCandidateDTO dto = new AuthCandidateDTO("joaosilva", "senha123456");
        String str = dto.toString();

        assertTrue(str.contains("joaosilva"));
        assertTrue(str.contains("senha123456"));
    }
}
