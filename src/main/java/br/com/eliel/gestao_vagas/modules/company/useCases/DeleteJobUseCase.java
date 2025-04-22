package br.com.eliel.gestao_vagas.modules.company.useCases;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.eliel.gestao_vagas.exceptions.JobNotFoundException;
import br.com.eliel.gestao_vagas.modules.company.repositories.JobRepository;

@Service
public class DeleteJobUseCase {
    
    @Autowired
    private JobRepository jobRepository;
    
    public void execute(UUID id) {
        var job = this.jobRepository.findById(id)
            .orElseThrow(() -> new JobNotFoundException());
        job.setActive(false);
        this.jobRepository.save(job);
    }
}
