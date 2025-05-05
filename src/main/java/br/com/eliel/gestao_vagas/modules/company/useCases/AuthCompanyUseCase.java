package br.com.eliel.gestao_vagas.modules.company.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.eliel.gestao_vagas.exceptions.AuthenticationException;
import br.com.eliel.gestao_vagas.modules.company.dto.AuthCompanyDTO;
import br.com.eliel.gestao_vagas.modules.company.dto.AuthCompanyResponseDTO;
import br.com.eliel.gestao_vagas.modules.company.repositories.CompanyRepository;
import br.com.eliel.gestao_vagas.providers.JWTProvider;

import java.util.Arrays;

@Service
public class AuthCompanyUseCase {
    
    @Autowired
    private CompanyRepository companyRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JWTProvider jwtProvider;
    
    public AuthCompanyResponseDTO execute(AuthCompanyDTO authCompanyDTO) {
        var company = this.companyRepository.findByUsernameOrEmail(authCompanyDTO.getUsername(), authCompanyDTO.getUsername())
            .orElseThrow(() -> {
                throw new AuthenticationException();
            });
            
        var passwordMatches = this.passwordEncoder.matches(authCompanyDTO.getPassword(), company.getPassword());
        
        if (!passwordMatches) {
            throw new AuthenticationException();
        }
        
        var token = jwtProvider.generateToken(company.getId().toString(), Arrays.asList("COMPANY"));
        
        return AuthCompanyResponseDTO.builder()
            .token(token)
            .id(company.getId())
            .username(company.getUsername())
            .build();
    }
}
