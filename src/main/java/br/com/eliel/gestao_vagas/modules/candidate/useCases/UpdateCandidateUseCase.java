package br.com.eliel.gestao_vagas.modules.candidate.useCases;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.eliel.gestao_vagas.modules.candidate.dto.UpdateCandidateDTO;
import br.com.eliel.gestao_vagas.modules.candidate.entites.CandidateEntity;
import br.com.eliel.gestao_vagas.modules.candidate.repositories.CandidateRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UpdateCandidateUseCase {

    @Autowired
    private CandidateRepository candidateRepository;
    
    public CandidateEntity execute(UUID candidateId, UpdateCandidateDTO updateCandidateDTO) {
        CandidateEntity candidate = this.candidateRepository.findById(candidateId)
                .orElseThrow(() -> new EntityNotFoundException("Candidato não encontrado"));
        
        if (updateCandidateDTO.getUsername() != null || updateCandidateDTO.getEmail() != null) {
            var existingCandidate = this.candidateRepository.findByUsernameOrEmail(
                updateCandidateDTO.getUsername() != null ? updateCandidateDTO.getUsername() : candidate.getUsername(),
                updateCandidateDTO.getEmail() != null ? updateCandidateDTO.getEmail() : candidate.getEmail()
            );
            
            if (existingCandidate.isPresent() && !existingCandidate.get().getId().equals(candidateId)) {
                throw new RuntimeException("Usuário já existe");
            }
        }
        
        if (updateCandidateDTO.getName() != null) {
            candidate.setName(updateCandidateDTO.getName());
        }
        
        if (updateCandidateDTO.getUsername() != null) {
            candidate.setUsername(updateCandidateDTO.getUsername());
        }
        
        if (updateCandidateDTO.getEmail() != null) {
            candidate.setEmail(updateCandidateDTO.getEmail());
        }
        
        if (updateCandidateDTO.getDescription() != null) {
            candidate.setDescription(updateCandidateDTO.getDescription());
        }
        
        return this.candidateRepository.save(candidate);
    }
} 