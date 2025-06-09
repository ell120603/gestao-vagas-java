package br.com.eliel.gestao_vagas.modules.candidate.repositories;

import br.com.eliel.gestao_vagas.modules.candidate.entites.CandidateEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat; 
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
@ActiveProfiles("test") 
public class CandidateRepositoryIntegrationTest {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private TestEntityManager entityManager; 

    @Test
    @DisplayName("Deve encontrar o candidato por username ou email quando ele existir")
    void shouldFindCandidateByUsernameOrEmailWhenExists() {
       
        CandidateEntity newCandidate = new CandidateEntity();
        newCandidate.setName("Test User");
        newCandidate.setUsername("testuser");
        newCandidate.setEmail("test@email.com");
        newCandidate.setPassword("password123");
        newCandidate.setDescription("A test candidate");
       
        entityManager.persistAndFlush(newCandidate); 

        Optional<CandidateEntity> foundCandidateByUsername = candidateRepository.findByUsernameOrEmail("testuser", "another@email.com"); 
        Optional<CandidateEntity> foundCandidateByEmail = candidateRepository.findByUsernameOrEmail("anotheruser", "test@email.com");

        assertTrue(foundCandidateByUsername.isPresent(), "O candidato deve ser encontrado por username");
        assertThat(foundCandidateByUsername.get().getUsername()).isEqualTo("testuser");
        assertThat(foundCandidateByUsername.get().getEmail()).isEqualTo("test@email.com");

        assertTrue(foundCandidateByEmail.isPresent(), "O candidato deve ser encontrado por email");
        assertThat(foundCandidateByEmail.get().getUsername()).isEqualTo("testuser");
        assertThat(foundCandidateByEmail.get().getEmail()).isEqualTo("test@email.com");
    }

    @Test
    @DisplayName("Não deve encontrar o candidato por username ou email quando ele não existir")
    void shouldNotFindCandidateByUsernameOrEmailWhenNotExists() {
        Optional<CandidateEntity> foundCandidate = candidateRepository.findByUsernameOrEmail("nonexistent", "nonexistent@email.com");

        assertFalse(foundCandidate.isPresent(), "O candidato não deve ser encontrado");
    }

    @Test
    @DisplayName("Deve persistir um novo candidato e verificar suas propriedades geradas")
    void shouldPersistNewCandidateSuccessfully() {
        CandidateEntity newCandidate = new CandidateEntity();
        newCandidate.setName("New Candidate");
        newCandidate.setUsername("newuserunique"); 
        newCandidate.setEmail("newunique@email.com"); 
        newCandidate.setPassword("securePassword123");
        newCandidate.setDescription("A newly created candidate.");

        CandidateEntity savedCandidate = candidateRepository.save(newCandidate);

        assertThat(savedCandidate).isNotNull();
        assertThat(savedCandidate.getId()).isNotNull(); 
        assertThat(savedCandidate.getCreatedAt()).isNotNull(); 
        assertThat(savedCandidate.isActive()).isTrue(); 
        assertThat(savedCandidate.getUsername()).isEqualTo("newuserunique");

        Optional<CandidateEntity> found = candidateRepository.findById(savedCandidate.getId());
        assertTrue(found.isPresent());
        assertThat(found.get().getUsername()).isEqualTo("newuserunique");
    }
}