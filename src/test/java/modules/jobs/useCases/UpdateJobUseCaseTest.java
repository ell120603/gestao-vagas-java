package modules.jobs.useCases;

import br.com.eliel.gestao_vagas.exceptions.AuthenticationException;
import br.com.eliel.gestao_vagas.modules.company.entites.CompanyEntity;
import br.com.eliel.gestao_vagas.modules.jobs.dto.UpdateJobDTO;
import br.com.eliel.gestao_vagas.modules.jobs.entites.JobEntity;
import br.com.eliel.gestao_vagas.modules.jobs.entites.TipoContrato;
import br.com.eliel.gestao_vagas.modules.jobs.repositories.JobsRepository;
import br.com.eliel.gestao_vagas.modules.jobs.useCases.UpdateJobUseCase;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UpdateJobUseCaseTest {

    @InjectMocks
    private UpdateJobUseCase updateJobUseCase;

    @Mock
    private JobsRepository jobsRepository;

    private UUID jobId;
    private UUID companyId;
    private CompanyEntity company;
    private JobEntity jobEntity;

    @BeforeEach
    void setUp() {
        jobId = UUID.randomUUID();
        companyId = UUID.randomUUID();
        company = new CompanyEntity();
        company.setId(companyId);
        jobEntity = JobEntity.builder()
                .id(jobId)
                .company(company)
                .titulo("Antigo Título")
                .descricao("Antiga descrição")
                .areaAtuacao("TI")
                .requisitos(List.of("Java"))
                .tipoContrato(TipoContrato.CLT)
                .localizacao("SP")
                .salario(new BigDecimal("5000"))
                .beneficios("VR")
                .build();
    }

    @Test
    void testExecuteSuccess() {
        UpdateJobDTO updateJobDTO = UpdateJobDTO.builder()
                .titulo("Novo Título")
                .descricao("Nova descrição")
                .areaAtuacao("Engenharia")
                .requisitos(List.of("Spring"))
                .tipoContrato(TipoContrato.PJ)
                .localizacao("RJ")
                .salario(new BigDecimal("8000"))
                .beneficios("VT")
                .build();

        when(jobsRepository.findById(jobId)).thenReturn(Optional.of(jobEntity));
        when(jobsRepository.save(any(JobEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        JobEntity result = updateJobUseCase.execute(jobId, companyId, updateJobDTO);

        assertNotNull(result);
        assertEquals("Novo Título", result.getTitulo());
        assertEquals("Nova descrição", result.getDescricao());
        assertEquals("Engenharia", result.getAreaAtuacao());
        assertEquals(List.of("Spring"), result.getRequisitos());
        assertEquals(TipoContrato.PJ, result.getTipoContrato());
        assertEquals("RJ", result.getLocalizacao());
        assertEquals(new BigDecimal("8000"), result.getSalario());
        assertEquals("VT", result.getBeneficios());
        verify(jobsRepository).findById(jobId);
        verify(jobsRepository).save(jobEntity);
    }

    @Test
    void testExecutePartialUpdate() {
        UpdateJobDTO updateJobDTO = UpdateJobDTO.builder()
                .titulo("Só o título")
                .build();

        when(jobsRepository.findById(jobId)).thenReturn(Optional.of(jobEntity));
        when(jobsRepository.save(any(JobEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        JobEntity result = updateJobUseCase.execute(jobId, companyId, updateJobDTO);

        assertEquals("Só o título", result.getTitulo());
        assertEquals("Antiga descrição", result.getDescricao());
        verify(jobsRepository).findById(jobId);
        verify(jobsRepository).save(jobEntity);
    }

    @Test
    void testExecuteJobNotFound() {
        UpdateJobDTO updateJobDTO = UpdateJobDTO.builder().titulo("Novo").build();
        when(jobsRepository.findById(jobId)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                updateJobUseCase.execute(jobId, companyId, updateJobDTO));
        assertEquals("Vaga não encontrada", ex.getMessage());
        verify(jobsRepository).findById(jobId);
        verify(jobsRepository, never()).save(any());
    }

    @Test
    void testExecuteNoPermission() {
        CompanyEntity otherCompany = new CompanyEntity();
        otherCompany.setId(UUID.randomUUID());
        JobEntity jobOther = JobEntity.builder().id(jobId).company(otherCompany).build();
        UpdateJobDTO updateJobDTO = UpdateJobDTO.builder().titulo("Novo").build();

        when(jobsRepository.findById(jobId)).thenReturn(Optional.of(jobOther));

        AuthenticationException ex = assertThrows(AuthenticationException.class, () ->
                updateJobUseCase.execute(jobId, companyId, updateJobDTO));
        assertEquals("Você não tem permissão para editar esta vaga", ex.getMessage());
        verify(jobsRepository).findById(jobId);
        verify(jobsRepository, never()).save(any());
    }
}
