package br.com.eliel.gestao_vagas.modules.jobs.useCases;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.eliel.gestao_vagas.exceptions.AuthenticationException;
import br.com.eliel.gestao_vagas.modules.company.repositories.CompanyRepository;
import br.com.eliel.gestao_vagas.modules.jobs.repositories.JobsRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class DeleteJobUseCase {
    
    @Autowired
    private JobsRepository jobsRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void execute(UUID jobId, UUID companyId, String password) {
        var job = this.jobsRepository.findById(jobId)
            .orElseThrow(() -> new EntityNotFoundException("Vaga não encontrada"));

        if (!job.getCompany().getId().equals(companyId)) {
            throw new AuthenticationException("Você não tem permissão para deletar esta vaga");
        }

        var company = this.companyRepository.findById(companyId)
            .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada"));

        if (!passwordEncoder.matches(password, company.getPassword())) {
            throw new AuthenticationException("Senha incorreta");
        }

        this.jobsRepository.delete(job);
    }
} 