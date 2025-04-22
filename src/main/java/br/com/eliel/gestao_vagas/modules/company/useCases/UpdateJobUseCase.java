package br.com.eliel.gestao_vagas.modules.company.useCases;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.eliel.gestao_vagas.exceptions.JobNotFoundException;
import br.com.eliel.gestao_vagas.modules.company.dto.JobDTO;
import br.com.eliel.gestao_vagas.modules.company.entites.JobEntity;
import br.com.eliel.gestao_vagas.modules.company.repositories.JobRepository;

@Service
public class UpdateJobUseCase {
    
    @Autowired
    private JobRepository jobRepository;
    
    public JobEntity execute(UUID id, JobDTO jobDTO) {
        var job = this.jobRepository.findById(id)
            .orElseThrow(() -> new JobNotFoundException());
        
        if (jobDTO.getTitle() != null && !jobDTO.getTitle().isEmpty()) {
            job.setTitle(jobDTO.getTitle());
        }
        
        if (jobDTO.getDescription() != null && !jobDTO.getDescription().isEmpty()) {
            job.setDescription(jobDTO.getDescription());
        }
        
        if (jobDTO.getBenefits() != null && !jobDTO.getBenefits().isEmpty()) {
            job.setBenefits(jobDTO.getBenefits());
        }
        
        if (jobDTO.getLevel() != null && !jobDTO.getLevel().isEmpty()) {
            job.setLevel(jobDTO.getLevel());
        }
        
        return this.jobRepository.save(job);
    }
}
