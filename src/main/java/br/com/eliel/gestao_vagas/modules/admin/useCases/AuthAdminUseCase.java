package br.com.eliel.gestao_vagas.modules.admin.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.eliel.gestao_vagas.exceptions.AuthenticationException;
import br.com.eliel.gestao_vagas.modules.admin.dto.AuthAdminDTO;
import br.com.eliel.gestao_vagas.modules.admin.dto.AuthAdminResponseDTO;
import br.com.eliel.gestao_vagas.modules.admin.repositories.AdminRepository;
import br.com.eliel.gestao_vagas.providers.JWTProvider;

import java.util.Arrays;

@Service
public class AuthAdminUseCase {
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JWTProvider jwtProvider;
    
    public AuthAdminResponseDTO execute(AuthAdminDTO authAdminDTO) {
        var admin = this.adminRepository.findByUsername(authAdminDTO.getUsername())
            .orElseThrow(() -> {
                throw new AuthenticationException();
            });
            
        var passwordMatches = this.passwordEncoder.matches(authAdminDTO.getPassword(), admin.getPassword());
        
        if (!passwordMatches) {
            throw new AuthenticationException();
        }
        
        var token = jwtProvider.generateToken(admin.getId().toString(), Arrays.asList("ADMIN"));
        
        return AuthAdminResponseDTO.builder()
            .token(token)
            .id(admin.getId())
            .username(admin.getUsername())
            .build();
    }
}
