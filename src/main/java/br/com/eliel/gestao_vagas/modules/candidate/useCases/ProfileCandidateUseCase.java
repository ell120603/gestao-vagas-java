package br.com.eliel.gestao_vagas.modules.candidate.useCases;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.eliel.gestao_vagas.modules.candidate.dto.ProfileCandidateResponseDTO;
import br.com.eliel.gestao_vagas.modules.candidate.repositories.CandidateRepository;

@Service
public class ProfileCandidateUseCase {
    
    @Autowired
    private CandidateRepository candidateRepository;
    
    public ProfileCandidateResponseDTO execute(UUID candidateId) {
        var candidate = this.candidateRepository.findById(candidateId)
            .orElseThrow(() -> {
                throw new RuntimeException("Candidato não encontrado");
            });

        var candidateDTO = ProfileCandidateResponseDTO.builder()
            .name(candidate.getName())
            .username(candidate.getUsername())
            .email(candidate.getEmail())
            .description(candidate.getDescription())
            .build();

        return candidateDTO;
    }
}
