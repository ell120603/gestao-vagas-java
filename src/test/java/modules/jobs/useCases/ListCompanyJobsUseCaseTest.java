package br.com.eliel.gestao_vagas.modules.jobs.useCases;

import br.com.eliel.gestao_vagas.modules.company.entites.CompanyEntity;
import br.com.eliel.gestao_vagas.modules.jobs.dto.JobsResponseDTO;
import br.com.eliel.gestao_vagas.modules.jobs.entites.JobEntity;
import br.com.eliel.gestao_vagas.modules.jobs.entites.TipoContrato;
import br.com.eliel.gestao_vagas.modules.jobs.repositories.JobsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListCompanyJobsUseCaseTest {

    @InjectMocks
    private ListCompanyJobsUseCase listCompanyJobsUseCase;

    @Mock
    private JobsRepository jobsRepository;

    private UUID companyId;
    private CompanyEntity company;
    private JobEntity job1;
    private JobEntity job2;

    @BeforeEach
    void setUp() {
        companyId = UUID.randomUUID();
        company = CompanyEntity.builder().id(companyId).name("Empresa X").build();
        job1 = JobEntity.builder()
                .id(UUID.randomUUID())
                .titulo("Desenvolvedor")
                .descricao("Desenvolve sistemas")
                .areaAtuacao("TI")
                .requisitos(List.of("Java", "Spring"))
                .tipoContrato(TipoContrato.CLT)
                .localizacao("SP")
                .company(company)
                .salario(new BigDecimal("5000"))
                .beneficios("VR, VT")
                .createdAt(LocalDateTime.now())
                .active(true)
                .build();

        job2 = JobEntity.builder()
                .id(UUID.randomUUID())
                .titulo("Analista")
                .descricao("Analisa sistemas")
                .areaAtuacao("RH")
                .requisitos(List.of("Excel"))
                .tipoContrato(TipoContrato.PJ)
                .localizacao("RJ")
                .company(company)
                .salario(new BigDecimal("3000"))
                .beneficios("Plano de Saúde")
                .createdAt(LocalDateTime.now())
                .active(true)
                .build();
    }

    @Test
    void testExecuteReturnsJobsResponseDTOList() {
        when(jobsRepository.findByCompanyId(companyId)).thenReturn(List.of(job1, job2));

        List<JobsResponseDTO> result = listCompanyJobsUseCase.execute(companyId);

        assertEquals(2, result.size());

        JobsResponseDTO dto1 = result.get(0);
        assertEquals(job1.getId(), dto1.getId());
        assertEquals(job1.getTitulo(), dto1.getTitulo());
        assertEquals(job1.getDescricao(), dto1.getDescricao());
        assertEquals(job1.getAreaAtuacao(), dto1.getAreaAtuacao());
        assertEquals(job1.getRequisitos(), dto1.getRequisitos());
        assertEquals(job1.getTipoContrato(), dto1.getTipoContrato());
        assertEquals(job1.getLocalizacao(), dto1.getLocalizacao());
        assertEquals(job1.getCompany().getName(), dto1.getCompanyName());
        assertEquals(job1.getSalario(), dto1.getSalario());
        assertEquals(job1.getBeneficios(), dto1.getBeneficios());
        assertEquals(job1.getCreatedAt(), dto1.getCreatedAt());

        JobsResponseDTO dto2 = result.get(1);
        assertEquals(job2.getId(), dto2.getId());
        assertEquals(job2.getTitulo(), dto2.getTitulo());
        assertEquals(job2.getDescricao(), dto2.getDescricao());
        assertEquals(job2.getAreaAtuacao(), dto2.getAreaAtuacao());
        assertEquals(job2.getRequisitos(), dto2.getRequisitos());
        assertEquals(job2.getTipoContrato(), dto2.getTipoContrato());
        assertEquals(job2.getLocalizacao(), dto2.getLocalizacao());
        assertEquals(job2.getCompany().getName(), dto2.getCompanyName());
        assertEquals(job2.getSalario(), dto2.getSalario());
        assertEquals(job2.getBeneficios(), dto2.getBeneficios());
        assertEquals(job2.getCreatedAt(), dto2.getCreatedAt());

        verify(jobsRepository).findByCompanyId(companyId);
    }

    @Test
    void testExecuteReturnsEmptyList() {
        when(jobsRepository.findByCompanyId(companyId)).thenReturn(Collections.emptyList());

        List<JobsResponseDTO> result = listCompanyJobsUseCase.execute(companyId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(jobsRepository).findByCompanyId(companyId);
    }
}
