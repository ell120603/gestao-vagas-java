package br.com.eliel.gestao_vagas.modules.company.entites;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Entity(name = "company")
@Data
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

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

    @CreationTimestamp
    private LocalDateTime createdAt;
    
    private boolean active = true;
}