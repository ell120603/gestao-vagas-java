package br.com.eliel.gestao_vagas.modules.jobs.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.eliel.gestao_vagas.modules.jobs.entites.JobEntity;

@Repository
public interface JobsRepositories extends JpaRepository<JobEntity, UUID> {
    List<JobEntity> findByCompanyId(UUID companyId);
    List<JobEntity> findByActiveTrue();
}
