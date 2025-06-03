package br.com.eliel.gestao_vagas.modules.company.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.eliel.gestao_vagas.modules.company.dto.AuthCompanyDTO;
import br.com.eliel.gestao_vagas.modules.company.useCases.AuthCompanyUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@Tag(name = "Empresa", description = "Autenticação de empresa")
public class AuthCompanyController {

    @Autowired
    private AuthCompanyUseCase authCompanyUseCase;
    
    @PostMapping("/company")
    @Operation(
        summary = "Autenticação de empresa", 
        description = "Rota responsável por autenticar uma empresa e retornar um token JWT para acesso às rotas protegidas"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Autenticação realizada com sucesso",
            content = @Content(
                examples = {
                    @ExampleObject(
                        name = "Resposta de Sucesso",
                        value = """
                        {
                            "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                            "expires_in": 3600000
                        }
                        """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Credenciais inválidas",
            content = @Content(
                examples = {
                    @ExampleObject(
                        name = "Erro de Autenticação",
                        value = """
                        {
                            "message": "Usuário ou senha incorretos"
                        }
                        """
                    )
                }
            )
        )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Credenciais de autenticação da empresa",
        required = true,
        content = @Content(
            examples = {
                @ExampleObject(
                    name = "Credenciais de Empresa",
                    value = """
                    {
                        "username": "techsolutions",
                        "password": "senha123456"
                    }
                    """
                )
            }
        )
    )
    public ResponseEntity<Object> auth(@Valid @RequestBody AuthCompanyDTO authCompanyDTO) {
        try {
            var result = this.authCompanyUseCase.execute(authCompanyDTO);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}