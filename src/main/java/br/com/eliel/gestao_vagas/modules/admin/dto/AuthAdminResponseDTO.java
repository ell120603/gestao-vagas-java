package br.com.eliel.gestao_vagas.modules.admin.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthAdminResponseDTO {
    private String token;
    private UUID id;
    private String username;
}
