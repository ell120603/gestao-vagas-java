package modules.jobs.dto;

import br.com.eliel.gestao_vagas.modules.jobs.dto.JobsResponseDTO;
import br.com.eliel.gestao_vagas.modules.jobs.entites.TipoContrato;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JobsResponseDTOTest {

    @Test
    void testBuilderAndGetters() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        JobsResponseDTO dto = JobsResponseDTO.builder()
                .id(id)
                .titulo("Desenvolvedor")
                .descricao("Desenvolve sistemas")
                .areaAtuacao("TI")
                .requisitos(List.of("Java", "Spring"))
                .tipoContrato(TipoContrato.CLT)
                .localizacao("SP")
                .companyName("Empresa X")
                .salario(new BigDecimal("5000"))
                .beneficios("VR, VT")
                .createdAt(now)
                .build();

        assertEquals(id, dto.getId());
        assertEquals("Desenvolvedor", dto.getTitulo());
        assertEquals("Desenvolve sistemas", dto.getDescricao());
        assertEquals("TI", dto.getAreaAtuacao());
        assertEquals(List.of("Java", "Spring"), dto.getRequisitos());
        assertEquals(TipoContrato.CLT, dto.getTipoContrato());
        assertEquals("SP", dto.getLocalizacao());
        assertEquals("Empresa X", dto.getCompanyName());
        assertEquals(new BigDecimal("5000"), dto.getSalario());
        assertEquals("VR, VT", dto.getBeneficios());
        assertEquals(now, dto.getCreatedAt());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        JobsResponseDTO dto = new JobsResponseDTO();
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        dto.setId(id);
        dto.setTitulo("Analista");
        dto.setDescricao("Analisa sistemas");
        dto.setAreaAtuacao("RH");
        dto.setRequisitos(List.of("Excel"));
        dto.setTipoContrato(TipoContrato.PJ);
        dto.setLocalizacao("RJ");
        dto.setCompanyName("Empresa Y");
        dto.setSalario(new BigDecimal("7000"));
        dto.setBeneficios("Plano de Saúde");
        dto.setCreatedAt(now);

        assertEquals(id, dto.getId());
        assertEquals("Analista", dto.getTitulo());
        assertEquals("Analisa sistemas", dto.getDescricao());
        assertEquals("RH", dto.getAreaAtuacao());
        assertEquals(List.of("Excel"), dto.getRequisitos());
        assertEquals(TipoContrato.PJ, dto.getTipoContrato());
        assertEquals("RJ", dto.getLocalizacao());
        assertEquals("Empresa Y", dto.getCompanyName());
        assertEquals(new BigDecimal("7000"), dto.getSalario());
        assertEquals("Plano de Saúde", dto.getBeneficios());
        assertEquals(now, dto.getCreatedAt());
    }

    @Test
    void testAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        JobsResponseDTO dto = new JobsResponseDTO(
                id,
                "QA",
                "Testa sistemas",
                "Qualidade",
                List.of("Testes"),
                TipoContrato.CLT,
                "MG",
                "Empresa Z",
                new BigDecimal("4000"),
                "VR",
                now
        );

        assertEquals(id, dto.getId());
        assertEquals("QA", dto.getTitulo());
        assertEquals("Testa sistemas", dto.getDescricao());
        assertEquals("Qualidade", dto.getAreaAtuacao());
        assertEquals(List.of("Testes"), dto.getRequisitos());
        assertEquals(TipoContrato.CLT, dto.getTipoContrato());
        assertEquals("MG", dto.getLocalizacao());
        assertEquals("Empresa Z", dto.getCompanyName());
        assertEquals(new BigDecimal("4000"), dto.getSalario());
        assertEquals("VR", dto.getBeneficios());
        assertEquals(now, dto.getCreatedAt());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        JobsResponseDTO dto1 = JobsResponseDTO.builder()
                .id(id)
                .titulo("Dev")
                .build();
        JobsResponseDTO dto2 = JobsResponseDTO.builder()
                .id(id)
                .titulo("Dev")
                .build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        JobsResponseDTO dto = JobsResponseDTO.builder()
                .titulo("Dev")
                .descricao("Desc")
                .areaAtuacao("TI")
                .localizacao("SP")
                .companyName("Empresa")
                .build();

        String str = dto.toString();
        assertTrue(str.contains("Dev"));
        assertTrue(str.contains("Desc"));
        assertTrue(str.contains("TI"));
        assertTrue(str.contains("SP"));
        assertTrue(str.contains("Empresa"));
    }
}