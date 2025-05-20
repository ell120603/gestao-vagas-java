package br.com.eliel.gestao_vagas.modules.admin.useCases;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.eliel.gestao_vagas.modules.admin.dto.AdminUpdateCandidateDTO;
import br.com.eliel.gestao_vagas.modules.candidate.entites.CandidateEntity;
import br.com.eliel.gestao_vagas.modules.candidate.repositories.CandidateRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class AdminUpdateCandidateUseCase {

    @Autowired
    private CandidateRepository candidateRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public CandidateEntity execute(UUID candidateId, AdminUpdateCandidateDTO adminUpdateCandidateDTO) {
        CandidateEntity candidate = this.candidateRepository.findById(candidateId)
                .orElseThrow(() -> new EntityNotFoundException("Candidato não encontrado"));
        
        if (adminUpdateCandidateDTO.getName() != null) {
            candidate.setName(adminUpdateCandidateDTO.getName());
        }
        if (adminUpdateCandidateDTO.getUsername() != null) {
            candidate.setUsername(adminUpdateCandidateDTO.getUsername());
        }
        if (adminUpdateCandidateDTO.getEmail() != null) {
            candidate.setEmail(adminUpdateCandidateDTO.getEmail());
        }
        if (adminUpdateCandidateDTO.getDescription() != null) {
            candidate.setDescription(adminUpdateCandidateDTO.getDescription());
        }
        
        if (adminUpdateCandidateDTO.getPassword() != null && !adminUpdateCandidateDTO.getPassword().isEmpty()) {
            candidate.setPassword(this.passwordEncoder.encode(adminUpdateCandidateDTO.getPassword()));
        }
        
        return this.candidateRepository.save(candidate);
    }
}
