package br.com.eliel.gestao_vagas.modules.candidate.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfileCandidateResponseDTOTest {

    private static final String NAME = "João Silva";
    private static final String USERNAME = "joaosilva";
    private static final String EMAIL = "joao@email.com";
    private static final String DESCRIPTION = "Desenvolvedor Java";

    @Test
    void shouldCreateProfileCandidateResponseDTOWithBuilder() {
        ProfileCandidateResponseDTO responseDTO = ProfileCandidateResponseDTO.builder()
            .name(NAME)
            .username(USERNAME)
            .email(EMAIL)
            .description(DESCRIPTION)
            .build();

        assertNotNull(responseDTO);
        assertEquals(NAME, responseDTO.getName());
        assertEquals(USERNAME, responseDTO.getUsername());
        assertEquals(EMAIL, responseDTO.getEmail());
        assertEquals(DESCRIPTION, responseDTO.getDescription());
    }

    @Test
    void shouldCreateProfileCandidateResponseDTOWithAllArgsConstructor() {
        ProfileCandidateResponseDTO responseDTO = new ProfileCandidateResponseDTO(
            NAME, USERNAME, EMAIL, DESCRIPTION);

        assertNotNull(responseDTO);
        assertEquals(NAME, responseDTO.getName());
        assertEquals(USERNAME, responseDTO.getUsername());
        assertEquals(EMAIL, responseDTO.getEmail());
        assertEquals(DESCRIPTION, responseDTO.getDescription());
    }

    @Test
    void shouldCreateProfileCandidateResponseDTOWithNoArgsConstructor() {
        ProfileCandidateResponseDTO responseDTO = new ProfileCandidateResponseDTO();
        responseDTO.setName(NAME);
        responseDTO.setUsername(USERNAME);
        responseDTO.setEmail(EMAIL);
        responseDTO.setDescription(DESCRIPTION);

        assertNotNull(responseDTO);
        assertEquals(NAME, responseDTO.getName());
        assertEquals(USERNAME, responseDTO.getUsername());
        assertEquals(EMAIL, responseDTO.getEmail());
        assertEquals(DESCRIPTION, responseDTO.getDescription());
    }

    @Test
    void shouldHaveCorrectToString() {
        ProfileCandidateResponseDTO responseDTO = ProfileCandidateResponseDTO.builder()
            .name(NAME)
            .username(USERNAME)
            .email(EMAIL)
            .description(DESCRIPTION)
            .build();

        String toString = responseDTO.toString();

        assertTrue(toString.contains("name=" + NAME));
        assertTrue(toString.contains("username=" + USERNAME));
        assertTrue(toString.contains("email=" + EMAIL));
        assertTrue(toString.contains("description=" + DESCRIPTION));
    }

    @Test
    void shouldHaveCorrectEqualsAndHashCode() {
        ProfileCandidateResponseDTO dto1 = ProfileCandidateResponseDTO.builder()
            .name(NAME)
            .username(USERNAME)
            .email(EMAIL)
            .description(DESCRIPTION)
            .build();

        ProfileCandidateResponseDTO dto2 = ProfileCandidateResponseDTO.builder()
            .name(NAME)
            .username(USERNAME)
            .email(EMAIL)
            .description(DESCRIPTION)
            .build();

        ProfileCandidateResponseDTO dto3 = ProfileCandidateResponseDTO.builder()
            .name("Maria Silva")
            .username("mariasilva")
            .email("maria@email.com")
            .description("Desenvolvedora Java")
            .build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1, dto3);
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }
}
