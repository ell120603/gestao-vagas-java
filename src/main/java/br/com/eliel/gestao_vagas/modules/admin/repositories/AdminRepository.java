package br.com.eliel.gestao_vagas.modules.admin.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.eliel.gestao_vagas.modules.admin.entities.AdminEntity;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, UUID> {
    Optional<AdminEntity> findByUsername(String username);
    Optional<AdminEntity> findByUsernameOrEmail(String username, String email);
}
