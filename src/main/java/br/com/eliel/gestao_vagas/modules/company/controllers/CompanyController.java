package br.com.eliel.gestao_vagas.modules.company.controllers;

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

import br.com.eliel.gestao_vagas.modules.company.dto.DeactivateCompanyDTO;
import br.com.eliel.gestao_vagas.modules.company.dto.UpdateCompanyDTO;
import br.com.eliel.gestao_vagas.modules.company.entites.CompanyEntity;
import br.com.eliel.gestao_vagas.modules.company.useCases.CreateCompanyUseCase;
import br.com.eliel.gestao_vagas.modules.company.useCases.DeactivateCompanyUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import br.com.eliel.gestao_vagas.modules.company.useCases.UpdateCompanyUseCase;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/company")
@Tag(name = "Empresa", description = "Gestão de empresas")
public class CompanyController {

    @Autowired
    private CreateCompanyUseCase createCompanyUseCase;
    
    @Autowired
    private DeactivateCompanyUseCase deactivateCompanyUseCase;

    @Autowired
    private UpdateCompanyUseCase updateCompanyUseCase;
    
    @PostMapping("/")
    @Operation(
        summary = "Cadastro de empresa", 
        description = "Rota responsável por cadastrar uma nova empresa no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Empresa cadastrada com sucesso",
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
                            "description": "Empresa especializada em desenvolvimento de software"
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
        description = "Dados da empresa a ser cadastrada",
        required = true,
        content = @Content(
            examples = {
                @ExampleObject(
                    name = "Cadastro de Empresa",
                    value = """
                    {
                        "name": "Tech Solutions",
                        "username": "techsolutions",
                        "email": "contato@techsolutions.com",
                        "password": "senha123456",
                        "website": "https://techsolutions.com",
                        "description": "Empresa especializada em desenvolvimento de software"
                    }
                    """
                )
            }
        )
    )
    public ResponseEntity<Object> create(@Valid @RequestBody CompanyEntity companyEntity) {
        try {
            var result = this.createCompanyUseCase.execute(companyEntity);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @DeleteMapping("/deactivate")
    @PreAuthorize("hasRole('COMPANY')")
    @Operation(
        summary = "Desativação de empresa", 
        description = "Rota responsável por deletar/desativar a própria empresa usando a senha",
        security = { @SecurityRequirement(name = "Bearer Authentication") }
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Empresa desativada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Senha incorreta"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Senha da empresa para confirmação da desativação",
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
        @Valid @RequestBody DeactivateCompanyDTO deactivateCompanyDTO,
        Authentication authentication
    ) {
        try {
            var companyId = UUID.fromString(authentication.getName());
            this.deactivateCompanyUseCase.execute(companyId, deactivateCompanyDTO.getPassword());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/")
    @PreAuthorize("hasRole('COMPANY')")
    @Operation(
        summary = "Atualização de dados da empresa", 
        description = "Rota responsável por atualizar os dados da própria empresa",
        security = { @SecurityRequirement(name = "Bearer Authentication") }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Empresa atualizada com sucesso",
            content = @Content(
                examples = {
                    @ExampleObject(
                        name = "Resposta de Sucesso",
                        value = """
                        {
                            "id": "123e4567-e89b-12d3-a456-426614174000",
                            "name": "Tech Solutions Atualizada",
                            "username": "techsolutions",
                            "email": "novo@techsolutions.com",
                            "website": "https://techsolutions.com.br",
                            "description": "Empresa atualizada especializada em desenvolvimento de software"
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
        description = "Dados da empresa a serem atualizados",
        required = true,
        content = @Content(
            examples = {
                @ExampleObject(
                    name = "Atualização de Empresa",
                    value = """
                    {
                        "name": "Tech Solutions Atualizada",
                        "email": "novo@techsolutions.com",
                        "website": "https://techsolutions.com.br",
                        "description": "Empresa atualizada especializada em desenvolvimento de software"
                    }
                    """
                )
            }
        )
    )
    public ResponseEntity<Object> update(
        @Valid @RequestBody UpdateCompanyDTO updateCompanyDTO,
        Authentication authentication
    ) {
        try {
            var companyId = UUID.fromString(authentication.getName());
            var updatedCompany = this.updateCompanyUseCase.execute(companyId, updateCompanyDTO);
            return ResponseEntity.ok().body(updatedCompany);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
