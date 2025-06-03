package br.com.eliel.gestao_vagas.modules.jobs.useCases;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.eliel.gestao_vagas.modules.jobs.dto.JobsResponseDTO;
import br.com.eliel.gestao_vagas.modules.jobs.repositories.CandidateJobRepository;
import br.com.eliel.gestao_vagas.modules.jobs.repositories.JobsRepository;

@Service
public class ListCandidateJobsUseCase {
    
    @Autowired
    private CandidateJobRepository candidateJobRepository;

    @Autowired
    private JobsRepository jobsRepository;

    public List<JobsResponseDTO> execute(UUID candidateId) {
        var candidateJobs = this.candidateJobRepository.findByCandidateId(candidateId);
        
        return candidateJobs.stream().map(candidateJob -> {
            var job = this.jobsRepository.findById(candidateJob.getJobId())
                .orElseThrow(() -> new RuntimeException("Vaga não encontrada"));
            
            return JobsResponseDTO.builder()
                .id(job.getId())
                .titulo(job.getTitulo())
                .descricao(job.getDescricao())
                .areaAtuacao(job.getAreaAtuacao())
                .requisitos(job.getRequisitos())
                .tipoContrato(job.getTipoContrato())
                .localizacao(job.getLocalizacao())
                .companyName(job.getCompany().getName())
                .salario(job.getSalario())
                .beneficios(job.getBeneficios())
                .build();
        }).collect(Collectors.toList());
    }
} 