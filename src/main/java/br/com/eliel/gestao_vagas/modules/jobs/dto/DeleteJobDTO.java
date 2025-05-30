package br.com.eliel.gestao_vagas.modules.jobs.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteJobDTO {
    @NotBlank(message = "A senha é obrigatória")
    private String password;
} 