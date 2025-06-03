package br.com.eliel.gestao_vagas.modules.jobs.useCases;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.eliel.gestao_vagas.exceptions.AuthenticationException;
import br.com.eliel.gestao_vagas.modules.candidate.repositories.CandidateRepository;
import br.com.eliel.gestao_vagas.modules.jobs.dto.CandidateJobResponseDTO;
import br.com.eliel.gestao_vagas.modules.jobs.repositories.CandidateJobRepository;
import br.com.eliel.gestao_vagas.modules.jobs.repositories.JobsRepository;

@Service
public class ListJobCandidatesUseCase {
    
    @Autowired
    private CandidateJobRepository candidateJobRepository;

    @Autowired
    private JobsRepository jobsRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    public List<CandidateJobResponseDTO> execute(UUID jobId, UUID companyId) {
        var job = this.jobsRepository.findById(jobId)
            .orElseThrow(() -> new RuntimeException("Vaga não encontrada"));

        if (!job.getCompany().getId().equals(companyId)) {
            throw new AuthenticationException("Você não tem permissão para ver os candidatos desta vaga");
        }

        var candidateJobs = this.candidateJobRepository.findByJobId(jobId);
        
        return candidateJobs.stream().map(candidateJob -> {
            var candidate = this.candidateRepository.findById(candidateJob.getCandidateId())
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado"));
            
            return CandidateJobResponseDTO.builder()
                .id(candidate.getId())
                .name(candidate.getName())
                .email(candidate.getEmail())
                .username(candidate.getUsername())
                .description(candidate.getDescription())
                .build();
        }).collect(Collectors.toList());
    }
} 