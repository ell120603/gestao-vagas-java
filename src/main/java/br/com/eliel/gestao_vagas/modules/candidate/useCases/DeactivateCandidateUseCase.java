package br.com.eliel.gestao_vagas.modules.candidate.useCases;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.eliel.gestao_vagas.modules.candidate.repositories.CandidateRepository;

@Service
public class DeactivateCandidateUseCase {
    @Autowired
    private CandidateRepository candidateRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void execute(UUID candidateId, String password) {
        var candidate = this.candidateRepository.findById(candidateId)
            .orElseThrow(() -> new RuntimeException("Candidato não encontrado"));
            
        var passwordMatches = this.passwordEncoder.matches(password, candidate.getPassword());
        
        if (!passwordMatches) {
            throw new RuntimeException("Senha incorreta");
        }
            
        candidate.setActive(false);
        this.candidateRepository.save(candidate);
    }
    
}
