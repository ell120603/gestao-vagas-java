package br.com.eliel.gestao_vagas.modules.company.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.eliel.gestao_vagas.exceptions.AuthenticationException;
import br.com.eliel.gestao_vagas.modules.company.dto.AuthCompanyDTO;
import br.com.eliel.gestao_vagas.modules.company.dto.AuthCompanyResponseDTO;
import br.com.eliel.gestao_vagas.modules.company.repositories.CompanyRepository;
import br.com.eliel.gestao_vagas.providers.JWTProvider;

import java.util.Arrays;

@Service
public class AuthCompanyUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthCompanyUseCase.class);
    
    @Autowired
    private CompanyRepository companyRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JWTProvider jwtProvider;
    
    public AuthCompanyResponseDTO execute(AuthCompanyDTO authCompanyDTO) {
        logger.info("Attempting to authenticate company with username/email: {}", authCompanyDTO.getUsername());
        
        var company = this.companyRepository.findByUsernameOrEmail(authCompanyDTO.getUsername(), authCompanyDTO.getUsername())
            .orElseThrow(() -> {
                logger.error("Company not found with username/email: {}", authCompanyDTO.getUsername());
                throw new AuthenticationException();
            });
            
        var passwordMatches = this.passwordEncoder.matches(authCompanyDTO.getPassword(), company.getPassword());
        
        if (!passwordMatches) {
            logger.error("Password does not match for company: {}", company.getUsername());
            throw new AuthenticationException();
        }

        if (!company.isActive()) {
            logger.error("Company is not active: {}", company.getUsername());
            throw new AuthenticationException("Empresa desativada");
        }
        
        logger.info("Company authenticated successfully: {}", company.getUsername());
        var token = jwtProvider.generateToken(company.getId().toString(), Arrays.asList("COMPANY"));
        logger.info("Token generated for company: {}", company.getUsername());
        
        return AuthCompanyResponseDTO.builder()
            .token(token)
            .id(company.getId())
            .username(company.getUsername())
            .build();
    }
}
