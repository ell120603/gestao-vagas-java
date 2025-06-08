package modules.jobs.dto;

import br.com.eliel.gestao_vagas.modules.jobs.dto.CandidateJobResponseDTO;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CandidateJobResponseDTOTest {

    @Test
    void testBuilderAndGetters() {
        UUID id = UUID.randomUUID();
        String name = "Renata";
        String email = "renata@email.com";
        String username = "renata123";
        String description = "Candidata a vaga";

        CandidateJobResponseDTO dto = CandidateJobResponseDTO.builder()
                .id(id)
                .name(name)
                .email(email)
                .username(username)
                .description(description)
                .build();

        assertEquals(id, dto.getId());
        assertEquals(name, dto.getName());
        assertEquals(email, dto.getEmail());
        assertEquals(username, dto.getUsername());
        assertEquals(description, dto.getDescription());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        CandidateJobResponseDTO dto = new CandidateJobResponseDTO();
        UUID id = UUID.randomUUID();

        dto.setId(id);
        dto.setName("Maria");
        dto.setEmail("maria@email.com");
        dto.setUsername("maria123");
        dto.setDescription("Descrição");

        assertEquals(id, dto.getId());
        assertEquals("Maria", dto.getName());
        assertEquals("maria@email.com", dto.getEmail());
        assertEquals("maria123", dto.getUsername());
        assertEquals("Descrição", dto.getDescription());
    }

    @Test
    void testAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        CandidateJobResponseDTO dto = new CandidateJobResponseDTO(
                id, "João", "joao@email.com", "joao123", "Descrição do João"
        );

        assertEquals(id, dto.getId());
        assertEquals("João", dto.getName());
        assertEquals("joao@email.com", dto.getEmail());
        assertEquals("joao123", dto.getUsername());
        assertEquals("Descrição do João", dto.getDescription());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        CandidateJobResponseDTO dto1 = CandidateJobResponseDTO.builder().id(id).name("A").build();
        CandidateJobResponseDTO dto2 = CandidateJobResponseDTO.builder().id(id).name("A").build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CandidateJobResponseDTO dto = CandidateJobResponseDTO.builder()
                .id(UUID.randomUUID())
                .name("Teste")
                .email("teste@email.com")
                .username("teste")
                .description("desc")
                .build();

        String str = dto.toString();
        assertTrue(str.contains("Teste"));
        assertTrue(str.contains("teste@email.com"));
        assertTrue(str.contains("desc"));
    }
}