package br.com.eliel.gestao_vagas.modules.jobs.useCases;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.eliel.gestao_vagas.modules.company.repositories.CompanyRepository;
import br.com.eliel.gestao_vagas.modules.jobs.dto.JobsDTO;
import br.com.eliel.gestao_vagas.modules.jobs.entites.JobEntity;
import br.com.eliel.gestao_vagas.modules.jobs.repositories.JobsRepositories;
import jakarta.persistence.EntityNotFoundException;


@Service
public class JobsUseCases {
    @Autowired
    private JobsRepositories jobsRepository;

    @Autowired
    private CompanyRepository companyRepository;

    public JobEntity execute(JobsDTO jobsDTO, UUID companyId) {
        var company = this.companyRepository.findById(companyId)
            .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada"));

        var job = JobEntity.builder()
            .titulo(jobsDTO.getTitulo())
            .descricao(jobsDTO.getDescricao())
            .areaAtuacao(jobsDTO.getAreaAtuacao())
            .tecnologias(jobsDTO.getTecnologias())
            .tipoContrato(jobsDTO.getTipoContrato())
            .localizacao(jobsDTO.getLocalizacao())
            .company(company) 
            .salario(jobsDTO.getSalario())
            .beneficios(jobsDTO.getBeneficios())
            .active(true)
            .build();

        return this.jobsRepository.save(job);
    }
}
