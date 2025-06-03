package br.com.eliel.gestao_vagas.modules.company.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.eliel.gestao_vagas.modules.company.dto.ProfileCompanyResponseDTO;
import br.com.eliel.gestao_vagas.modules.company.useCases.ProfileCompanyUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/company")
@Tag(name = "Empresa", description = "Informações da empresa")
public class ProfileCompanyController {

    @Autowired
    private ProfileCompanyUseCase profileCompanyUseCase;

    @GetMapping("/profile")
    @PreAuthorize("hasRole('COMPANY')")
    @Operation(
        summary = "Perfil da empresa", 
        description = "Rota responsável por buscar as informações do perfil da empresa logada",
        security = { @SecurityRequirement(name = "Bearer Authentication") }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Perfil da empresa retornado com sucesso",
            content = @Content(
                examples = {
                    @ExampleObject(
                        name = "Resposta de Sucesso",
                        value = """
                        {
                            "id": "123e4567-e89b-12d3-a456-426614174000",
                            "name": "Tech Solutions",
                            "username": "techsolutions",
                            "email": "contato@techsolutions.com",
                            "website": "https://techsolutions.com",
                            "description": "Empresa especializada em desenvolvimento de software",
                            "createdAt": "2024-03-20T10:00:00Z"
                        }
                        """
                    )
                }
            )
        ),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado"),
        @ApiResponse(responseCode = "404", description = "Empresa não encontrada")
    })
    public ResponseEntity<ProfileCompanyResponseDTO> profile(HttpServletRequest request) {
        var companyId = request.getAttribute("company_id");
        var profile = this.profileCompanyUseCase.execute(UUID.fromString(companyId.toString()));
        return ResponseEntity.ok().body(profile);
    }
} 