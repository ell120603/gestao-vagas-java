package br.com.eliel.gestao_vagas.modules.company.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.eliel.gestao_vagas.modules.company.entites.CompanyEntity;
import br.com.eliel.gestao_vagas.modules.company.repositories.CompanyRepository;

@Service
public class CreateCompanyUseCase {
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired 
    private PasswordEncoder passwordEncoder;
    
    public ResponseEntity<Object> execute(CompanyEntity companyEntity) {
        try {
            this.companyRepository
                .findByUsernameOrEmail(companyEntity.getUsername(), companyEntity.getEmail())
                .ifPresent(company -> {
                    throw new RuntimeException("Username or email already exists");
                });

            var password = passwordEncoder.encode(companyEntity.getPassword());
            companyEntity.setPassword(password);
            var companyCreated = this.companyRepository.save(companyEntity);
            return ResponseEntity.ok().body(companyCreated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
