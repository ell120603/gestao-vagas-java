package modules.jobs.dto;

import br.com.eliel.gestao_vagas.modules.jobs.dto.JobFilterDTO;
import br.com.eliel.gestao_vagas.modules.jobs.entites.TipoContrato;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JobFilterDTOTest {

    @Test
    void testBuilderAndGetters() {
        JobFilterDTO dto = JobFilterDTO.builder()
                .areaAtuacao("TI")
                .requisitos(List.of("Java", "Spring"))
                .tipoContrato(TipoContrato.CLT)
                .localizacao("SP")
                .beneficios(List.of("VR", "VT"))
                .salarioMinimo(new BigDecimal("2000"))
                .salarioMaximo(new BigDecimal("5000"))
                .build();

        assertEquals("TI", dto.getAreaAtuacao());
        assertEquals(List.of("Java", "Spring"), dto.getRequisitos());
        assertEquals(TipoContrato.CLT, dto.getTipoContrato());
        assertEquals("SP", dto.getLocalizacao());
        assertEquals(List.of("VR", "VT"), dto.getBeneficios());
        assertEquals(new BigDecimal("2000"), dto.getSalarioMinimo());
        assertEquals(new BigDecimal("5000"), dto.getSalarioMaximo());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        JobFilterDTO dto = new JobFilterDTO();
        dto.setAreaAtuacao("RH");
        dto.setRequisitos(List.of("Excel"));
        dto.setTipoContrato(TipoContrato.PJ);
        dto.setLocalizacao("RJ");
        dto.setBeneficios(List.of("Plano de Saúde"));
        dto.setSalarioMinimo(new BigDecimal("3000"));
        dto.setSalarioMaximo(new BigDecimal("7000"));

        assertEquals("RH", dto.getAreaAtuacao());
        assertEquals(List.of("Excel"), dto.getRequisitos());
        assertEquals(TipoContrato.PJ, dto.getTipoContrato());
        assertEquals("RJ", dto.getLocalizacao());
        assertEquals(List.of("Plano de Saúde"), dto.getBeneficios());
        assertEquals(new BigDecimal("3000"), dto.getSalarioMinimo());
        assertEquals(new BigDecimal("7000"), dto.getSalarioMaximo());
    }

    @Test
    void testAllArgsConstructor() {
        JobFilterDTO dto = new JobFilterDTO(
                "Engenharia",
                List.of("AutoCAD"),
                TipoContrato.CLT,
                "MG",
                List.of("VR"),
                new BigDecimal("4000"),
                new BigDecimal("9000")
        );

        assertEquals("Engenharia", dto.getAreaAtuacao());
        assertEquals(List.of("AutoCAD"), dto.getRequisitos());
        assertEquals(TipoContrato.CLT, dto.getTipoContrato());
        assertEquals("MG", dto.getLocalizacao());
        assertEquals(List.of("VR"), dto.getBeneficios());
        assertEquals(new BigDecimal("4000"), dto.getSalarioMinimo());
        assertEquals(new BigDecimal("9000"), dto.getSalarioMaximo());
    }

    @Test
    void testEqualsAndHashCode() {
        JobFilterDTO dto1 = JobFilterDTO.builder()
                .areaAtuacao("TI")
                .build();
        JobFilterDTO dto2 = JobFilterDTO.builder()
                .areaAtuacao("TI")
                .build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        JobFilterDTO dto = JobFilterDTO.builder()
                .areaAtuacao("TI")
                .localizacao("SP")
                .build();

        String str = dto.toString();
        assertTrue(str.contains("TI"));
        assertTrue(str.contains("SP"));
    }
}