package br.com.eliel.gestao_vagas.modules.candidate;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.eliel.gestao_vagas.modules.candidate.entites.CandidateEntity;

@Repository
public interface CandidateRepository extends JpaRepository<CandidateEntity, UUID> {
    Optional<CandidateEntity> findByUsernameOrEmail(String username, String email);
} 