package br.com.eliel.gestao_vagas.modules.candidate.useCases;

import br.com.eliel.gestao_vagas.modules.candidate.dto.UpdateCandidateDTO;
import br.com.eliel.gestao_vagas.modules.candidate.entites.CandidateEntity;
import br.com.eliel.gestao_vagas.modules.candidate.repositories.CandidateRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName; 
import org.junit.jupiter.api.Test;
import org.mockito.*; 

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*; 

class UpdateCandidateUseCaseTest {

    @InjectMocks
    private UpdateCandidateUseCase updateCandidateUseCase;

    @Mock
    private CandidateRepository candidateRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve atualizar todos os campos do candidato quando presentes no DTO")
    void execute_UpdatesCandidate_WhenFieldsArePresent() {
        UUID candidateId = UUID.randomUUID();
        CandidateEntity candidate = new CandidateEntity();
        candidate.setId(candidateId);
        candidate.setName("Nome Antigo");
        candidate.setUsername("usuarioantigo");
        candidate.setEmail("antigo@email.com");
        candidate.setDescription("Descricao Antiga");

        UpdateCandidateDTO dto = UpdateCandidateDTO.builder()
                .name("Novo Nome")
                .username("novousuario")
                .email("novo@email.com")
                .description("Nova Descricao")
                .build();

        when(candidateRepository.findById(candidateId)).thenReturn(Optional.of(candidate));
        when(candidateRepository.save(any(CandidateEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CandidateEntity updated = updateCandidateUseCase.execute(candidateId, dto);

        assertEquals("Novo Nome", updated.getName());
        assertEquals("novousuario", updated.getUsername());
        assertEquals("novo@email.com", updated.getEmail());
        assertEquals("Nova Descricao", updated.getDescription());
        assertEquals(candidateId, updated.getId()); 

        verify(candidateRepository, times(1)).findById(eq(candidateId)); 
        verify(candidateRepository, times(1)).save(eq(candidate)); 
        verifyNoMoreInteractions(candidateRepository); 
    }

    @Test
    @DisplayName("Deve atualizar apenas os campos não nulos do DTO")
    void execute_UpdatesOnlyNonNullFields() {
        UUID candidateId = UUID.randomUUID();
        CandidateEntity candidate = new CandidateEntity();
        candidate.setId(candidateId);
        candidate.setName("Nome Original");
        candidate.setUsername("usuariooriginal");
        candidate.setEmail("original@email.com");
        candidate.setDescription("Descricao Original");

        UpdateCandidateDTO dto = UpdateCandidateDTO.builder()
                .name(null) 
                .username("novousuario") 
                .email(null) 
                .description("Nova Descricao Parcial") 
                .build();

        when(candidateRepository.findById