package br.com.eliel.gestao_vagas.modules.company.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.eliel.gestao_vagas.modules.company.dto.JobDTO;
import br.com.eliel.gestao_vagas.modules.company.entites.JobEntity;
import br.com.eliel.gestao_vagas.modules.company.useCases.DeleteJobUseCase;
import br.com.eliel.gestao_vagas.modules.company.useCases.UpdateJobUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/job")
@Tag(name = "Vagas", description = "Gerenciamento de vagas")
public class JobManagementController {

    @Autowired
    private UpdateJobUseCase updateJobUseCase;
    
    @Autowired
    private DeleteJobUseCase deleteJobUseCase;
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    @Operation(
        summary = "Atualização de vaga", 
        description = "Essa função é responsável por atualizar uma vaga existente",
        security = { @SecurityRequirement(name = "Bearer Authentication") }
    )
    public ResponseEntity<JobEntity> update(@PathVariable UUID id, @Valid @RequestBody JobDTO jobDTO) {
        try {
            var result = this.updateJobUseCase.execute(id, jobDTO);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    @Operation(
        summary = "Exclusão de vaga", 
        description = "Essa função é responsável por excluir uma vaga existente",
        security = { @SecurityRequirement(name = "Bearer Authentication") }
    )
    public ResponseEntity<Object> delete(@PathVariable UUID id) {
        try {
            this.deleteJobUseCase.execute(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
