package br.com.eliel.gestao_vagas.modules.jobs.useCases;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.eliel.gestao_vagas.modules.jobs.dto.JobFilterDTO;
import br.com.eliel.gestao_vagas.modules.jobs.dto.JobsResponseDTO;
import br.com.eliel.gestao_vagas.modules.jobs.entites.JobEntity;
import br.com.eliel.gestao_vagas.modules.jobs.repositories.JobsRepository;

@Service
public class FilterJobsUseCase {
    
    @Autowired
    private JobsRepository jobsRepository;

    public List<JobsResponseDTO> execute(JobFilterDTO filter) {
        final JobFilterDTO finalFilter = filter != null ? filter : new JobFilterDTO();
        var jobs = this.jobsRepository.findByActiveTrue();
        
        return jobs.stream()
            .filter(job -> {
                boolean matches = true;
                
                if (finalFilter.getAreaAtuacao() != null && !finalFilter.getAreaAtuacao().isBlank()) {
                    matches = matches && job.getAreaAtuacao().toLowerCase()
                        .contains(finalFilter.getAreaAtuacao().toLowerCase());
                }
                
                if (finalFilter.getRequisitos() != null && !finalFilter.getRequisitos().isEmpty()) {
                    matches = matches && finalFilter.getRequisitos().stream()
                        .anyMatch(req -> job.getRequisitos().stream()
                            .anyMatch(jobReq -> jobReq.toLowerCase().contains(req.toLowerCase())));
                }
                
                if (finalFilter.getTipoContrato() != null) {
                    matches = matches && job.getTipoContrato().equals(finalFilter.getTipoContrato());
                }
                
                if (finalFilter.getLocalizacao() != null && !finalFilter.getLocalizacao().isBlank()) {
                    matches = matches && job.getLocalizacao().toLowerCase()
                        .contains(finalFilter.getLocalizacao().toLowerCase());
                }
                
                if (finalFilter.getBeneficios() != null && !finalFilter.getBeneficios().isEmpty()) {
                    matches = matches && finalFilter.getBeneficios().stream()
                        .anyMatch(beneficio -> job.getBeneficios() != null && 
                            job.getBeneficios().toLowerCase().contains(beneficio.toLowerCase()));
                }
                
                if (job.getSalario() != null) {
                    if (finalFilter.getSalarioMinimo() != null && 
                        job.getSalario().compareTo(finalFilter.getSalarioMinimo()) < 0) {
                        matches = false;
                    }
                    
                    if (finalFilter.getSalarioMaximo() != null && 
                        job.getSalario().compareTo(finalFilter.getSalarioMaximo()) > 0) {
                        matches = false;
                    }
                }
                
                return matches;
            })
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    private JobsResponseDTO convertToDTO(JobEntity job) {
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
            .createdAt(job.getCreatedAt())
            .build();
    }
} 