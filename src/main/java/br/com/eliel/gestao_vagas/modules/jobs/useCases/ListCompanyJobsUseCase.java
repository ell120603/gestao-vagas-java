package br.com.eliel.gestao_vagas.modules.jobs.useCases;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.eliel.gestao_vagas.modules.jobs.dto.JobsResponseDTO;
import br.com.eliel.gestao_vagas.modules.jobs.entites.JobEntity;
import br.com.eliel.gestao_vagas.modules.jobs.repositories.JobsRepository;

@Service
public class ListCompanyJobsUseCase {
    
    @Autowired
    private JobsRepository jobsRepository;

    public List<JobsResponseDTO> execute(UUID companyId) {
        var jobs = this.jobsRepository.findByCompanyId(companyId);
        
        return jobs.stream().map(job -> {
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
        }).collect(Collectors.toList());
    }
} 