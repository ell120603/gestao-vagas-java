package br.com.eliel.gestao_vagas.modules.admin.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
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
public class AdminUpdateCandidateDTO {
    
    private String name;
    
    @Pattern(regexp="\\S+", message = "O campo [username] não deve conter espaço")
    private String username;
    
    @Email(message = "O campo [email] deve conter um email válido")
    private String email;
    
    @Length(min=10, max=100, message = "A senha deve ter entre (10) e (100) caracteres")
    private String password;
    
    private String description;
}
