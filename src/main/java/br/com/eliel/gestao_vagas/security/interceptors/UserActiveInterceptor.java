package br.com.eliel.gestao_vagas.security.interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import br.com.eliel.gestao_vagas.modules.candidate.repositories.CandidateRepository;
import br.com.eliel.gestao_vagas.modules.company.repositories.CompanyRepository;
import br.com.eliel.gestao_vagas.exceptions.UserInactiveException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;

@Component
public class UserActiveInterceptor implements HandlerInterceptor {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        var companyId = request.getAttribute("company_id");
        var candidateId = request.getAttribute("candidate_id");

        if (companyId != null) {
            var company = this.companyRepository.findById(UUID.fromString(companyId.toString()))
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));

            if (!company.isActive()) {
                throw new UserInactiveException();
            }
        }

        if (candidateId != null) {
            var candidate = this.candidateRepository.findById(UUID.fromString(candidateId.toString()))
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado"));

            if (!candidate.isActive()) {
                throw new UserInactiveException();
            }
        }

        return true;
    }
} 