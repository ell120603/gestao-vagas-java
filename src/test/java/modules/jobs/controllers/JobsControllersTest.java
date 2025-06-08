package br.com.eliel.gestao_vagas.modules.jobs.controllers;

import br.com.eliel.gestao_vagas.modules.jobs.dto.*;
import br.com.eliel.gestao_vagas.modules.jobs.entites.TipoContrato;
import br.com.eliel.gestao_vagas.modules.jobs.useCases.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class JobsControllersTest {

    @InjectMocks
    private JobsControllers jobsControllers;

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
        JobsDTO jobsDTO = JobsDTO.builder().titulo("Dev").build();
        JobsResponseDTO responseDTO = JobsResponseDTO.builder().id(jobId).titulo("Dev").build();

        when(jobsUseCases.execute(eq(jobsDTO), eq(companyId))).thenReturn(responseDTO);

        ResponseEntity<Object> response = jobsControllers.create(jobsDTO, request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void testCreateException() {
        JobsDTO jobsDTO = JobsDTO.builder().titulo("Dev").build();
        when(jobsUseCases.execute(any(), any())).thenThrow(new RuntimeException("Erro"));

        ResponseEntity<Object> response = jobsControllers.create(jobsDTO, request);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Erro", response.getBody());
    }

    @Test
    void testList() {
        List<JobsResponseDTO> jobs = List.of(JobsResponseDTO.builder().id(jobId).build());
        when(listAllJobsUseCase.execute()).thenReturn(jobs);

        ResponseEntity<List<JobsResponseDTO>> response = jobsControllers.list();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(jobs, response.getBody());
    }

    @Test
    void testUpdateSuccess() {
        UpdateJobDTO updateJobDTO = UpdateJobDTO.builder().titulo("Novo").build();
        JobsResponseDTO responseDTO = JobsResponseDTO.builder().id(jobId).titulo("Novo").build();

        when(updateJobUseCase.execute(eq(jobId), eq(companyId), eq(updateJobDTO))).thenReturn(responseDTO);

        ResponseEntity<Object> response = jobsControllers.update(jobId, updateJobDTO, request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void testUpdateException() {
        UpdateJobDTO updateJobDTO = UpdateJobDTO.builder().titulo("Novo").build();
        when(updateJobUseCase.execute(any(), any(), any())).thenThrow(new RuntimeException("Erro"));

        ResponseEntity<Object> response = jobsControllers.update(jobId, updateJobDTO, request);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Erro", response.getBody());
    }

    @Test
    void testCloseSuccess() {
        JobsResponseDTO responseDTO = JobsResponseDTO.builder().id(jobId).titulo("Fechada").build();
        when(closeJobUseCase.execute(eq(jobId), eq(companyId))).thenReturn(responseDTO);

        ResponseEntity<Object> response = jobsControllers.close(jobId, request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void testCloseException() {
        when(closeJobUseCase.execute(any(), any())).thenThrow(new RuntimeException("Erro"));

        ResponseEntity<Object> response = jobsControllers.close(jobId, request);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Erro", response.getBody());
    }

    @Test
    void testApplySuccess() {
        String result = "Candidatado";
        when(applyJobUseCase.execute(eq(candidateId), eq(jobId))).thenReturn(result);

        ResponseEntity<Object> response = jobsControllers.apply(jobId, request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(result, response.getBody());
    }

    @Test
    void testApplyException() {
        when(applyJobUseCase.execute(any(), any())).thenThrow(new RuntimeException("Erro"));

        ResponseEntity<Object> response = jobsControllers.apply(jobId, request);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Erro", response.getBody());
    }

    @Test
    void testListCompanyJobs() {
        List<JobsResponseDTO> jobs = List.of(JobsResponseDTO.builder().id(jobId).build());
        when(listCompanyJobsUseCase.execute(eq(companyId))).thenReturn(jobs);

        ResponseEntity<List<JobsResponseDTO>> response = jobsControllers.listCompanyJobs(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(jobs, response.getBody());
    }

    @Test
    void testDeleteSuccess() {
        DeleteJobDTO deleteJobDTO = DeleteJobDTO.builder().password("senha").build();

        ResponseEntity<Object> response = jobsControllers.delete(jobId, deleteJobDTO, request);

        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(deleteJobUseCase).execute(eq(jobId), eq(companyId), eq("senha"));
    }

    @Test
    void testDeleteException() {
        DeleteJobDTO deleteJobDTO = DeleteJobDTO.builder().password("senha").build();
        doThrow(new RuntimeException("Erro")).when(deleteJobUseCase).execute(any(), any(), any());

        ResponseEntity<Object> response = jobsControllers.delete(jobId, deleteJobDTO, request);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Erro", response.getBody());
    }

    @Test
    void testReactivateSuccess() {
        JobsResponseDTO responseDTO = JobsResponseDTO.builder().id(jobId).titulo("Reativada").build();
        when(reactivateJobUseCase.execute(eq(jobId), eq(companyId))).thenReturn(responseDTO);

        ResponseEntity<Object> response = jobsControllers.reactivate(jobId, request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void testReactivateException() {
        when(reactivateJobUseCase.execute(any(), any())).thenThrow(new RuntimeException("Erro"));

        ResponseEntity<Object> response = jobsControllers.reactivate(jobId, request);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Erro", response.getBody());
    }

    @Test
    void testFilter() {
        List<JobsResponseDTO> jobs = List.of(JobsResponseDTO.builder().id(jobId).build());
        when(filterJobsUseCase.execute(any())).thenReturn(jobs);

        ResponseEntity<List<JobsResponseDTO>> response = jobsControllers.filter(
                "Desenvolvimento",
                new String[]{"Java"},
                "CLT",
                "SP",
                "1000",
                "5000"
        );

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(jobs, response.getBody());
    }

    @Test
    void testListCandidateJobs() {
        List<JobsResponseDTO> jobs = List.of(JobsResponseDTO.builder().id(jobId).build());
        when(listCandidateJobsUseCase.execute(eq(candidateId))).thenReturn(jobs);

        ResponseEntity<List<JobsResponseDTO>> response = jobsControllers.listCandidateJobs(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(jobs, response.getBody());
    }

    @Test
    void testListJobCandidates() {
        List<CandidateJobResponseDTO> candidates = List.of(CandidateJobResponseDTO.builder().candidateId(candidateId).build());
        when(listJobCandidatesUseCase.execute(eq(jobId), eq(companyId))).thenReturn(candidates);

        ResponseEntity<List<CandidateJobResponseDTO>> response = jobsControllers.listJobCandidates(jobId, request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(candidates, response.getBody());
    }

    @Test
    void testCreate_ShouldValidateDTO() {
        JobsDTO invalidDTO = JobsDTO.builder()
                .descricao("Descrição")
                .salario(BigDecimal.valueOf(1000))
                .build();
        
        var violations = validator.validate(invalidDTO);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("titulo", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void testCreate_ShouldRequireCompanyRole() {
        when(jobsUseCases.execute(any(), any())).thenThrow(new AccessDeniedException("Acesso negado"));
        JobsDTO jobsDTO = JobsDTO.builder().titulo("Dev").build();
        
        assertThrows(AccessDeniedException.class, () -> jobsControllers.create(jobsDTO, request));
    }

    @Test
    void testUpdate_ShouldValidateUpdateDTO() {
        UpdateJobDTO invalidDTO = UpdateJobDTO.builder()
                .descricao("Nova descrição")
                .build();
        
        var violations = validator.validate(invalidDTO);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("titulo", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void testDelete_ShouldValidatePassword() {
        DeleteJobDTO invalidDTO = DeleteJobDTO.builder().build();
        
        var violations = validator.validate(invalidDTO);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("password", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void testListCompanyJobs_ShouldRequireCompanyRole() {
        when(listCompanyJobsUseCase.execute(any())).thenThrow(new AccessDeniedException("Acesso negado"));
        assertThrows(AccessDeniedException.class, () -> jobsControllers.listCompanyJobs(request));
    }

    @Test
    void testApplyJob_ShouldRequireCandidateRole() {
        when(applyJobUseCase.execute(any(), any())).thenThrow(new AccessDeniedException("Acesso negado"));
        assertThrows(AccessDeniedException.class, () -> jobsControllers.apply(jobId, request));
    }

    @Test
    void testListCandidateJobs_ShouldRequireCandidateRole() {
        when(listCandidateJobsUseCase.execute(any())).thenThrow(new AccessDeniedException("Acesso negado"));
        assertThrows(AccessDeniedException.class, () -> jobsControllers.listCandidateJobs(request));
    }

    @Test
    void testListJobCandidates_ShouldRequireCompanyRole() {
        when(listJobCandidatesUseCase.execute(any(), any())).thenThrow(new AccessDeniedException("Acesso negado"));
        assertThrows(AccessDeniedException.class, () -> jobsControllers.listJobCandidates(jobId, request));
    }
}