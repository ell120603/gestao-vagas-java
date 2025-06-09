package br.com.eliel.gestao_vagas.modules.company.dto;

import org.junit.jupiter.api.DisplayName; 
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AuthCompanyResponseDTOTest {

    @Test
    @DisplayName("Deve testar o construtor sem argumentos e os métodos setters")
    void testNoArgsConstructorAndSetters() {
        AuthCompanyResponseDTO dto = new AuthCompanyResponseDTO();
        UUID id = UUID.randomUUID();
        
        dto.setToken("token123");
        dto.setId(id);
        dto.setUsername("techsolutions");

        assertEquals("token123", dto.getToken(), "O token deve ser 'token123'");
        assertEquals(id, dto.getId(), "O ID deve ser o UUID gerado");
        assertEquals("techsolutions", dto.getUsername(), "O username deve ser 'techsolutions'");
    }

    @Test
    @DisplayName("Deve testar o construtor com todos os argumentos")
    void testAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        AuthCompanyResponseDTO dto = new AuthCompanyResponseDTO("token123", id, "techsolutions");

        assertEquals("token123", dto.getToken(), "O token deve ser 'token123'");
        assertEquals(id, dto.getId(), "O ID deve ser o UUID gerado");
        assertEquals("techsolutions", dto.getUsername(), "O username deve ser 'techsolutions'");
    }

    @Test
    @DisplayName("Deve testar a criação de objetos utilizando o padrão Builder")
    void testBuilder() {
        UUID id = UUID.randomUUID();
        AuthCompanyResponseDTO dto = AuthCompanyResponseDTO.builder()
                .token("token123")
                .id(id)
                .username("techsolutions")
                .build();

        assertEquals("token123", dto.getToken(), "O token construído deve ser 'token123'");
        assertEquals(id, dto.getId(), "O ID construído deve ser o UUID gerado");
        assertEquals("techsolutions", dto.getUsername(), "O username construído deve ser 'techsolutions'");
    }

    @Test
    @DisplayName("Deve testar a igualdade e o hashcode para objetos iguais e diferentes")
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        AuthCompanyResponseDTO dto1 = new AuthCompanyResponseDTO("token123", id, "techsolutions");
        AuthCompanyResponseDTO dto2 = new AuthCompanyResponseDTO("token123", id, "techsolutions"); 
        AuthCompanyResponseDTO dto3 = new AuthCompanyResponseDTO("token456", id, "techsolutions"); 
        AuthCompanyResponseDTO dto4 = new AuthCompanyResponseDTO("token123", UUID.randomUUID(), "techsolutions"); 
        AuthCompanyResponseDTO dto5 = new AuthCompanyResponseDTO("token123", id, "otheruser"); 

        assertEquals(dto1, dto2, "dto1 e dto2 devem ser considerados iguais");
        assertEquals(dto1.hashCode(), dto2.hashCode(), "Os hashCodes de dto1 e dto2 devem ser iguais");

        assertNotEquals(dto1, dto3, "dto1 e dto3 não devem ser iguais (token diferente)");
        assertNotEquals(dto1, dto4, "dto1 e dto4 não devem ser iguais (ID diferente)");
        assertNotEquals(dto1, dto5, "dto1 e dto5 não devem ser iguais (username diferente)");
        assertNotEquals(dto1, null, "Um objeto não deve ser igual a null");
        assertNotEquals(dto1, new Object(), "Um objeto não deve ser igual a uma instância de outra classe");
    }

    @Test
    @DisplayName("Deve testar a representação em string (toString) da classe")
    void testToString() {
        UUID id = UUID.randomUUID();
        AuthCompanyResponseDTO dto = new AuthCompanyResponseDTO("token123", id, "techsolutions");
        String str = dto.toString();
        
        assertTrue(str.contains("token=token123"), "A string deve conter o token");
        assertTrue(str.contains("username=techsolutions"), "A string deve conter o username");
        assertTrue(str.contains("id=" + id.toString()), "A string deve conter o ID");
        assertTrue(str.startsWith("AuthCompanyResponseDTO("), "A string deve começar com o nome da classe");
    }
}