package br.com.eliel.gestao_vagas.modules.company.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateCompanyDTO {
    @NotBlank(message = "O nome da empresa é obrigatório")
    private String name;

    @NotBlank(message = "O username é obrigatório")
    @Pattern(regexp="\\S+", message = "O username não deve conter espaço")
    private String username;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O email deve ser válido")
    private String email;
    
    @NotBlank(message = "A senha é obrigatória")
    @Length(min=10, max=100, message = "A senha deve ter entre 10 e 100 caracteres")
    private String password;

    private String website;
    private String description;
} 