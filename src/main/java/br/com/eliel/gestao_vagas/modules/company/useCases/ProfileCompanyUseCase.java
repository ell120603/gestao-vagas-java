package br.com.eliel.gestao_vagas.modules.company.useCases;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.eliel.gestao_vagas.modules.company.dto.ProfileCompanyResponseDTO;
import br.com.eliel.gestao_vagas.modules.company.repositories.CompanyRepository;

@Service
public class ProfileCompanyUseCase {
    
    @Autowired
    private CompanyRepository companyRepository;
    
    public ProfileCompanyResponseDTO execute(UUID companyId) {
        var company = this.companyRepository.findById(companyId)
            .orElseThrow(() -> {
                throw new RuntimeException("Empresa não encontrada");
            });

        var companyDTO = ProfileCompanyResponseDTO.builder()
            .name(company.getName())
            .username(company.getUsername())
            .email(company.getEmail())
            .website(company.getWebsite())
            .description(company.getDescription())
            .build();

        return companyDTO;
    }
} 