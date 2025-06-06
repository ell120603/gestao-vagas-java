package br.com.eliel.gestao_vagas.modules.candidate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCandidateDTO {
    
    private String name;
    
    @Pattern(regexp="\\S+", message = "O username não deve conter espaço")
    private String username;
    
    @Email(message = "O email deve ser válido")
    private String email;
    
    private String description;
} 