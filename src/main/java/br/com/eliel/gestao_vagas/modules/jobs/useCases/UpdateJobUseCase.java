package br.com.eliel.gestao_vagas.modules.jobs.useCases;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.eliel.gestao_vagas.exceptions.AuthenticationException;
import br.com.eliel.gestao_vagas.modules.jobs.dto.UpdateJobDTO;
import br.com.eliel.gestao_vagas.modules.jobs.entites.JobEntity;
import br.com.eliel.gestao_vagas.modules.jobs.repositories.JobsRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UpdateJobUseCase {
    
    @Autowired
    private JobsRepository jobsRepository;

    public JobEntity execute(UUID jobId, UUID companyId, UpdateJobDTO updateJobDTO) {
        var job = this.jobsRepository.findById(jobId)
            .orElseThrow(() -> new EntityNotFoundException("Vaga não encontrada"));

        if (!job.getCompany().getId().equals(companyId)) {
            throw new AuthenticationException("Você não tem permissão para editar esta vaga");
        }

        if (updateJobDTO.getTitulo() != null) {
            job.setTitulo(updateJobDTO.getTitulo());
        }
        if (updateJobDTO.getDescricao() != null) {
            job.setDescricao(updateJobDTO.getDescricao());
        }
        if (updateJobDTO.getAreaAtuacao() != null) {
            job.setAreaAtuacao(updateJobDTO.getAreaAtuacao());
        }
        if (updateJobDTO.getRequisitos() != null) {
            job.setRequisitos(updateJobDTO.getRequisitos());
        }
        if (updateJobDTO.getTipoContrato() != null) {
            job.setTipoContrato(updateJobDTO.getTipoContrato());
        }
        if (updateJobDTO.getLocalizacao() != null) {
            job.setLocalizacao(updateJobDTO.getLocalizacao());
        }
        if (updateJobDTO.getSalario() != null) {
            job.setSalario(updateJobDTO.getSalario());
        }
        if (updateJobDTO.getBeneficios() != null) {
            job.setBeneficios(updateJobDTO.getBeneficios());
        }

        return this.jobsRepository.save(job);
    }
} 