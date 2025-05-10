package br.com.eliel.gestao_vagas.modules.company.useCases;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.eliel.gestao_vagas.modules.company.repositories.CompanyRepository;

@Service
public class DeactivateCompanyUseCase {
    
    @Autowired
    private CompanyRepository companyRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public void execute(UUID companyId, String password) {
        var company = this.companyRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
            
        var passwordMatches = this.passwordEncoder.matches(password, company.getPassword());
        
        if (!passwordMatches) {
            throw new RuntimeException("Senha incorreta");
        }
            
        company.setActive(false);
        this.companyRepository.save(company);
    }
} 