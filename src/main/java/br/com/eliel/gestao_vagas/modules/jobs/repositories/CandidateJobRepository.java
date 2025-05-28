package br.com.eliel.gestao_vagas.modules.jobs.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.eliel.gestao_vagas.modules.jobs.entites.CandidateJobEntity;

public interface CandidateJobRepository extends JpaRepository<CandidateJobEntity, UUID> {
    Optional<CandidateJobEntity> findByCandidateIdAndJobId(UUID candidateId, UUID jobId);
} 