package br.com.eliel.gestao_vagas.modules.candidate.dto;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileCandidateResponseDTO {
    private String name;
    private String username;
    private String email;
    private String description;
}
