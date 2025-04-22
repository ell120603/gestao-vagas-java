package br.com.eliel.gestao_vagas.modules.company.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.eliel.gestao_vagas.modules.company.entites.CompanyEntity;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity,UUID> {

    Optional<CompanyEntity> findByUsernameOrEmail(String username, String email);
    CompanyEntity findByUsername(String username);
} 
    