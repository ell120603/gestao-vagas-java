package br.com.eliel.gestao_vagas.modules.admin.useCases;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.eliel.gestao_vagas.modules.admin.dto.AdminUpdateCompanyDTO;
import br.com.eliel.gestao_vagas.modules.company.entites.CompanyEntity;
import br.com.eliel.gestao_vagas.modules.company.repositories.CompanyRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class AdminUpdateCompanyUseCase {

    @Autowired
    private CompanyRepository companyRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public CompanyEntity execute(UUID companyId, AdminUpdateCompanyDTO adminUpdateCompanyDTO) {
        CompanyEntity company = this.companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada"));
        
        if (adminUpdateCompanyDTO.getName() != null) {
            company.setName(adminUpdateCompanyDTO.getName());
        }
        
        if (adminUpdateCompanyDTO.getUsername() != null) {
            company.setUsername(adminUpdateCompanyDTO.getUsername());
        }
        
        if (adminUpdateCompanyDTO.getEmail() != null) {
            company.setEmail(adminUpdateCompanyDTO.getEmail());
        }
        
        if (adminUpdateCompanyDTO.getWebsite() != null) {
            company.setWebsite(adminUpdateCompanyDTO.getWebsite());
        }
        
        if (adminUpdateCompanyDTO.getDescription() != null) {
            company.setDescription(adminUpdateCompanyDTO.getDescription());
        }
        
        if (adminUpdateCompanyDTO.getPassword() != null && !adminUpdateCompanyDTO.getPassword().isEmpty()) {
            company.setPassword(this.passwordEncoder.encode(adminUpdateCompanyDTO.getPassword()));
        }
        
        return this.companyRepository.save(company);
    }
}
