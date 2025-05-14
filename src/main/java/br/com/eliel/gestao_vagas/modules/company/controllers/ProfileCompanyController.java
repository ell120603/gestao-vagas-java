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
    @Operation(summary = "Perfil da empresa", description = "Essa função é responsável por buscar as informações do perfil da empresa", security = {
            @SecurityRequirement(name = "Bearer Authentication")
    })
    public ResponseEntity<ProfileCompanyResponseDTO> profile(HttpServletRequest request) {
        var companyId = request.getAttribute("company_id");
        var profile = this.profileCompanyUseCase.execute(UUID.fromString(companyId.toString()));
        return ResponseEntity.ok().body(profile);
    }
} 