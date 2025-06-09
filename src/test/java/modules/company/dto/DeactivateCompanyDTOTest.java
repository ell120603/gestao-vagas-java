package br.com.eliel.gestao_vagas.modules.company.dto;

import org.junit.jupiter.api.DisplayName; 
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeactivateCompanyDTOTest {

    @Test
    @DisplayName("Deve testar a definição e recuperação da senha através de setters e getters")
    void testSetAndGetPassword() {
        DeactivateCompanyDTO dto = new DeactivateCompanyDTO();
        dto.setPassword("senhaSegura123");
        assertEquals("senhaSegura123", dto.getPassword(), "A senha deve ser 'senhaSegura123'");

        dto.setPassword("outraSenhaForte");
        assertEquals("outraSenhaForte", dto.getPassword(), "A senha deve ser atualizada para 'outraSenhaForte'");
    }

    @Test
    @DisplayName("Deve testar a igualdade e o hashcode para objetos com a mesma senha")
    void testEqualsAndHashCode_SamePassword() {
        DeactivateCompanyDTO dto1 = new DeactivateCompanyDTO();
        dto1.setPassword("senhaComparacao");
        
        DeactivateCompanyDTO dto2 = new DeactivateCompanyDTO();
        dto2.setPassword("senhaComparacao");
        
        assertEquals(dto1, dto2, "DTOs com a mesma senha devem ser iguais");
        assertEquals(dto1.hashCode(), dto2.hashCode(), "Hash codes de DTOs com a mesma senha devem ser iguais");
    }

    @Test
    @DisplayName("Deve testar a desigualdade e o hashcode para objetos com senhas diferentes")
    void testEqualsAndHashCode_DifferentPassword() {
        DeactivateCompanyDTO dto1 = new DeactivateCompanyDTO();
        dto1.setPassword("senhaOriginal");
        
        DeactivateCompanyDTO dto3 = new DeactivateCompanyDTO();
        dto3.setPassword("senhaDiferente");
        
        assertNotEquals(dto1, dto3, "DTOs com senhas diferentes não devem ser iguais");
        assertNotEquals(dto1.hashCode(), dto3.hashCode(), "Hash codes de DTOs com senhas diferentes não devem ser iguais");
    }

    @Test
    @DisplayName("Deve testar a igualdade com objeto nulo e com outra classe")
    void testEquals_WithNullAndDifferentClass() {
        DeactivateCompanyDTO dto1 = new DeactivateCompanyDTO();
        dto1.setPassword("testPassword");

        assertNotEquals(dto1, null, "Um DTO não deve ser igual a null");
        assertNotEquals(dto1, new Object(), "Um DTO não deve ser igual a uma instância de outra classe");
    }

    @Test
    @DisplayName("Deve testar a representação em string (toString) da classe")
    void testToString() {
        DeactivateCompanyDTO dto = new DeactivateCompanyDTO();
        dto.setPassword("minhaSenhaSecreta");
        String str = dto.toString();
        
        assertTrue(str.contains("password=minhaSenhaSecreta"), "A string toString deve conter a senha definida");
        assertTrue(str.startsWith("DeactivateCompanyDTO("), "A string toString deve começar com o nome da classe");
    }
}