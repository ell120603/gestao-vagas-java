package modules.jobs.entites;

import br.com.eliel.gestao_vagas.modules.jobs.entites.TipoContrato;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TipoContratoTest {

    @Test
    void testValues() {
        TipoContrato[] values = TipoContrato.values();
        assertEquals(4, values.length);
        assertArrayEquals(
                new TipoContrato[]{TipoContrato.CLT, TipoContrato.PJ, TipoContrato.TEMPORARIO, TipoContrato.ESTAGIO},
                values
        );
    }

    @Test
    void testValueOf() {
        assertEquals(TipoContrato.CLT, TipoContrato.valueOf("CLT"));
        assertEquals(TipoContrato.PJ, TipoContrato.valueOf("PJ"));
        assertEquals(TipoContrato.TEMPORARIO, TipoContrato.valueOf("TEMPORARIO"));
        assertEquals(TipoContrato.ESTAGIO, TipoContrato.valueOf("ESTAGIO"));
    }
}