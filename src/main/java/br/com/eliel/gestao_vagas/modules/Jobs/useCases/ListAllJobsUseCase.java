package br.com.eliel.gestao_vagas.modules.jobs.useCases;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.eliel.gestao_vagas.modules.jobs.dto.JobsResponseDTO;
import br.com.eliel.gestao_vagas.modules.jobs.repositories.JobsRepository;

@Service
public class ListAllJobsUseCase {
    
    @Autowired
    private JobsRepository jobsRepository;
    
    public List<JobsResponseDTO> execute() {
        var jobs = this.jobsRepository.findByActiveTrue();
        
        return jobs.stream().map(job -> {
            return JobsResponseDTO.builder()
                .id(job.getId())
                .titulo(job.getTitulo())
                .descricao(job.getDescricao())
                .areaAtuacao(job.getAreaAtuacao())
                .tecnologias(job.getTecnologias())
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