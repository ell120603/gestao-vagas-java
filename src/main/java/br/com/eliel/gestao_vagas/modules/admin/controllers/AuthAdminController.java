package br.com.eliel.gestao_vagas.modules.admin.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.eliel.gestao_vagas.modules.admin.dto.AuthAdminDTO;
import br.com.eliel.gestao_vagas.modules.admin.useCases.AuthAdminUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@Tag(name = "Administrador", description = "Autenticação de administrador")
public class AuthAdminController {

    @Autowired
    private AuthAdminUseCase authAdminUseCase;
    
    @PostMapping("/admin")
    @Operation(summary = "Autenticação de administrador", description = "Rota responsável por autenticar um administrador")
    public ResponseEntity<Object> auth(@Valid @RequestBody AuthAdminDTO authAdminDTO) {
        try {
            var result = this.authAdminUseCase.execute(authAdminDTO);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
