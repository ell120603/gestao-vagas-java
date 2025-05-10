package br.com.eliel.gestao_vagas.modules.company.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeactivateCompanyDTO {
    @NotBlank(message = "A senha é obrigatória")
    private String password;
} 