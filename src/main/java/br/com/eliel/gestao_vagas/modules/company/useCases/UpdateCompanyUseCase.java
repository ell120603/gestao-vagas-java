package br.com.eliel.gestao_vagas.modules.company.useCases;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.eliel.gestao_vagas.modules.company.dto.UpdateCompanyDTO;
import br.com.eliel.gestao_vagas.modules.company.entites.CompanyEntity;
import br.com.eliel.gestao_vagas.modules.company.repositories.CompanyRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UpdateCompanyUseCase {

    @Autowired
    private CompanyRepository companyRepository;
    
    public CompanyEntity execute(UUID companyId, UpdateCompanyDTO updateCompanyDTO) {
        CompanyEntity company = this.companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada"));
        
        if (updateCompanyDTO.getName() != null) {
            company.setName(updateCompanyDTO.getName());
        }
        
        if (updateCompanyDTO.getUsername() != null) {
            company.setUsername(updateCompanyDTO.getUsername());
        }
        
        if (updateCompanyDTO.getEmail() != null) {
            company.setEmail(updateCompanyDTO.getEmail());
        }
        
        if (updateCompanyDTO.getWebsite() != null) {
            company.setWebsite(updateCompanyDTO.getWebsite());
        }
        
        if (updateCompanyDTO.getDescription() != null) {
            company.setDescription(updateCompanyDTO.getDescription());
        }
        
        return this.companyRepository.save(company);
    }
} 