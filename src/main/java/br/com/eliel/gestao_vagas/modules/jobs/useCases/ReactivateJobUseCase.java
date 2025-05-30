package br.com.eliel.gestao_vagas.modules.jobs.useCases;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.eliel.gestao_vagas.exceptions.AuthenticationException;
import br.com.eliel.gestao_vagas.modules.jobs.entites.JobEntity;
import br.com.eliel.gestao_vagas.modules.jobs.repositories.JobsRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ReactivateJobUseCase {

    @Autowired
    private JobsRepository jobsRepository;

    public JobEntity execute(UUID jobId, UUID companyId) {
        var job = this.jobsRepository.findById(jobId)
            .orElseThrow(() -> new EntityNotFoundException("Vaga não encontrada"));

        if (!job.getCompany().getId().equals(companyId)) {
            throw new AuthenticationException("Você não tem permissão para reativar esta vaga");
        }

        if (job.isActive()) {
            throw new RuntimeException("A vaga já está ativa");
        }

        job.setActive(true);
        return this.jobsRepository.save(job);
    }
} 