package br.com.eliel.gestao_vagas.modules.jobs.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import br.com.eliel.gestao_vagas.modules.jobs.entites.TipoContrato;

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

    private List<String> requisitos;

    @NotNull(message = "O tipo de contrato é obrigatório")
    private TipoContrato tipoContrato;

    @NotBlank(message = "A localização é obrigatória")
    private String localizacao;

    private BigDecimal salario;
    private String beneficios;
}
