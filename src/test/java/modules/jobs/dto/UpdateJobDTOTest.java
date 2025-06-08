package modules.jobs.dto;

import br.com.eliel.gestao_vagas.modules.jobs.dto.UpdateJobDTO;
import br.com.eliel.gestao_vagas.modules.jobs.entites.TipoContrato;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UpdateJobDTOTest {

    @Test
    void testBuilderAndGetters() {
        UpdateJobDTO dto = UpdateJobDTO.builder()
                .titulo("Desenvolvedor")
                .descricao("Desenvolve sistemas")
                .areaAtuacao("TI")
                .requisitos(List.of("Java", "Spring"))
                .tipoContrato(TipoContrato.CLT)
                .localizacao("SP")
                .salario(new BigDecimal("5000"))
                .beneficios("VR, VT")
                .build();

        assertEquals("Desenvolvedor", dto.getTitulo());
        assertEquals("Desenvolve sistemas", dto.getDescricao());
        assertEquals("TI", dto.getAreaAtuacao());
        assertEquals(List.of("Java", "Spring"), dto.getRequisitos());
        assertEquals(TipoContrato.CLT, dto.getTipoContrato());
        assertEquals("SP", dto.getLocalizacao());
        assertEquals(new BigDecimal("5000"), dto.getSalario());
        assertEquals("VR, VT", dto.getBeneficios());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        UpdateJobDTO dto = new UpdateJobDTO();
        dto.setTitulo("Analista");
        dto.setDescricao("Analisa sistemas");
        dto.setAreaAtuacao("RH");
        dto.setRequisitos(List.of("Excel"));
        dto.setTipoContrato(TipoContrato.PJ);
        dto.setLocalizacao("RJ");
        dto.setSalario(new BigDecimal("7000"));
        dto.setBeneficios("Plano de Saúde");

        assertEquals("Analista", dto.getTitulo());
        assertEquals("Analisa sistemas", dto.getDescricao());
        assertEquals("RH", dto.getAreaAtuacao());
        assertEquals(List.of("Excel"), dto.getRequisitos());
        assertEquals(TipoContrato.PJ, dto.getTipoContrato());
        assertEquals("RJ", dto.getLocalizacao());
        assertEquals(new BigDecimal("7000"), dto.getSalario());
        assertEquals("Plano de Saúde", dto.getBeneficios());
    }

    @Test
    void testAllArgsConstructor() {
        UpdateJobDTO dto = new UpdateJobDTO(
                "QA",
                "Testa sistemas",
                "Qualidade",
                List.of("Testes"),
                TipoContrato.CLT,
                "MG",
                new BigDecimal("4000"),
                "VR"
        );

        assertEquals("QA", dto.getTitulo());
        assertEquals("Testa sistemas", dto.getDescricao());
        assertEquals("Qualidade", dto.getAreaAtuacao());
        assertEquals(List.of("Testes"), dto.getRequisitos());
        assertEquals(TipoContrato.CLT, dto.getTipoContrato());
        assertEquals("MG", dto.getLocalizacao());
        assertEquals(new BigDecimal("4000"), dto.getSalario());
        assertEquals("VR", dto.getBeneficios());
    }

    @Test
    void testEqualsAndHashCode() {
        UpdateJobDTO dto1 = UpdateJobDTO.builder()
                .titulo("Dev")
                .descricao("Desc")
                .areaAtuacao("TI")
                .tipoContrato(TipoContrato.CLT)
                .localizacao("SP")
                .build();

        UpdateJobDTO dto2 = UpdateJobDTO.builder()
                .titulo("Dev")
                .descricao("Desc")
                .areaAtuacao("TI")
                .tipoContrato(TipoContrato.CLT)
                .localizacao("SP")
                .build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        UpdateJobDTO dto = UpdateJobDTO.builder()
                .titulo("Dev")
                .descricao("Desc")
                .areaAtuacao("TI")
                .tipoContrato(TipoContrato.CLT)
                .localizacao("SP")
                .build();

        String str = dto.toString();
        assertTrue(str.contains("Dev"));
        assertTrue(str.contains("Desc"));
        assertTrue(str.contains("TI"));
        assertTrue(str.contains("SP"));
    }
}