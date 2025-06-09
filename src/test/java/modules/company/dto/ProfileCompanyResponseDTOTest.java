package br.com.eliel.gestao_vagas.modules.company.dto;

import org.junit.jupiter.api.DisplayName; 
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfileCompanyResponseDTOTest {

    @Test
    @DisplayName("Deve testar o construtor sem argumentos e os métodos setters")
    void testNoArgsConstructorAndSetters() {
        ProfileCompanyResponseDTO dto = new ProfileCompanyResponseDTO();
        
        dto.setName("Tech Solutions");
        dto.setUsername("techsolutions");
        dto.setEmail("contato@techsolutions.com");
        dto.setWebsite("https://techsolutions.com");
        dto.setDescription("Empresa especializada em desenvolvimento de software");

        assertEquals("Tech Solutions", dto.getName(), "O nome da empresa deve corresponder ao definido");
        assertEquals("techsolutions", dto.getUsername(), "O username deve corresponder ao definido");
        assertEquals("contato@techsolutions.com", dto.getEmail(), "O email deve corresponder ao definido");
        assertEquals("https://techsolutions.com", dto.getWebsite(), "O website deve corresponder ao definido");
        assertEquals("Empresa especializada em desenvolvimento de software", dto.getDescription(), "A descrição deve corresponder ao definido");
    }

    @Test
    @DisplayName("Deve testar o construtor com todos os argumentos (AllArgsConstructor)")
    void testAllArgsConstructor() {
        ProfileCompanyResponseDTO dto = new ProfileCompanyResponseDTO(
                "Tech Solutions Ltda.", "techltda", "contato@techltda.com",
                "https://techltda.com.br", "Empresa de TI e consultoria"
        );
        
        assertEquals("Tech Solutions Ltda.", dto.getName(), "O nome da empresa deve estar correto");
        assertEquals("techltda", dto.getUsername(), "O username deve estar correto");
        assertEquals("contato@techltda.com", dto.getEmail(), "O email deve estar correto");
        assertEquals("https://techltda.com.br", dto.getWebsite(), "O website deve estar correto");
        assertEquals("Empresa de TI e consultoria", dto.getDescription(), "A descrição deve estar correta");
    }

    @Test
    @DisplayName("Deve testar a criação de objetos utilizando o padrão Builder")
    void testBuilder() {
        ProfileCompanyResponseDTO dto = ProfileCompanyResponseDTO.builder()
                .name("Global Corp")
                .username("globalcorp")
                .email("info@globalcorp.com")
                .website("https://globalcorp.com")
                .description("Líder global em soluções de negócios")
                .build();

        assertEquals("Global Corp", dto.getName(), "O nome construído deve ser 'Global Corp'");
        assertEquals("globalcorp", dto.getUsername(), "O username construído deve ser 'globalcorp'");
        assertEquals("info@globalcorp.com", dto.getEmail(), "O email construído deve ser 'info@globalcorp.com'");
        assertEquals("https://globalcorp.com", dto.getWebsite(), "O website construído deve ser 'https://globalcorp.com'");
        assertEquals("Líder global em soluções de negócios", dto.getDescription(), "A descrição construída deve ser a correta");
    }

    @Test
    @DisplayName("Deve testar a igualdade e o hashcode para objetos iguais e diferentes")
    void testEqualsAndHashCode() {
        ProfileCompanyResponseDTO dto1 = new ProfileCompanyResponseDTO(
                "Empresa A", "userA", "emailA@teste.com", "siteA.com", "Desc A"
        );
        ProfileCompanyResponseDTO dto2 = new ProfileCompanyResponseDTO(
                "Empresa A", "userA", "emailA@teste.com", "siteA.com", "Desc A"
        );
        assertEquals(dto1, dto2, "DTOs com os mesmos valores devem ser iguais");
        assertEquals(dto1.hashCode(), dto2.hashCode(), "Hash codes de DTOs iguais devem ser iguais");

        ProfileCompanyResponseDTO dto3 = new ProfileCompanyResponseDTO(
                "Empresa B", "userA", "emailA@teste.com", "siteA.com", "Desc A" 
        );
        assertNotEquals(dto1, dto3, "DTOs com nome diferente não devem ser iguais");
        
        ProfileCompanyResponseDTO dto4 = new ProfileCompanyResponseDTO(
                "Empresa A", "userB", "emailA@teste.com", "siteA.com", "Desc A" 
        );
        assertNotEquals(dto1, dto4, "DTOs com username diferente não devem ser iguais");

        ProfileCompanyResponseDTO dto5 = new ProfileCompanyResponseDTO(
                "Empresa A", "userA", "emailB@teste.com", "siteA.com", "Desc A" 
        );
        assertNotEquals(dto1, dto5, "DTOs com email diferente não devem ser iguais");

        assertNotEquals(dto1, null, "Um objeto não deve ser igual a null");
        
        assertNotEquals(dto1, new Object(), "Um objeto não deve ser igual a uma instância de outra classe");
    }

    @Test
    @DisplayName("Deve testar a representação em string (toString) da classe")
    void testToString() {
        ProfileCompanyResponseDTO dto = new ProfileCompanyResponseDTO(
                "Example Company", "exampleuser", "example@company.com",
                "https://example.com", "A simple example company"
        );
        String str = dto.toString();
        
        assertTrue(str.contains("name=Example Company"), "A string deve conter o nome");
        assertTrue(str.contains("username=exampleuser"), "A string deve conter o username");
        assertTrue(str.contains("email=example@company.com"), "A string deve conter o email");
        assertTrue(str.contains("website=https://example.com"), "A string deve conter o website");
        assertTrue(str.contains("description=A simple example company"), "A string deve conter a descrição");
        assertTrue(str.startsWith("ProfileCompanyResponseDTO("), "A string deve começar com o nome da classe");
    }
}