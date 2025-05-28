package br.com.eliel.gestao_vagas.modules.jobs.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import br.com.eliel.gestao_vagas.modules.jobs.entites.TipoContrato;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobsResponseDTO {
    private UUID id;
    private String titulo;
    private String descricao;
    private String areaAtuacao;
    private List<String> requisitos;
    private TipoContrato tipoContrato;
    private String localizacao;
    private String companyName;
    private BigDecimal salario;
    private String beneficios;
    private LocalDateTime createdAt;
} 