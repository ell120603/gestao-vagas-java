package br.com.eliel.gestao_vagas.modules.company.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.eliel.gestao_vagas.modules.company.entites.JobEntity;

public interface JobRepository extends JpaRepository<JobEntity, UUID> {
    
    List<JobEntity> findByActiveTrue();
    
    @Query("SELECT j FROM job j WHERE j.active = true AND j.companyId = :companyId")
    List<JobEntity> findByCompanyId(@Param("companyId") UUID companyId);
    
    @Query("SELECT j FROM job j WHERE j.active = true AND LOWER(j.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<JobEntity> findByTitleContainingIgnoreCase(@Param("title") String title);
    
    @Query("SELECT j FROM job j WHERE j.active = true AND LOWER(j.level) = LOWER(:level)")
    List<JobEntity> findByLevel(@Param("level") String level);
}