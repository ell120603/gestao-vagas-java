package br.com.eliel.gestao_vagas.modules.jobs.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateJobResponseDTO {
    private UUID id;
    private String name;
    private String email;
    private String username;
    private String description;
} 