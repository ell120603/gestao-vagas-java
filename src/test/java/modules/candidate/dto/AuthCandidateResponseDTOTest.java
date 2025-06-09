package br.com.eliel.gestao_vagas.modules.candidate.dto;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AuthCandidateResponseDTOTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        AuthCandidateResponseDTO dto = new AuthCandidateResponseDTO();
        UUID id = UUID.randomUUID();
        dto.setToken("token123");
        dto.setId(id);
        dto.setUsername("joaosilva");

        assertEquals("token123", dto.getToken());
        assertEquals(id, dto.getId());
        assertEquals("joaosilva", dto.getUsername());
    }

    @Test
    void testAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        AuthCandidateResponseDTO dto = new AuthCandidateResponseDTO("token123", id, "joaosilva");

        assertEquals("token123", dto.getToken());
        assertEquals(id, dto.getId());
        assertEquals("joaosilva", dto.getUsername());
    }

    @Test
    void testBuilder() {
        UUID id = UUID.randomUUID();
        AuthCandidateResponseDTO dto = AuthCandidateResponseDTO.builder()
                .token("token123")
                .id(id)
                .username("joaosilva")
                .build();

        assertEquals("token123", dto.getToken());
        assertEquals(id, dto.getId());
        assertEquals("joaosilva", dto.getUsername());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        AuthCandidateResponseDTO dto1 = new AuthCandidateResponseDTO("token123", id, "joaosilva");
        AuthCandidateResponseDTO dto2 = new AuthCandidateResponseDTO("token123", id, "joaosilva");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        UUID id = UUID.randomUUID();
        AuthCandidateResponseDTO dto = new AuthCandidateResponseDTO("token123", id, "joaosilva");
        String str = dto.toString();
        assertTrue(str.contains("token123"));
        assertTrue(str.contains("joaosilva"));
        assertTrue(str.contains(id.toString()));
    }
}