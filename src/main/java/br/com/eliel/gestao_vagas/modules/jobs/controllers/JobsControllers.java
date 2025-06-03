package br.com.eliel.gestao_vagas.modules.jobs.controllers;

import java.util.List;
import java.util.UUID;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import br.com.eliel.gestao_vagas.modules.jobs.dto.JobsDTO;
import br.com.eliel.gestao_vagas.modules.jobs.dto.JobsResponseDTO;
import br.com.eliel.gestao_vagas.modules.jobs.dto.UpdateJobDTO;
import br.com.eliel.gestao_vagas.modules.jobs.dto.JobFilterDTO;
import br.com.eliel.gestao_vagas.modules.jobs.entites.TipoContrato;
import br.com.eliel.gestao_vagas.modules.jobs.useCases.JobsUseCases;
import br.com.eliel.gestao_vagas.modules.jobs.useCases.ListAllJobsUseCase;
import br.com.eliel.gestao_vagas.modules.jobs.useCases.UpdateJobUseCase;
import br.com.eliel.gestao_vagas.modules.jobs.useCases.CloseJobUseCase;
import br.com.eliel.gestao_vagas.modules.jobs.useCases.ApplyJobUseCase;
import br.com.eliel.gestao_vagas.modules.jobs.useCases.ListCompanyJobsUseCase;
import br.com.eliel.gestao_vagas.modules.jobs.useCases.DeleteJobUseCase;
import br.com.eliel.gestao_vagas.modules.jobs.useCases.ReactivateJobUseCase;
import br.com.eliel.gestao_vagas.modules.jobs.useCases.FilterJobsUseCase;
import br.com.eliel.gestao_vagas.modules.jobs.dto.DeleteJobDTO;
import br.com.eliel.gestao_vagas.modules.jobs.useCases.ListCandidateJobsUseCase;
import br.com.eliel.gestao_vagas.modules.jobs.useCases.ListJobCandidatesUseCase;
import br.com.eliel.gestao_vagas.modules.jobs.dto.CandidateJobResponseDTO;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/job")
@Tag(name = "Vagas", description = "Gestão de vagas")
public class JobsControllers {
    @Autowired
    private JobsUseCases jobsUseCases;

    @Autowired
    private ListAllJobsUseCase listAllJobsUseCase;

    @Autowired
    private UpdateJobUseCase updateJobUseCase;

    @Autowired
    private CloseJobUseCase closeJobUseCase;

    @Autowired
    private ApplyJobUseCase applyJobUseCase;

    @Autowired
    private ListCompanyJobsUseCase listCompanyJobsUseCase;

    @Autowired
    private DeleteJobUseCase deleteJobUseCase;

    @Autowired
    private ReactivateJobUseCase reactivateJobUseCase;

    @Autowired
    private FilterJobsUseCase filterJobsUseCase;

    @Autowired
    private ListCandidateJobsUseCase listCandidateJobsUseCase;

    @Autowired
    private ListJobCandidatesUseCase listJobCandidatesUseCase;

    @PostMapping("/")
    @PreAuthorize("hasRole('COMPANY')")
    @Operation(
        summary = "Criação de vaga", 
        description = "Rota responsável por criar uma nova vaga",
        security = { @SecurityRequirement(name = "Bearer Authentication") }
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vaga criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Dados da vaga a ser criada",
        required = true,
        content = @Content(
            examples = {
                @ExampleObject(
                    name = "Vaga de Desenvolvedor",
                    value = """
                    {
                        "titulo": "Desenvolvedor Java",
                        "descricao": "Vaga para desenvolvedor Java com experiência em Spring Boot",
                        "areaAtuacao": "Desenvolvimento",
                        "requisitos": ["Java", "Spring Boot", "PostgreSQL"],
                        "tipoContrato": "CLT",
                        "localizacao": "São Paulo - Remoto",
                        "salario": 8000.00,
                        "beneficios": "Vale alimentação, Plano de saúde"
                    }
                    """
                )
            }
        )
    )
    public ResponseEntity<Object> create(@Valid @RequestBody JobsDTO jobsDTO, HttpServletRequest request) {
        var companyId = request.getAttribute("company_id");
        
        try {
            var job = this.jobsUseCases.execute(jobsDTO, UUID.fromString(companyId.toString()));
            return ResponseEntity.ok().body(job);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/")
    @Operation(
        summary = "Listagem de vagas", 
        description = "Rota responsável por listar todas as vagas ativas"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de vagas retornada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<List<JobsResponseDTO>> list() {
        var jobs = this.listAllJobsUseCase.execute();
        return ResponseEntity.ok().body(jobs);
    }

    @PutMapping("/{jobId}")
    @PreAuthorize("hasRole('COMPANY')")
    @Operation(
        summary = "Atualização de vaga", 
        description = "Rota responsável por atualizar uma vaga existente",
        security = { @SecurityRequirement(name = "Bearer Authentication") }
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vaga atualizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado"),
        @ApiResponse(responseCode = "404", description = "Vaga não encontrada")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Dados da vaga a ser atualizada",
        required = true,
        content = @Content(
            examples = {
                @ExampleObject(
                    name = "Atualização de Vaga",
                    value = """
                    {
                        "titulo": "Desenvolvedor Java Senior",
                        "descricao": "Vaga atualizada para desenvolvedor Java Senior",
                        "areaAtuacao": "Desenvolvimento",
                        "requisitos": ["Java", "Spring Boot", "PostgreSQL", "AWS"],
                        "tipoContrato": "CLT",
                        "localizacao": "São Paulo - Híbrido",
                        "salario": 12000.00,
                        "beneficios": "Vale alimentação, Plano de saúde, Plano odontológico"
                    }
                    """
                )
            }
        )
    )
    public ResponseEntity<Object> update(
        @PathVariable UUID jobId,
        @Valid @RequestBody UpdateJobDTO updateJobDTO,
        HttpServletRequest request
    ) {
        var companyId = request.getAttribute("company_id");
        
        try {
            var job = this.updateJobUseCase.execute(
                jobId,
                UUID.fromString(companyId.toString()),
                updateJobDTO
            );
            return ResponseEntity.ok().body(job);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{jobId}/close")
    @PreAuthorize("hasRole('COMPANY')")
    @Operation(
        summary = "Fechamento de vaga", 
        description = "Rota responsável por fechar uma vaga existente",
        security = { @SecurityRequirement(name = "Bearer Authentication") }
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vaga fechada com sucesso"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado"),
        @ApiResponse(responseCode = "404", description = "Vaga não encontrada")
    })
    public ResponseEntity<Object> close(
        @PathVariable UUID jobId,
        HttpServletRequest request
    ) {
        var companyId = request.getAttribute("company_id");
        
        try {
            var job = this.closeJobUseCase.execute(
                jobId,
                UUID.fromString(companyId.toString())
            );
            return ResponseEntity.ok().body(job);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{jobId}/apply")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(
        summary = "Candidatura em vaga", 
        description = "Rota responsável por permitir que um candidato se candidate a uma vaga",
        security = { @SecurityRequirement(name = "Bearer Authentication") }
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Candidatura realizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Candidato já se candidatou a esta vaga"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado"),
        @ApiResponse(responseCode = "404", description = "Vaga não encontrada")
    })
    public ResponseEntity<Object> apply(
        @PathVariable UUID jobId,
        HttpServletRequest request
    ) {
        var candidateId = request.getAttribute("candidate_id");
        
        try {
            var result = this.applyJobUseCase.execute(
                UUID.fromString(candidateId.toString()),
                jobId
            );
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/company")
    @PreAuthorize("hasRole('COMPANY')")
    @Operation(
        summary = "Listagem de vagas da empresa", 
        description = "Rota responsável por listar todas as vagas criadas pela empresa logada",
        security = { @SecurityRequirement(name = "Bearer Authentication") }
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de vagas da empresa retornada com sucesso"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<List<JobsResponseDTO>> listCompanyJobs(HttpServletRequest request) {
        var companyId = request.getAttribute("company_id");
        var jobs = this.listCompanyJobsUseCase.execute(UUID.fromString(companyId.toString()));
        return ResponseEntity.ok().body(jobs);
    }

    @DeleteMapping("/{jobId}")
    @PreAuthorize("hasRole('COMPANY')")
    @Operation(
        summary = "Deleção de vaga", 
        description = "Rota responsável por deletar uma vaga existente. Requer autenticação e senha da empresa.",
        security = { @SecurityRequirement(name = "Bearer Authentication") }
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Vaga deletada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Senha incorreta"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado"),
        @ApiResponse(responseCode = "404", description = "Vaga não encontrada")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Senha da empresa para confirmação",
        required = true,
        content = @Content(
            examples = {
                @ExampleObject(
                    name = "Confirmação de Deleção",
                    value = """
                    {
                        "password": "senha123456"
                    }
                    """
                )
            }
        )
    )
    public ResponseEntity<Object> delete(
        @PathVariable UUID jobId,
        @Valid @RequestBody DeleteJobDTO deleteJobDTO,
        HttpServletRequest request
    ) {
        var companyId = request.getAttribute("company_id");
        
        try {
            this.deleteJobUseCase.execute(
                jobId,
                UUID.fromString(companyId.toString()),
                deleteJobDTO.getPassword()
            );
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{jobId}/reactivate")
    @PreAuthorize("hasRole('COMPANY')")
    @Operation(
        summary = "Reativação de vaga", 
        description = "Rota responsável por reativar uma vaga que foi fechada anteriormente",
        security = { @SecurityRequirement(name = "Bearer Authentication") }
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vaga reativada com sucesso"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado"),
        @ApiResponse(responseCode = "404", description = "Vaga não encontrada")
    })
    public ResponseEntity<Object> reactivate(
        @PathVariable UUID jobId,
        HttpServletRequest request
    ) {
        var companyId = request.getAttribute("company_id");
        
        try {
            var job = this.reactivateJobUseCase.execute(
                jobId,
                UUID.fromString(companyId.toString())
            );
            return ResponseEntity.ok().body(job);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/filter")
    @Operation(
        summary = "Pesquisa de vagas com filtros", 
        description = "Rota responsável por pesquisar vagas com base em filtros como área de atuação, requisitos, tipo de contrato, localização, benefícios e faixa salarial"
    )
    public ResponseEntity<List<JobsResponseDTO>> filter(
        @RequestParam(required = false) String areaAtuacao,
        @RequestParam(required = false) String[] requisitos,
        @RequestParam(required = false) String tipoContrato,
        @RequestParam(required = false) String localizacao,
        @RequestParam(required = false) String salarioMinimo,
        @RequestParam(required = false) String salarioMaximo
    ) {
        var filter = JobFilterDTO.builder()
            .areaAtuacao(areaAtuacao)
            .requisitos(requisitos != null ? List.of(requisitos) : null)
            .tipoContrato(tipoContrato != null ? TipoContrato.valueOf(tipoContrato) : null)
            .localizacao(localizacao)
            .salarioMinimo(salarioMinimo != null ? new BigDecimal(salarioMinimo) : null)
            .salarioMaximo(salarioMaximo != null ? new BigDecimal(salarioMaximo) : null)
            .build();

        var jobs = this.filterJobsUseCase.execute(filter);
        return ResponseEntity.ok().body(jobs);
    }

    @GetMapping("/candidate")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(
        summary = "Listagem de candidaturas", 
        description = "Rota responsável por listar todas as vagas em que o candidato se candidatou",
        security = { @SecurityRequirement(name = "Bearer Authentication") }
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de candidaturas retornada com sucesso"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<List<JobsResponseDTO>> listCandidateJobs(HttpServletRequest request) {
        var candidateId = request.getAttribute("candidate_id");
        var jobs = this.listCandidateJobsUseCase.execute(UUID.fromString(candidateId.toString()));
        return ResponseEntity.ok().body(jobs);
    }

    @GetMapping("/{jobId}/candidates")
    @PreAuthorize("hasRole('COMPANY')")
    @Operation(
        summary = "Listagem de candidatos da vaga", 
        description = "Rota responsável por listar todos os candidatos que se candidataram a uma vaga específica",
        security = { @SecurityRequirement(name = "Bearer Authentication") }
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de candidatos retornada com sucesso"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado"),
        @ApiResponse(responseCode = "404", description = "Vaga não encontrada")
    })
    public ResponseEntity<List<CandidateJobResponseDTO>> listJobCandidates(
        @PathVariable UUID jobId,
        HttpServletRequest request
    ) {
        var companyId = request.getAttribute("company_id");
        var candidates = this.listJobCandidatesUseCase.execute(
            jobId,
            UUID.fromString(companyId.toString())
        );
        return ResponseEntity.ok().body(candidates);
    }
}
