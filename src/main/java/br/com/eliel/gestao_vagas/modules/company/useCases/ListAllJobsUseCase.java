package br.com.eliel.gestao_vagas.modules.company.useCases;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.eliel.gestao_vagas.modules.company.dto.JobResponseDTO;
import br.com.eliel.gestao_vagas.modules.company.repositories.JobRepository;

@Service
public class ListAllJobsUseCase {
    
    @Autowired
    private JobRepository jobRepository;
    
    public List<JobResponseDTO> execute() {
        var jobs = this.jobRepository.findByActiveTrue();
        
        return jobs.stream().map(job -> {
            var companyName = job.getCompanyEntity() != null ? job.getCompanyEntity().getUsername() : "";
            
            return JobResponseDTO.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .benefits(job.getBenefits())
                .level(job.getLevel())
                .companyId(job.getCompanyId())
                .companyName(companyName)
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .build();
        }).collect(Collectors.toList());
    }
}
