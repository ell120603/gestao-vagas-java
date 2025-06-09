package br.com.eliel.gestao_vagas.modules.company.dto;

import br.com.eliel.gestao_vagas.modules.company.dto.AuthCompanyDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthCompanyDTOTest {

    private static final String USERNAME = "techsolutions";
    private static final String PASSWORD = "senha123456";

    @Test
    void shouldCreateAuthCompanyDTOWithAllArgsConstructor() {
        AuthCompanyDTO authCompanyDTO = new AuthCompanyDTO(PASSWORD, USERNAME);

        assertNotNull(authCompanyDTO);
        assertEquals(PASSWORD, authCompanyDTO.getPassword());
        assertEquals(USERNAME, authCompanyDTO.getUsername());
    }

    @Test
    void shouldHaveCorrectToString() {
        AuthCompanyDTO authCompanyDTO = new AuthCompanyDTO(PASSWORD, USERNAME);
        String toString = authCompanyDTO.toString();

        assertTrue(toString.contains("password=" + PASSWORD));
        assertTrue(toString.contains("username=" + USERNAME));
    }

    @Test
    void shouldHaveCorrectEqualsAndHashCode() {
        AuthCompanyDTO authCompanyDTO1 = new AuthCompanyDTO(PASSWORD, USERNAME);
        AuthCompanyDTO authCompanyDTO2 = new AuthCompanyDTO(PASSWORD, USERNAME);
        AuthCompanyDTO authCompanyDTO3 = new AuthCompanyDTO("different", "different");

        assertEquals(authCompanyDTO1, authCompanyDTO2);
        assertEquals(authCompanyDTO1.hashCode(), authCompanyDTO2.hashCode());
        assertNotEquals(authCompanyDTO1, authCompanyDTO3);
        assertNotEquals(authCompanyDTO1.hashCode(), authCompanyDTO3.hashCode());
    }

    @Test
    void shouldHandleNullValues() {
        AuthCompanyDTO authCompanyDTO = new AuthCompanyDTO(null, null);

        assertNull(authCompanyDTO.getPassword());
        assertNull(authCompanyDTO.getUsername());
    }
}
