package modules.jobs.controllers;

import br.com.eliel.gestao_vagas.modules.jobs.dto.*;
import br.com.eliel.gestao_vagas.modules.jobs.entites.JobEntity;
import br.com.eliel.gestao_vagas.modules.jobs.entites.TipoContrato;
import br.com.eliel.gestao_vagas.modules.jobs.entites.CandidateJobEntity;
import br.com.eliel.gestao_vagas.modules.jobs.useCases.*;
import br.com.eliel.gestao_vagas.modules.jobs.controllers.JobsControllers;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class JobsControllersTest {

    @InjectMocks
    private JobsControllers jobsController;

    @Mock private JobsUseCases jobsUseCases;
    @Mock private ListAllJobsUseCase listAllJobsUseCase;
    @Mock private UpdateJobUseCase updateJobUseCase;
    @Mock private CloseJobUseCase closeJobUseCase;
    @Mock private ApplyJobUseCase applyJobUseCase;
    @Mock private ListCompanyJobsUseCase listCompanyJobsUseCase;
    @Mock private DeleteJobUseCase deleteJobUseCase;
    @Mock private ReactivateJobUseCase reactivateJobUseCase;
    @Mock private FilterJobsUseCase filterJobsUseCase;
    @Mock private ListCandidateJobsUseCase listCandidateJobsUseCase;
    @Mock private ListJobCandidatesUseCase listJobCandidatesUseCase;

    @Mock private HttpServletRequest request;

    private final UUID companyId = UUID.randomUUID();
    private final UUID candidateId = UUID.randomUUID();
    private final UUID jobId = UUID.randomUUID();
    private Validator validator;

    @BeforeEach
    void setup() {
        when(request.getAttribute("company_id")).thenReturn(companyId.toString());
        when(request.getAttribute("candidate_id")).thenReturn(candidateId.toString());
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testCreateSuccess() {
        JobsDTO jobsDTO = JobsDTO.builder()
            .titulo("Dev")
            .descricao("Descrição da vaga")
            .areaAtuacao("TI")
            .requisitos(List.of("Java", "Spring"))
            .tipoContrato(TipoContrato.CLT)
            .localizacao("São Paulo")
            .salario(new BigDecimal("5000"))
            .beneficios("VR, VT")
            .build();

        JobEntity jobEntity = JobEntity.builder()
            .id(jobId)
            .titulo("Dev")
            .build();

        when(jobsUseCases.execute(eq(jobsDTO), eq(companyId))).thenReturn(jobEntity);

        ResponseEntity<Object> response = jobsController.create(jobsDTO, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(jobEntity, response.getBody());
        verify(jobsUseCases).execute(jobsDTO, companyId);
    }

    @Test
    void testCreateException() {
        JobsDTO jobsDTO = JobsDTO.builder()
            .titulo("Dev")
            .build();

        when(jobsUseCases.execute(any(), any())).thenThrow(new RuntimeException("Erro"));

        ResponseEntity<Object> response = jobsController.create(jobsDTO, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erro", response.getBody());
        verify(jobsUseCases).execute(jobsDTO, companyId);
    }

    @Test
    void testList() {
        List<JobsResponseDTO> jobs = List.of(
            JobsResponseDTO.builder()
                .id(jobId)
                .titulo("Dev")
                .build()
        );

        when(listAllJobsUseCase.execute()).thenReturn(jobs);

        ResponseEntity<List<JobsResponseDTO>> response = jobsController.list();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(jobs, response.getBody());
        verify(listAllJobsUseCase).execute();
    }

    @Test
    void testUpdateSuccess() {
        UpdateJobDTO updateJobDTO = UpdateJobDTO.builder()
            .titulo("Novo")
            .descricao("Nova descrição")
            .build();

        JobEntity jobEntity = JobEntity.builder()
            .id(jobId)
            .titulo("Novo")
            .build();

        when(updateJobUseCase.execute(eq(jobId), eq(companyId), eq(updateJobDTO))).thenReturn(jobEntity);

        ResponseEntity<Object> response = jobsController.update(jobId, updateJobDTO, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(jobEntity, response.getBody());
        verify(updateJobUseCase).execute(jobId, companyId, updateJobDTO);
    }

    @Test
    void testUpdateException() {
        UpdateJobDTO updateJobDTO = UpdateJobDTO.builder()
            .titulo("Novo")
            .build();

        when(updateJobUseCase.execute(any(), any(), any())).thenThrow(new RuntimeException("Erro"));

        ResponseEntity<Object> response = jobsController.update(jobId, updateJobDTO, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erro", response.getBody());
        verify(updateJobUseCase).execute(jobId, companyId, updateJobDTO);
    }

    @Test
    void testCloseSuccess() {
        JobEntity jobEntity = JobEntity.builder()
            .id(jobId)
            .titulo("Fechada")
            .build();

        when(closeJobUseCase.execute(eq(jobId), eq(companyId))).thenReturn(jobEntity);

        ResponseEntity<Object> response = jobsController.close(jobId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(jobEntity, response.getBody());
        verify(closeJobUseCase).execute(jobId, companyId);
    }

    @Test
    void testCloseException() {
        when(closeJobUseCase.execute(any(), any())).thenThrow(new RuntimeException("Erro"));

        ResponseEntity<Object> response = jobsController.close(jobId, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erro", response.getBody());
        verify(closeJobUseCase).execute(jobId, companyId);
    }

    @Test
    void testApplySuccess() {
        CandidateJobEntity candidateJob = CandidateJobEntity.builder()
            .id(UUID.randomUUID())
            .candidateId(candidateId)
            .jobId(jobId)
            .build();

        when(applyJobUseCase.execute(eq(candidateId), eq(jobId))).thenReturn(candidateJob);

        ResponseEntity<Object> response = jobsController.apply(jobId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(candidateJob, response.getBody());
        verify(applyJobUseCase).execute(candidateId, jobId);
    }

    @Test
    void testApplyException() {
        when(applyJobUseCase.execute(any(), any())).thenThrow(new RuntimeException("Erro"));

        ResponseEntity<Object> response = jobsController.apply(jobId, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erro", response.getBody());
        verify(applyJobUseCase).execute(candidateId, jobId);
    }

    @Test
    void testListCompanyJobs() {
        List<JobsResponseDTO> jobs = List.of(
            JobsResponseDTO.builder()
                .id(jobId)
                .titulo("Dev")
                .build()
        );

        when(listCompanyJobsUseCase.execute(eq(companyId))).thenReturn(jobs);

        ResponseEntity<List<JobsResponseDTO>> response = jobsController.listCompanyJobs(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(jobs, response.getBody());
        verify(listCompanyJobsUseCase).execute(companyId);
    }

    @Test
    void testDeleteSuccess() {
        DeleteJobDTO deleteJobDTO = DeleteJobDTO.builder()
            .password("senha")
            .build();

        ResponseEntity<Object> response = jobsController.delete(jobId, deleteJobDTO, request);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(deleteJobUseCase).execute(jobId, companyId, "senha");
    }

    @Test
    void testDeleteException() {
        DeleteJobDTO deleteJobDTO = DeleteJobDTO.builder()
            .password("senha")
            .build();

        doThrow(new RuntimeException("Erro")).when(deleteJobUseCase).execute(any(), any(), any());

        ResponseEntity<Object> response = jobsController.delete(jobId, deleteJobDTO, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erro", response.getBody());
        verify(deleteJobUseCase).execute(jobId, companyId, "senha");
    }

    @Test
    void testReactivateSuccess() {
        JobEntity jobEntity = JobEntity.builder()
            .id(jobId)
            .titulo("Reativada")
            .build();

        when(reactivateJobUseCase.execute(eq(jobId), eq(companyId))).thenReturn(jobEntity);

        ResponseEntity<Object> response = jobsController.reactivate(jobId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(jobEntity, response.getBody());
        verify(reactivateJobUseCase).execute(jobId, companyId);
    }

    @Test
    void testReactivateException() {
        when(reactivateJobUseCase.execute(any(), any())).thenThrow(new RuntimeException("Erro"));

        ResponseEntity<Object> response = jobsController.reactivate(jobId, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erro", response.getBody());
        verify(reactivateJobUseCase).execute(jobId, companyId);
    }

    @Test
    void testFilter() {
        List<JobsResponseDTO> jobs = List.of(
            JobsResponseDTO.builder()
                .id(jobId)
                .titulo("Dev")
                .build()
        );

        JobFilterDTO filterDTO = JobFilterDTO.builder()
            .areaAtuacao("TI")
            .requisitos(List.of("Java"))
            .tipoContrato(TipoContrato.CLT)
            .localizacao("São Paulo")
            .salarioMinimo(new BigDecimal("5000"))
            .salarioMaximo(new BigDecimal("10000"))
            .build();

        when(filterJobsUseCase.execute(any(JobFilterDTO.class))).thenReturn(jobs);

        ResponseEntity<List<JobsResponseDTO>> response = jobsController.filter(
            "TI",
            new String[]{"Java"},
            "CLT",
            "São Paulo",
            "5000",
            "10000"
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(jobs, response.getBody());
        verify(filterJobsUseCase).execute(any(JobFilterDTO.class));
    }

    @Test
    void testListCandidateJobs() {
        List<JobsResponseDTO> jobs = List.of(
            JobsResponseDTO.builder()
                .id(jobId)
                .titulo("Dev")
                .build()
        );

        when(listCandidateJobsUseCase.execute(eq(candidateId))).thenReturn(jobs);

        ResponseEntity<List<JobsResponseDTO>> response = jobsController.listCandidateJobs(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(jobs, response.getBody());
        verify(listCandidateJobsUseCase).execute(candidateId);
    }

    @Test
    void testListJobCandidates() {
        List<CandidateJobResponseDTO> candidates = List.of(
            CandidateJobResponseDTO.builder()
                .id(UUID.randomUUID())
                .name("Candidato")
                .build()
        );

        when(listJobCandidatesUseCase.execute(eq(jobId), eq(companyId))).thenReturn(candidates);

        ResponseEntity<List<CandidateJobResponseDTO>> response = jobsController.listJobCandidates(jobId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(candidates, response.getBody());
        verify(listJobCandidatesUseCase).execute(jobId, companyId);
    }

    @Test
    void testCreate_ShouldValidateDTO() {
        JobsDTO jobsDTO = JobsDTO.builder().build();
        var violations = validator.validate(jobsDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testUpdate_ShouldValidateUpdateDTO() {
        UpdateJobDTO updateJobDTO = UpdateJobDTO.builder().build();
        var violations = validator.validate(updateJobDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testDelete_ShouldValidatePassword() {
        DeleteJobDTO deleteJobDTO = DeleteJobDTO.builder().build();
        var violations = validator.validate(deleteJobDTO);
        assertFalse(violations.isEmpty());
    }
}