package br.com.eliel.gestao_vagas.modules.candidate.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.eliel.gestao_vagas.modules.candidate.dto.ProfileCandidateResponseDTO;
import br.com.eliel.gestao_vagas.modules.candidate.useCases.ProfileCandidateUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(
        summary = "Perfil do candidato", 
        description = "Rota responsável por buscar as informações do perfil do candidato logado",
        security = { @SecurityRequirement(name = "Bearer Authentication") }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Perfil do candidato retornado com sucesso",
            content = @Content(
                examples = {
                    @ExampleObject(
                        name = "Resposta de Sucesso",
                        value = """
                        {
                            "id": "123e4567-e89b-12d3-a456-426614174000",
                            "name": "João Silva",
                            "username": "joaosilva",
                            "email": "joao@email.com",
                            "curriculum": "https://linkedin.com/in/joaosilva",
                            "description": "Desenvolvedor Java com 5 anos de experiência",
                            "createdAt": "2024-03-20T10:00:00Z"
                        }
                        """
                    )
                }
            )
        ),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado"),
        @ApiResponse(responseCode = "404", description = "Candidato não encontrado")
    })
    public ResponseEntity<Object> profile(HttpServletRequest request) {
        try {
            var candidateId = UUID.fromString(request.getAttribute("candidate_id").toString());
            var candidate = this.profileCandidateUseCase.execute(candidateId);
            return ResponseEntity.ok().body(candidate);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("ID do candidato inválido");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
