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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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
    @Operation(
        summary = "Cadastro de candidato", 
        description = "Rota responsável por cadastrar um novo candidato no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Candidato cadastrado com sucesso",
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
                            "description": "Desenvolvedor Java com 5 anos de experiência"
                        }
                        """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Dados inválidos fornecidos",
            content = @Content(
                examples = {
                    @ExampleObject(
                        name = "Erro de Validação",
                        value = """
                        {
                            "message": "Username já existe"
                        }
                        """
                    )
                }
            )
        )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Dados do candidato a ser cadastrado",
        required = true,
        content = @Content(
            examples = {
                @ExampleObject(
                    name = "Cadastro de Candidato",
                    value = """
                    {
                        "name": "João Silva",
                        "username": "joaosilva",
                        "email": "joao@email.com",
                        "password": "senha123456",
                        "description": "Desenvolvedor Java com 5 anos de experiência"
                    }
                    """
                )
            }
        )
    )
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
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Candidato desativado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Senha incorreta"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Senha do candidato para confirmação da desativação",
        required = true,
        content = @Content(
            examples = {
                @ExampleObject(
                    name = "Confirmação de Desativação",
                    value = """
                    {
                        "password": "senha123456"
                    }
                    """
                )
            }
        )
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
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Candidato atualizado com sucesso",
            content = @Content(
                examples = {
                    @ExampleObject(
                        name = "Resposta de Sucesso",
                        value = """
                        {
                            "id": "123e4567-e89b-12d3-a456-426614174000",
                            "name": "João Silva Atualizado",
                            "username": "joaosilva",
                            "email": "novo@email.com",
                            "description": "Desenvolvedor Java Senior com 5 anos de experiência"
                        }
                        """
                    )
                }
            )
        ),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Dados do candidato a serem atualizados",
        required = true,
        content = @Content(
            examples = {
                @ExampleObject(
                    name = "Atualização de Candidato",
                    value = """
                    {
                        "name": "João Silva Atualizado",
                        "email": "novo@email.com",
                        "description": "Desenvolvedor Java Senior com 5 anos de experiência"
                    }
                    """
                )
            }
        )
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
