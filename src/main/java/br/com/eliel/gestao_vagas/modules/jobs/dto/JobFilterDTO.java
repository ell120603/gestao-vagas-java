package br.com.eliel.gestao_vagas.modules.jobs.dto;

import java.math.BigDecimal;
import java.util.List;

import br.com.eliel.gestao_vagas.modules.jobs.entites.TipoContrato;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobFilterDTO {
    private String areaAtuacao;
    private List<String> requisitos;
    private TipoContrato tipoContrato;
    private String localizacao;
    private List<String> beneficios;
    private BigDecimal salarioMinimo;
    private BigDecimal salarioMaximo;
} 