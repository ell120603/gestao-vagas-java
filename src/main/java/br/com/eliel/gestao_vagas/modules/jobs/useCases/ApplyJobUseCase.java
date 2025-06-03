package br.com.eliel.gestao_vagas.modules.jobs.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.eliel.gestao_vagas.modules.candidate.repositories.CandidateRepository;
import br.com.eliel.gestao_vagas.modules.jobs.repositories.JobsRepository;
import br.com.eliel.gestao_vagas.modules.jobs.repositories.CandidateJobRepository;
import br.com.eliel.gestao_vagas.modules.jobs.entites.CandidateJobEntity;

import java.util.UUID;

@Service
public class ApplyJobUseCase {
    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private JobsRepository jobsRepository;

    @Autowired
    private CandidateJobRepository candidateJobRepository;

    public CandidateJobEntity execute(UUID candidateId, UUID jobId) {
        this.candidateRepository.findById(candidateId)
            .orElseThrow(() -> {
                throw new RuntimeException("Candidato não encontrado");
            });

        this.jobsRepository.findById(jobId)
            .orElseThrow(() -> {
                throw new RuntimeException("Vaga não encontrada");
            });
        this.candidateJobRepository.findByCandidateIdAndJobId(candidateId, jobId)
            .ifPresent((application) -> {
                throw new RuntimeException("Candidato já se candidatou para esta vaga");
            });

        var candidateJob = CandidateJobEntity.builder()
            .candidateId(candidateId)
            .jobId(jobId)
            .build();

        return this.candidateJobRepository.save(candidateJob);
    }
} 