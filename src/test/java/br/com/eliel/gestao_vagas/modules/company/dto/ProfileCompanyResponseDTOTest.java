package br.com.eliel.gestao_vagas.modules.company.dto;

import br.com.eliel.gestao_vagas.modules.company.dto.ProfileCompanyResponseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfileCompanyResponseDTOTest {

    private static final String NAME = "Tech Solutions";
    private static final String USERNAME = "techsolutions";
    private static final String EMAIL = "contato@techsolutions.com";
    private static final String WEBSITE = "https://techsolutions.com";
    private static final String DESCRIPTION = "Empresa de tecnologia focada em soluções inovadoras";

    @Test
    void shouldCreateProfileCompanyResponseDTOWithBuilder() {
        ProfileCompanyResponseDTO responseDTO = ProfileCompanyResponseDTO.builder()
            .name(NAME)
            .username(USERNAME)
            .email(EMAIL)
            .website(WEBSITE)
            .description(DESCRIPTION)
            .build();

        assertNotNull(responseDTO);
        assertEquals(NAME, responseDTO.getName());
        assertEquals(USERNAME, responseDTO.getUsername());
        assertEquals(EMAIL, responseDTO.getEmail());
        assertEquals(WEBSITE, responseDTO.getWebsite());
        assertEquals(DESCRIPTION, responseDTO.getDescription());
    }

    @Test
    void shouldCreateProfileCompanyResponseDTOWithAllArgsConstructor() {
        ProfileCompanyResponseDTO responseDTO = new ProfileCompanyResponseDTO(
            NAME, USERNAME, EMAIL, WEBSITE, DESCRIPTION);

        assertNotNull(responseDTO);
        assertEquals(NAME, responseDTO.getName());
        assertEquals(USERNAME, responseDTO.getUsername());
        assertEquals(EMAIL, responseDTO.getEmail());
        assertEquals(WEBSITE, responseDTO.getWebsite());
        assertEquals(DESCRIPTION, responseDTO.getDescription());
    }

    @Test
    void shouldCreateProfileCompanyResponseDTOWithNoArgsConstructor() {
        ProfileCompanyResponseDTO responseDTO = new ProfileCompanyResponseDTO();
        responseDTO.setName(NAME);
        responseDTO.setUsername(USERNAME);
        responseDTO.setEmail(EMAIL);
        responseDTO.setWebsite(WEBSITE);
        responseDTO.setDescription(DESCRIPTION);

        assertNotNull(responseDTO);
        assertEquals(NAME, responseDTO.getName());
        assertEquals(USERNAME, responseDTO.getUsername());
        assertEquals(EMAIL, responseDTO.getEmail());
        assertEquals(WEBSITE, responseDTO.getWebsite());
        assertEquals(DESCRIPTION, responseDTO.getDescription());
    }

    @Test
    void shouldHaveCorrectToString() {
        ProfileCompanyResponseDTO responseDTO = ProfileCompanyResponseDTO.builder()
            .name(NAME)
            .username(USERNAME)
            .email(EMAIL)
            .website(WEBSITE)
            .description(DESCRIPTION)
            .build();

        String toString = responseDTO.toString();

        assertTrue(toString.contains("name=" + NAME));
        assertTrue(toString.contains("username=" + USERNAME));
        assertTrue(toString.contains("email=" + EMAIL));
        assertTrue(toString.contains("website=" + WEBSITE));
        assertTrue(toString.contains("description=" + DESCRIPTION));
    }

    @Test
    void shouldHaveCorrectEqualsAndHashCode() {
        ProfileCompanyResponseDTO responseDTO1 = ProfileCompanyResponseDTO.builder()
            .name(NAME)
            .username(USERNAME)
            .email(EMAIL)
            .website(WEBSITE)
            .description(DESCRIPTION)
            .build();

        ProfileCompanyResponseDTO responseDTO2 = ProfileCompanyResponseDTO.builder()
            .name(NAME)
            .username(USERNAME)
            .email(EMAIL)
            .website(WEBSITE)
            .description(DESCRIPTION)
            .build();

        ProfileCompanyResponseDTO responseDTO3 = ProfileCompanyResponseDTO.builder()
            .name("Different")
            .username("different")
            .email("different@email.com")
            .website("https://different.com")
            .description("Different description")
            .build();

        assertEquals(responseDTO1, responseDTO2);
        assertEquals(responseDTO1.hashCode(), responseDTO2.hashCode());
        assertNotEquals(responseDTO1, responseDTO3);
        assertNotEquals(responseDTO1.hashCode(), responseDTO3.hashCode());
    }

    @Test
    void shouldHandleNullValues() {
        ProfileCompanyResponseDTO responseDTO = new ProfileCompanyResponseDTO(null, null, null, null, null);

        assertNull(responseDTO.getName());
        assertNull(responseDTO.getUsername());
        assertNull(responseDTO.getEmail());
        assertNull(responseDTO.getWebsite());
        assertNull(responseDTO.getDescription());
    }
} 