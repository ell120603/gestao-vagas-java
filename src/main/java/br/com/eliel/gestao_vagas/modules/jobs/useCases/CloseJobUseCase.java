package br.com.eliel.gestao_vagas.modules.jobs.useCases;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.eliel.gestao_vagas.modules.jobs.entites.JobEntity;
import br.com.eliel.gestao_vagas.modules.jobs.repositories.JobsRepository;

@Service
public class CloseJobUseCase {

    @Autowired
    private JobsRepository jobsRepository;

    public JobEntity execute(UUID jobId, UUID companyId) {
        Optional<JobEntity> job = jobsRepository.findById(jobId);

        if (job.isEmpty()) {
            throw new RuntimeException("Vaga não encontrada.");
        }

        JobEntity jobEntity = job.get();

        if (!jobEntity.getCompany().getId().equals(companyId)) {
            throw new RuntimeException("Você não tem permissão para fechar esta vaga.");
        }

        if (!jobEntity.isActive()) {
            throw new RuntimeException("A vaga já está fechada.");
        }

        jobEntity.setActive(false);
        return jobsRepository.save(jobEntity);
    }
}