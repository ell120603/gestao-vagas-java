package br.com.eliel.gestao_vagas.modules.company.dto;

import br.com.eliel.gestao_vagas.modules.company.dto.AuthCompanyResponseDTO;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AuthCompanyResponseDTOTest {

    private static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";
    private static final UUID ID = UUID.randomUUID();
    private static final String USERNAME = "techsolutions";

    @Test
    void shouldCreateAuthCompanyResponseDTOWithBuilder() {
        AuthCompanyResponseDTO responseDTO = AuthCompanyResponseDTO.builder()
            .token(TOKEN)
            .id(ID)
            .username(USERNAME)
            .build();

        assertNotNull(responseDTO);
        assertEquals(TOKEN, responseDTO.getToken());
        assertEquals(ID, responseDTO.getId());
        assertEquals(USERNAME, responseDTO.getUsername());
    }

    @Test
    void shouldCreateAuthCompanyResponseDTOWithAllArgsConstructor() {
        AuthCompanyResponseDTO responseDTO = new AuthCompanyResponseDTO(TOKEN, ID, USERNAME);

        assertNotNull(responseDTO);
        assertEquals(TOKEN, responseDTO.getToken());
        assertEquals(ID, responseDTO.getId());
        assertEquals(USERNAME, responseDTO.getUsername());
    }

    @Test
    void shouldCreateAuthCompanyResponseDTOWithNoArgsConstructor() {
        AuthCompanyResponseDTO responseDTO = new AuthCompanyResponseDTO();
        responseDTO.setToken(TOKEN);
        responseDTO.setId(ID);
        responseDTO.setUsername(USERNAME);

        assertNotNull(responseDTO);
        assertEquals(TOKEN, responseDTO.getToken());
        assertEquals(ID, responseDTO.getId());
        assertEquals(USERNAME, responseDTO.getUsername());
    }

    @Test
    void shouldHaveCorrectToString() {
        AuthCompanyResponseDTO responseDTO = AuthCompanyResponseDTO.builder()
            .token(TOKEN)
            .id(ID)
            .username(USERNAME)
            .build();

        String toString = responseDTO.toString();

        assertTrue(toString.contains("token=" + TOKEN));
        assertTrue(toString.contains("id=" + ID));
        assertTrue(toString.contains("username=" + USERNAME));
    }

    @Test
    void shouldHaveCorrectEqualsAndHashCode() {
        AuthCompanyResponseDTO responseDTO1 = AuthCompanyResponseDTO.builder()
            .token(TOKEN)
            .id(ID)
            .username(USERNAME)
            .build();

        AuthCompanyResponseDTO responseDTO2 = AuthCompanyResponseDTO.builder()
            .token(TOKEN)
            .id(ID)
            .username(USERNAME)
            .build();

        AuthCompanyResponseDTO responseDTO3 = AuthCompanyResponseDTO.builder()
            .token("different")
            .id(UUID.randomUUID())
            .username("different")
            .build();

        assertEquals(responseDTO1, responseDTO2);
        assertEquals(responseDTO1.hashCode(), responseDTO2.hashCode());
        assertNotEquals(responseDTO1, responseDTO3);
        assertNotEquals(responseDTO1.hashCode(), responseDTO3.hashCode());
    }

    @Test
    void shouldHandleNullValues() {
        AuthCompanyResponseDTO responseDTO = new AuthCompanyResponseDTO(null, null, null);

        assertNull(responseDTO.getToken());
        assertNull(responseDTO.getId());
        assertNull(responseDTO.getUsername());
    }
}
