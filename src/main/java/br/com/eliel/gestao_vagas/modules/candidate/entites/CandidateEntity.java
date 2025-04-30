package br.com.eliel.gestao_vagas.modules.candidate.entites;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Entity
@Table(name = "candidates")
public class CandidateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    private String name;
    
    @NotBlank
    @Pattern(regexp="\\S+",message = "o campo [username] não deve conter espaço")
    private String username;
    
    @Email(message = "o campo [email] deve conter um email valido")
    private String email;
    
    @Length(min=10,max=100,message= "A senha deve ter entre(10) e (100) caracteres")
    private String password;
    
    private String description;
    private String curriculum; //tirar do codigo
    
    @CreationTimestamp
    private LocalDateTime createdAT;
    
    private boolean active = true;
}
