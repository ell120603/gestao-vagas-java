package br.com.eliel.gestao_vagas.modules.candidate.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.eliel.gestao_vagas.modules.candidate.CandidateEntity;
import br.com.eliel.gestao_vagas.modules.candidate.CandidateRepository;

@Service
public class CreateCandidateUseCase {
    
    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public ResponseEntity<Object> execute(CandidateEntity candidateEntity) {
        try {
            var candidate = this.candidateRepository.findByUsernameOrEmail(candidateEntity.getUsername(), candidateEntity.getEmail());
            
            if(candidate.isPresent()) {
                return ResponseEntity.badRequest().body("Usuário já existe");
            }

            var password = passwordEncoder.encode(candidateEntity.getPassword());
            candidateEntity.setPassword(password);

            var candidateCreated = this.candidateRepository.save(candidateEntity);
            return ResponseEntity.ok().body(candidateCreated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao criar candidato: " + e.getMessage());
        }
    }
}
