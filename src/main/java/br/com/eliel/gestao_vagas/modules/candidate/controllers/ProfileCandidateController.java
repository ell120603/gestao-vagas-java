package br.com.eliel.gestao_vagas.modules.candidate.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.eliel.gestao_vagas.modules.candidate.dto.ProfileCandidateResponseDTO;
import br.com.eliel.gestao_vagas.modules.candidate.useCases.ProfileCandidateUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/candidate")
@Tag(name = "Candidato", description = "Informações do candidato")
public class ProfileCandidateController {

    @Autowired
    private ProfileCandidateUseCase profileCandidateUseCase;

    @GetMapping("/profile")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Perfil do candidato", description = "Essa função é responsável por buscar as informações do perfil do candidato", security = {
            @SecurityRequirement(name = "Bearer Authentication")
    })
    public ResponseEntity<ProfileCandidateResponseDTO> profile(HttpServletRequest request) {
        var candidateId = request.getAttribute("candidate_id");
        var profile = this.profileCandidateUseCase.execute(UUID.fromString(candidateId.toString()));
        return ResponseEntity.ok().body(profile);
    }
}
