package br.com.eliel.gestao_vagas.modules.jobs.useCases;

import br.com.eliel.gestao_vagas.modules.company.entites.CompanyEntity;
import br.com.eliel.gestao_vagas.modules.company.repositories.CompanyRepository;
import br.com.eliel.gestao_vagas.modules.jobs.dto.JobsDTO;
import br.com.eliel.gestao_vagas.modules.jobs.entites.JobEntity;
import br.com.eliel.gestao_vagas.modules.jobs.entites.TipoContrato;
import br.com.eliel.gestao_vagas.modules.jobs.repositories.JobsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobsUseCasesTest {

    @InjectMocks
    private JobsUseCases jobsUseCases;

    @Mock
    private JobsRepository jobsRepository;

    @Mock
    private CompanyRepository companyRepository;

    private UUID companyId;
    private JobsDTO jobsDTO;
    private CompanyEntity company;

    @BeforeEach
    void setUp() {
        companyId = UUID.randomUUID();
        company = CompanyEntity.builder().id(companyId).name("Empresa X").build();
        jobsDTO = JobsDTO.builder()
                .titulo("Desenvolvedor Java")
                .descricao("Desenvolvimento de sistemas")
                .areaAtuacao("TI")
                .requisitos(List.of("Java", "Spring"))
                .tipoContrato(TipoContrato.CLT)
                .localizacao("São Paulo")
                .salario(new BigDecimal("5000"))
                .beneficios("VR, VT, Plano de Saúde")
                .build();
    }

    @Test
    void shouldCreateJobSuccessfully() {
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        
        JobEntity expectedJob = JobEntity.builder()
                .titulo(jobsDTO.getTitulo())
                .descricao(jobsDTO.getDescricao())
                .areaAtuacao(jobsDTO.getAreaAtuacao())
                .requisitos(jobsDTO.getRequisitos())
                .tipoContrato(jobsDTO.getTipoContrato())
                .localizacao(jobsDTO.getLocalizacao())
                .company(company)
                .salario(jobsDTO.getSalario())
                .beneficios(jobsDTO.getBeneficios())
                .active(true)
                .build();
        
        when(jobsRepository.save(any(JobEntity.class))).thenReturn(expectedJob);

        JobEntity result = jobsUseCases.execute(jobsDTO, companyId);

        assertEquals(jobsDTO.getTitulo(), result.getTitulo());
        assertEquals(company, result.getCompany());
        assertTrue(result.isActive());
        verify(jobsRepository).save(any(JobEntity.class));
    }

    @Test
    void shouldThrowExceptionWhenCompanyNotFound() {
        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            jobsUseCases.execute(jobsDTO, companyId);
        });

        assertEquals("Empresa não encontrada", exception.getMessage());
        verify(jobsRepository, never()).save(any());
    }
}