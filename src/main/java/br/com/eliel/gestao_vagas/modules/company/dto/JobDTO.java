package br.com.eliel.gestao_vagas.modules.company.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobDTO {
    private String title;
    private String description;
    private String benefits;
    private String level;
    private UUID companyId;
}
