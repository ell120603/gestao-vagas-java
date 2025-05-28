package br.com.eliel.gestao_vagas.modules.jobs.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import br.com.eliel.gestao_vagas.modules.jobs.entites.TipoContrato;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateJobDTO {
    private String titulo;
    private String descricao;
    private String areaAtuacao;
    private List<String> requisitos;
    private TipoContrato tipoContrato;
    private String localizacao;
    private BigDecimal salario;
    private String beneficios;
} 