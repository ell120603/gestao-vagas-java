package br.com.eliel.gestao_vagas.modules.candidate.repositories;

import br.com.eliel.gestao_vagas.modules.candidate.entites.CandidateEntity;
import br.com.eliel.gestao_vagas.modules.candidate.repositories.CandidateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class CandidateRepositoryIntegrationTest {

    @Autowired
    private CandidateRepository candidateRepository;

    private CandidateEntity candidate;

    @BeforeEach
    void setUp() {
        candidate = new CandidateEntity();
        candidate.setName("João Silva");
        candidate.setUsername("joaosilva");
        candidate.setEmail("joao@email.com");
        candidate.setPassword("senha123456");
        candidate.setDescription("Desenvolvedor Java");
    }

    @Test
    void save_ShouldCreateCandidate_WhenValidData() {
        CandidateEntity savedCandidate = candidateRepository.save(candidate);

        assertThat(savedCandidate).isNotNull();
        assertThat(savedCandidate.getId()).isNotNull();
        assertThat(savedCandidate.getName()).isEqualTo("João Silva");
        assertThat(savedCandidate.getUsername()).isEqualTo("joaosilva");
        assertThat(savedCandidate.getEmail()).isEqualTo("joao@email.com");
        assertThat(savedCandidate.getDescription()).isEqualTo("Desenvolvedor Java");
        assertThat(savedCandidate.isActive()).isTrue();
    }

    @Test
    void findById_ShouldReturnCandidate_WhenExists() {
        CandidateEntity savedCandidate = candidateRepository.save(candidate);
        Optional<CandidateEntity> foundCandidate = candidateRepository.findById(savedCandidate.getId());

        assertThat(foundCandidate).isPresent();
        assertThat(foundCandidate.get().getId()).isEqualTo(savedCandidate.getId());
        assertThat(foundCandidate.get().getName()).isEqualTo("João Silva");
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotExists() {
        Optional<CandidateEntity> foundCandidate = candidateRepository.findById(UUID.randomUUID());

        assertThat(foundCandidate).isEmpty();
    }

    @Test
    void findByUsernameOrEmail_ShouldReturnCandidate_WhenUsernameExists() {
        candidateRepository.save(candidate);
        Optional<CandidateEntity> foundCandidate = candidateRepository.findByUsernameOrEmail("joaosilva", "joaosilva");

        assertThat(foundCandidate).isPresent();
        assertThat(foundCandidate.get().getUsername()).isEqualTo("joaosilva");
    }

    @Test
    void findByUsernameOrEmail_ShouldReturnCandidate_WhenEmailExists() {
        candidateRepository.save(candidate);
        Optional<CandidateEntity> foundCandidate = candidateRepository.findByUsernameOrEmail("joao@email.com", "joao@email.com");

        assertThat(foundCandidate).isPresent();
        assertThat(foundCandidate.get().getEmail()).isEqualTo("joao@email.com");
    }

    @Test
    void findByUsernameOrEmail_ShouldReturnEmpty_WhenNotExists() {
        Optional<CandidateEntity> foundCandidate = candidateRepository.findByUsernameOrEmail("nonexistent", "nonexistent");

        assertThat(foundCandidate).isEmpty();
    }

    @Test
    void delete_ShouldRemoveCandidate_WhenExists() {
        CandidateEntity savedCandidate = candidateRepository.save(candidate);
        candidateRepository.delete(savedCandidate);
        Optional<CandidateEntity> foundCandidate = candidateRepository.findById(savedCandidate.getId());

        assertThat(foundCandidate).isEmpty();
    }
}
