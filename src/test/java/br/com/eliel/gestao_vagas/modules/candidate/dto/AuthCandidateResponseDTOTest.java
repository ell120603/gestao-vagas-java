package br.com.eliel.gestao_vagas.modules.candidate.dto;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AuthCandidateResponseDTOTest {

    private static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ";
    private static final String USERNAME = "joaosilva";

    @Test
    void shouldCreateAuthCandidateResponseDTOWithBuilder() {
        UUID id = UUID.randomUUID();
        AuthCandidateResponseDTO responseDTO = AuthCandidateResponseDTO.builder()
            .token(TOKEN)
            .id(id)
            .username(USERNAME)
            .build();

        assertNotNull(responseDTO);
        assertEquals(TOKEN, responseDTO.getToken());
        assertEquals(id, responseDTO.getId());
        assertEquals(USERNAME, responseDTO.getUsername());
    }

    @Test
    void shouldCreateAuthCandidateResponseDTOWithAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        AuthCandidateResponseDTO responseDTO = new AuthCandidateResponseDTO(TOKEN, id, USERNAME);

        assertNotNull(responseDTO);
        assertEquals(TOKEN, responseDTO.getToken());
        assertEquals(id, responseDTO.getId());
        assertEquals(USERNAME, responseDTO.getUsername());
    }

    @Test
    void shouldCreateAuthCandidateResponseDTOWithNoArgsConstructor() {
        UUID id = UUID.randomUUID();
        AuthCandidateResponseDTO responseDTO = new AuthCandidateResponseDTO();
        responseDTO.setToken(TOKEN);
        responseDTO.setId(id);
        responseDTO.setUsername(USERNAME);

        assertNotNull(responseDTO);
        assertEquals(TOKEN, responseDTO.getToken());
        assertEquals(id, responseDTO.getId());
        assertEquals(USERNAME, responseDTO.getUsername());
    }

    @Test
    void shouldHaveCorrectToString() {
        UUID id = UUID.randomUUID();
        AuthCandidateResponseDTO responseDTO = AuthCandidateResponseDTO.builder()
            .token(TOKEN)
            .id(id)
            .username(USERNAME)
            .build();

        String toString = responseDTO.toString();

        assertTrue(toString.contains("token=" + TOKEN));
        assertTrue(toString.contains("id=" + id));
        assertTrue(toString.contains("username=" + USERNAME));
    }

    @Test
    void shouldHaveCorrectEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        AuthCandidateResponseDTO dto1 = AuthCandidateResponseDTO.builder()
            .token(TOKEN)
            .id(id)
            .username(USERNAME)
            .build();

        AuthCandidateResponseDTO dto2 = AuthCandidateResponseDTO.builder()
            .token(TOKEN)
            .id(id)
            .username(USERNAME)
            .build();

        AuthCandidateResponseDTO dto3 = AuthCandidateResponseDTO.builder()
            .token("outro_token")
            .id(UUID.randomUUID())
            .username("outro_usuario")
            .build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1, dto3);
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }
}
