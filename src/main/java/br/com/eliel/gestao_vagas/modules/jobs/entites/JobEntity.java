package br.com.eliel.gestao_vagas.modules.jobs.entites;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import br.com.eliel.gestao_vagas.modules.company.entites.CompanyEntity;

@Data
@Entity
@Table(name = "jobs")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "O título é obrigatório")
    private String titulo;

    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    @NotBlank(message = "A área de atuação é obrigatória")
    private String areaAtuacao;

    private String[] tecnologias;

    @NotBlank(message = "O tipo de contrato é obrigatório")
    @Pattern(regexp = "CLT|PJ|TEMPORARIO|ESTAGIO", message = "O tipo de contrato deve ser CLT, PJ, TEMPORARIO ou ESTAGIO")
    private String tipoContrato;

    @NotBlank(message = "A localização é obrigatória")
    private String localizacao;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private CompanyEntity company;

    private BigDecimal salario;
    private String beneficios;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private boolean active = true;
}
