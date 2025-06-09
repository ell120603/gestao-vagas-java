package br.com.eliel.gestao_vagas.modules.company.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthCompanyDTOTest {

    @Test
    @DisplayName("Deve testar o construtor, getters e setters da classe AuthCompanyDTO")
    void testAllArgsConstructorAndGettersSetters() {
        AuthCompanyDTO dto = new AuthCompanyDTO("senha123", "techsolutions");
        
        assertEquals("senha123", dto.getPassword(), "A senha deve ser 'senha123'");
        assertEquals("techsolutions", dto.getUsername(), "O username deve ser 'techsolutions'");

        dto.setPassword("novaSenha");
        dto.setUsername("novaempresa");
        
        assertEquals("novaSenha", dto.getPassword(), "A senha deve ser atualizada para 'novaSenha'");
        assertEquals("novaempresa", dto.getUsername(), "O username deve ser atualizado para 'novaempresa'");
    }

    @Test
    @DisplayName("Deve testar a igualdade e o hashcode de objetos AuthCompanyDTO")
    void testEqualsAndHashCode() {
        AuthCompanyDTO dto1 = new AuthCompanyDTO("senha123", "techsolutions");
        AuthCompanyDTO dto2 = new AuthCompanyDTO("senha123", "techsolutions");
        AuthCompanyDTO dto3 = new AuthCompanyDTO("outraSenha", "techsolutions"); 
        AuthCompanyDTO dto4 = new AuthCompanyDTO("senha123", "outraempresa"); 
        assertEquals(dto1, dto2, "dto1 e dto2 devem ser iguais");
        assertEquals(dto1.hashCode(), dto2.hashCode(), "Os hashCodes de dto1 e dto2 devem ser iguais");

        assertNotEquals(dto1, dto3, "dto1 e dto3 não devem ser iguais (senha diferente)");
        assertNotEquals(dto1.hashCode(), dto3.hashCode(), "Os hashCodes de dto1 e dto3 não devem ser iguais (senha diferente)");

        assertNotEquals(dto1, dto4, "dto1 e dto4 não devem ser iguais (username diferente)");
        assertNotEquals(dto1.hashCode(), dto4.hashCode(), "Os hashCodes de dto1 e dto4 não devem ser iguais (username diferente)");

        assertNotEquals(dto1, null, "Um objeto não deve ser igual a null");
        
        assertNotEquals(dto1, new Object(), "Um objeto não deve ser igual a uma instância de outra classe");
    }

    @Test
    @DisplayName("Deve testar a representação em string (toString) da classe AuthCompanyDTO")
    void testToString() {
        AuthCompanyDTO dto = new AuthCompanyDTO("senha123", "techsolutions");
        String str = dto.toString();
        
        assertTrue(str.contains("password=senha123"), "A string deve conter a senha");
        assertTrue(str.contains("username=techsolutions"), "A string deve conter o username");
        assertTrue(str.startsWith("AuthCompanyDTO("), "A string deve começar com o nome da classe");
    }
}