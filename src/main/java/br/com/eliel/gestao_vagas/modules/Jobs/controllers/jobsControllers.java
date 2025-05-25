package br.com.eliel.gestao_vagas.modules.jobs.controllers;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import br.com.eliel.gestao_vagas.modules.jobs.dto.JobsDTO;
import br.com.eliel.gestao_vagas.modules.jobs.dto.JobsResponseDTO;
import br.com.eliel.gestao_vagas.modules.jobs.entites.JobEntity;
import br.com.eliel.gestao_vagas.modules.jobs.useCases.JobsUseCases;
import br.com.eliel.gestao_vagas.modules.jobs.useCases.ListAllJobsUseCase;

@RestController
@RequestMapping("/job")
@Tag(name = "Vagas", description = "Gestão de vagas")
public class JobsControllers {
     @Autowired
    private JobsUseCases jobsUseCases;

    @Autowired
    private ListAllJobsUseCase listAllJobsUseCase;

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
}
