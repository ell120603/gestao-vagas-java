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
public class ProfileCompanyResponseDTO {
    private UUID id;
    private String name;
    private String username;
    private String email;
    private String website;
    private String description;
} 