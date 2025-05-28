package br.com.eliel.gestao_vagas.modules.jobs.entites;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "candidate_job")
@Table(name = "candidate_job")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateJobEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID candidateId;

    private UUID jobId;

    @CreationTimestamp
    private LocalDateTime createdAt;
} 