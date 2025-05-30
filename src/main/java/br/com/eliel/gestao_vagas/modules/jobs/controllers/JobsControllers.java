package br.com.eliel.gestao_vagas.modules.jobs.controllers;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import br.com.eliel.gestao_vagas.modules.jobs.dto.JobsDTO;
import br.com.eliel.gestao_vagas.modules.jobs.dto.JobsResponseDTO;
import br.com.eliel.gestao_vagas.modules.jobs.dto.UpdateJobDTO;
import br.com.eliel.gestao_vagas.modules.jobs.useCases.JobsUseCases;
import br.com.eliel.gestao_vagas.modules.jobs.useCases.ListAllJobsUseCase;
import br.com.eliel.gestao_vagas.modules.jobs.useCases.UpdateJobUseCase;
import br.com.eliel.gestao_vagas.modules.jobs.useCases.CloseJobUseCase;
import br.com.eliel.gestao_vagas.modules.jobs.useCases.ApplyJobUseCase;
import br.com.eliel.gestao_vagas.modules.jobs.useCases.ListCompanyJobsUseCase;
import br.com.eliel.gestao_vagas.modules.jobs.useCases.DeleteJobUseCase;
import br.com.eliel.gestao_vagas.modules.jobs.dto.DeleteJobDTO;
import br.com.eliel.gestao_vagas.modules.jobs.useCases.ReactivateJobUseCase;

@RestController
@RequestMapping("/job")
@Tag(name = "Vagas", description = "Gestão de vagas")
public class JobsControllers {
    @Autowired
    private JobsUseCases jobsUseCases;

    @Autowired
    private ListAllJobsUseCase listAllJobsUseCase;

    @Autowired
    private UpdateJobUseCase updateJobUseCase;

    @Autowired
    private CloseJobUseCase closeJobUseCase;

    @Autowired
    private ApplyJobUseCase applyJobUseCase;

    @Autowired
    private ListCompanyJobsUseCase listCompanyJobsUseCase;

    @Autowired
    private DeleteJobUseCase deleteJobUseCase;

    @Autowired
    private ReactivateJobUseCase reactivateJobUseCase;

    @PostMapping("/")
    @PreAuthorize("hasRole('COMPANY')")
    @Operation(
        summary = "Criação de vaga", 
        description = "Rota responsável por criar uma nova vaga",
        security = { @SecurityRequirement(name = "Bearer Authentication") }
    )
    public ResponseEntity<Object> create(@Valid @RequestBody JobsDTO jobsDTO, HttpServletRequest request) {
        var companyId = request.getAttribute("company_id");
        
        try {
            var job = this.jobsUseCases.execute(jobsDTO, UUID.fromString(companyId.toString()));
            return ResponseEntity.ok().body(job);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/")
    @Operation(
        summary = "Listagem de vagas", 
        description = "Rota responsável por listar todas as vagas ativas"
    )
    public ResponseEntity<List<JobsResponseDTO>> list() {
        var jobs = this.listAllJobsUseCase.execute();
        return ResponseEntity.ok().body(jobs);
    }

    @PutMapping("/{jobId}")
    @PreAuthorize("hasRole('COMPANY')")
    @Operation(
        summary = "Atualização de vaga", 
        description = "Rota responsável por atualizar uma vaga existente",
        security = { @SecurityRequirement(name = "Bearer Authentication") }
    )
    public ResponseEntity<Object> update(
        @PathVariable UUID jobId,
        @Valid @RequestBody UpdateJobDTO updateJobDTO,
        HttpServletRequest request
    ) {
        var companyId = request.getAttribute("company_id");
        
        try {
            var job = this.updateJobUseCase.execute(
                jobId,
                UUID.fromString(companyId.toString()),
                updateJobDTO
            );
            return ResponseEntity.ok().body(job);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{jobId}/close")
    @PreAuthorize("hasRole('COMPANY')")
    @Operation(
        summary = "Fechamento de vaga", 
        description = "Rota responsável por fechar uma vaga existente",
        security = { @SecurityRequirement(name = "Bearer Authentication") }
    )
    public ResponseEntity<Object> close(
        @PathVariable UUID jobId,
        HttpServletRequest request
    ) {
        var companyId = request.getAttribute("company_id");
        
        try {
            var job = this.closeJobUseCase.execute(
                jobId,
                UUID.fromString(companyId.toString())
            );
            return ResponseEntity.ok().body(job);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{jobId}/apply")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(
        summary = "Candidatura em vaga", 
        description = "Rota responsável por permitir que um candidato se candidate a uma vaga",
        security = { @SecurityRequirement(name = "Bearer Authentication") }
    )
    public ResponseEntity<Object> apply(
        @PathVariable UUID jobId,
        HttpServletRequest request
    ) {
        var candidateId = request.getAttribute("candidate_id");
        
        try {
            var result = this.applyJobUseCase.execute(
                UUID.fromString(candidateId.toString()),
                jobId
            );
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/company")
    @PreAuthorize("hasRole('COMPANY')")
    @Operation(
        summary = "Listagem de vagas da empresa", 
        description = "Rota responsável por listar todas as vagas criadas pela empresa logada",
        security = { @SecurityRequirement(name = "Bearer Authentication") }
    )
    public ResponseEntity<List<JobsResponseDTO>> listCompanyJobs(HttpServletRequest request) {
        var companyId = request.getAttribute("company_id");
        var jobs = this.listCompanyJobsUseCase.execute(UUID.fromString(companyId.toString()));
        return ResponseEntity.ok().body(jobs);
    }

    @DeleteMapping("/{jobId}")
    @PreAuthorize("hasRole('COMPANY')")
    @Operation(
        summary = "Deleção de vaga", 
        description = "Rota responsável por deletar uma vaga existente. Requer autenticação e senha da empresa.",
        security = { @SecurityRequirement(name = "Bearer Authentication") }
    )
    public ResponseEntity<Object> delete(
        @PathVariable UUID jobId,
        @Valid @RequestBody DeleteJobDTO deleteJobDTO,
        HttpServletRequest request
    ) {
        var companyId = request.getAttribute("company_id");
        
        try {
            this.deleteJobUseCase.execute(
                jobId,
                UUID.fromString(companyId.toString()),
                deleteJobDTO.getPassword()
            );
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{jobId}/reactivate")
    @PreAuthorize("hasRole('COMPANY')")
    @Operation(
        summary = "Reativação de vaga", 
        description = "Rota responsável por reativar uma vaga que foi fechada anteriormente",
        security = { @SecurityRequirement(name = "Bearer Authentication") }
    )
    public ResponseEntity<Object> reactivate(
        @PathVariable UUID jobId,
        HttpServletRequest request
    ) {
        var companyId = request.getAttribute("company_id");
        
        try {
            var job = this.reactivateJobUseCase.execute(
                jobId,
                UUID.fromString(companyId.toString())
            );
            return ResponseEntity.ok().body(job);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
