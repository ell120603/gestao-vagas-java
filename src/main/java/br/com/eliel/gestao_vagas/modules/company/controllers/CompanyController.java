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
    @Operation(summary = "Cadastro de empresa", description = "Rota responsável por cadastrar uma nova empresa")
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
