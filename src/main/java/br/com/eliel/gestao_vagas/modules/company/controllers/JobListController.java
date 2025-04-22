package br.com.eliel.gestao_vagas.modules.company.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.eliel.gestao_vagas.modules.company.dto.JobResponseDTO;
import br.com.eliel.gestao_vagas.modules.company.useCases.ListAllJobsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/jobs")
@Tag(name = "Vagas", description = "Informações de vagas")
public class JobListController {

    @Autowired
    private ListAllJobsUseCase listAllJobsUseCase;
    
    @GetMapping("/")
    @Operation(summary = "Listagem de vagas", description = "Essa função é responsável por listar todas as vagas ativas")
    public ResponseEntity<List<JobResponseDTO>> findAll() {
        var jobs = this.listAllJobsUseCase.execute();
        return ResponseEntity.ok().body(jobs);
    }
}
