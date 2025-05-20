package br.com.eliel.gestao_vagas.modules.candidate.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.eliel.gestao_vagas.modules.candidate.entites.CandidateEntity;
import br.com.eliel.gestao_vagas.modules.candidate.useCases.CreateCandidateUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import br.com.eliel.gestao_vagas.modules.candidate.dto.DeactivateCandidateDTO;
import br.com.eliel.gestao_vagas.modules.candidate.dto.UpdateCandidateDTO;
import br.com.eliel.gestao_vagas.modules.candidate.useCases.DeactivateCandidateUseCase;
import br.com.eliel.gestao_vagas.modules.candidate.useCases.UpdateCandidateUseCase;

@RestController
@RequestMapping("/candidate")
@Tag(name = "Candidato", description = "Gestão de candidatos")
public class CandidateController {

    @Autowired
    private CreateCandidateUseCase createCandidateUseCase;
    @Autowired
    private DeactivateCandidateUseCase deactivateCandidateUseCase;
    @Autowired
    private UpdateCandidateUseCase updateCandidateUseCase;

    @PostMapping("/")
    @Operation(summary = "Cadastro de candidato", description = "Rota responsável por cadastrar um novo candidato")
    public ResponseEntity<Object> create(@Valid @RequestBody CandidateEntity candidateEntity) {
        try {
            var result = this.createCandidateUseCase.execute(candidateEntity);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/deactivate")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(
        summary = "Desativação do candidato", 
        description = "Rota responsável por deletar/desativar o próprio candidato usando a senha",
        security = { @SecurityRequirement(name = "Bearer Authentication") }
    )
    public ResponseEntity<Object> deactivate(
        @Valid @RequestBody DeactivateCandidateDTO deactivateCandidateDTO,
        Authentication authentication
    ) {
        try {
            var candidateId = UUID.fromString(authentication.getName());
            this.deactivateCandidateUseCase.execute(candidateId, deactivateCandidateDTO.getPassword());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PutMapping("/")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(
        summary = "Atualização de dados do candidato", 
        description = "Rota responsável por atualizar os dados do próprio candidato",
        security = { @SecurityRequirement(name = "Bearer Authentication") }
    )
    public ResponseEntity<Object> update(
        @Valid @RequestBody UpdateCandidateDTO updateCandidateDTO,
        Authentication authentication
    ) {
        try {
            var candidateId = UUID.fromString(authentication.getName());
            var updatedCandidate = this.updateCandidateUseCase.execute(candidateId, updateCandidateDTO);
            return ResponseEntity.ok().body(updatedCandidate);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
