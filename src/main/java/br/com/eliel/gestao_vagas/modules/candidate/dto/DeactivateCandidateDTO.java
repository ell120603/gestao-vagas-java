package br.com.eliel.gestao_vagas.modules.candidate.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeactivateCandidateDTO {
    @NotBlank(message = "A senha é obrigatória")
    private String password;
}
