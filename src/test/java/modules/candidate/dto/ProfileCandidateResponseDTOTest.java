package br.com.eliel.gestao_vagas.modules.candidate.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfileCandidateResponseDTOTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        ProfileCandidateResponseDTO dto = new ProfileCandidateResponseDTO();
        dto.setName("João Silva");
        dto.setUsername("joaosilva");
        dto.setEmail("joao@email.com");
        dto.setDescription("Desenvolvedor Java");

        assertEquals("João Silva", dto.getName());
        assertEquals("joaosilva", dto.getUsername());
        assertEquals("joao@email.com", dto.getEmail());
        assertEquals("Desenvolvedor Java", dto.getDescription());
    }

    @Test
    void testAllArgsConstructor() {
        ProfileCandidateResponseDTO dto = new ProfileCandidateResponseDTO(
                "João Silva", "joaosilva", "joao@email.com", "Desenvolvedor Java"
        );
        assertEquals("João Silva", dto.getName());
        assertEquals("joaosilva", dto.getUsername());
        assertEquals("joao@email.com", dto.getEmail());
        assertEquals("Desenvolvedor Java", dto.getDescription());
    }

    @Test
    void testBuilder() {
        ProfileCandidateResponseDTO dto = ProfileCandidateResponseDTO.builder()
                .name("João Silva")
                .username("joaosilva")
                .email("joao@email.com")
                .description("Desenvolvedor Java")
                .build();

        assertEquals("João Silva", dto.getName());
        assertEquals("joaosilva", dto.getUsername());
        assertEquals("joao@email.com", dto.getEmail());
        assertEquals("Desenvolvedor Java", dto.getDescription());
    }

    @Test
    void testEqualsAndHashCode() {
        ProfileCandidateResponseDTO dto1 = new ProfileCandidateResponseDTO(
                "João Silva", "joaosilva", "joao@email.com", "Desenvolvedor Java"
        );
        ProfileCandidateResponseDTO dto2 = new ProfileCandidateResponseDTO(
                "João Silva", "joaosilva", "joao@email.com", "Desenvolvedor Java"
        );
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ProfileCandidateResponseDTO dto = new ProfileCandidateResponseDTO(
                "João Silva", "joaosilva", "joao@email.com", "Desenvolvedor Java"
        );
        String str = dto.toString();
        assertTrue(str.contains("João Silva"));
        assertTrue(str.contains("joaosilva"));
        assertTrue(str.contains("joao@email.com"));
        assertTrue(str.contains("Desenvolvedor Java"));
    }
}