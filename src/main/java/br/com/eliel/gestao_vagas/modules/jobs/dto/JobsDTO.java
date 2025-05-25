package br.com.eliel.gestao_vagas.modules.jobs.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobsDTO {
    @NotBlank(message = "O título é obrigatório")
    private String titulo;

    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    @NotBlank(message = "A área de atuação é obrigatória")
    private String areaAtuacao;

    private String[] tecnologias;

    @NotBlank(message = "O tipo de contrato é obrigatório")
    @Pattern(regexp = "CLT|PJ|TEMPORARIO|ESTAGIO", message = "O tipo de contrato deve ser CLT, PJ, TEMPORARIO ou ESTAGIO")
    private String tipoContrato;

    @NotBlank(message = "A localização é obrigatória")
    private String localizacao;

    private BigDecimal salario;
    private String beneficios;
}
