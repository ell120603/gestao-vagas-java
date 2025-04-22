package br.com.eliel.gestao_vagas.modules.candidate.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.eliel.gestao_vagas.modules.candidate.CandidateRepository;
import br.com.eliel.gestao_vagas.modules.candidate.dto.ProfileCandidateResponseDTO;

import java.util.UUID;

@Service
public class ProfileCandidateUseCase {
    
    @Autowired
    private CandidateRepository candidateRepository;
    
    public ProfileCandidateResponseDTO execute(UUID candidateId) {
        var candidate = this.candidateRepository.findById(candidateId)
            .orElseThrow(() -> {
                throw new UsernameNotFoundException("Candidato não encontrado");
            });
            
        var candidateDTO = ProfileCandidateResponseDTO.builder()
            .id(candidate.getId())
            .name(candidate.getName())
            .username(candidate.getUsername())
            .email(candidate.getEmail())
            .description(candidate.getDescription())
            .build();
            
        return candidateDTO;
    }
}
