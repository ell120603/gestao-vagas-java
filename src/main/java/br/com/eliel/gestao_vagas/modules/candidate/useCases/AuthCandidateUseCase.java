package br.com.eliel.gestao_vagas.modules.candidate.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.eliel.gestao_vagas.exceptions.AuthenticationException;
import br.com.eliel.gestao_vagas.modules.candidate.dto.AuthCandidateDTO;
import br.com.eliel.gestao_vagas.modules.candidate.dto.AuthCandidateResponseDTO;
import br.com.eliel.gestao_vagas.modules.candidate.repositories.CandidateRepository;
import br.com.eliel.gestao_vagas.providers.JWTProvider;

import java.util.Arrays;

@Service
public class AuthCandidateUseCase {
    
    @Autowired
    private CandidateRepository candidateRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JWTProvider jwtProvider;
    
    public AuthCandidateResponseDTO execute(AuthCandidateDTO authCandidateDTO) {
        var candidate = this.candidateRepository.findByUsernameOrEmail(authCandidateDTO.getUsername(), authCandidateDTO.getUsername())
            .orElseThrow(() -> {
                throw new AuthenticationException();
            });
            
        var passwordMatches = this.passwordEncoder.matches(authCandidateDTO.getPassword(), candidate.getPassword());
        
        if (!passwordMatches) {
            throw new AuthenticationException();
        }
        
        var token = jwtProvider.generateToken(candidate.getId().toString(), Arrays.asList("CANDIDATE"));
        
        return AuthCandidateResponseDTO.builder()
            .token(token)
            .id(candidate.getId())
            .username(candidate.getUsername())
            .build();
    }
}
