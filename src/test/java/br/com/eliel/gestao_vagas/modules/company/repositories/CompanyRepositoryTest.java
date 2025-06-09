package br.com.eliel.gestao_vagas.modules.company.repositories;

import br.com.eliel.gestao_vagas.modules.company.entites.CompanyEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CompanyRepositoryTest {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private TestEntityManager entityManager;

    private CompanyEntity company;

    @BeforeEach
    void setUp() {
        company = new CompanyEntity();
        company.setName("Tech Solutions");
        company.setUsername("techsolutions");
        company.setEmail("contato@techsolutions.com");
        company.setPassword("senha123456");
        company.setWebsite("https://techsolutions.com");
        company.setDescription("Empresa de tecnologia");
        company.setActive(true);
    }

    @Test
    void findByUsernameOrEmail_ShouldReturnCompany_WhenUsernameExists() {
        entityManager.persist(company);

        Optional<CompanyEntity> result = companyRepository.findByUsernameOrEmail(company.getUsername(), "outro@email.com");

        assertTrue(result.isPresent());
        assertEquals(company.getUsername(), result.get().getUsername());
        assertEquals(company.getEmail(), result.get().getEmail());
    }

    @Test
    void findByUsernameOrEmail_ShouldReturnCompany_WhenEmailExists() {
        entityManager.persist(company);

        Optional<CompanyEntity> result = companyRepository.findByUsernameOrEmail("outro_username", company.getEmail());

        assertTrue(result.isPresent());
        assertEquals(company.getUsername(), result.get().getUsername());
        assertEquals(company.getEmail(), result.get().getEmail());
    }

    @Test
    void findByUsernameOrEmail_ShouldReturnEmpty_WhenNeitherUsernameNorEmailExists() {
        entityManager.persist(company);

        Optional<CompanyEntity> result = companyRepository.findByUsernameOrEmail("outro_username", "outro@email.com");

        assertTrue(result.isEmpty());
    }

    @Test
    void findByUsername_ShouldReturnCompany_WhenUsernameExists() {
        entityManager.persist(company);

        CompanyEntity result = companyRepository.findByUsername(company.getUsername());

        assertNotNull(result);
        assertEquals(company.getUsername(), result.getUsername());
        assertEquals(company.getEmail(), result.getEmail());
    }

    @Test
    void findByUsername_ShouldReturnNull_WhenUsernameDoesNotExist() {
        entityManager.persist(company);

        CompanyEntity result = companyRepository.findByUsername("outro_username");

        assertNull(result);
    }

    @Test
    void save_ShouldPersistCompany() {
        CompanyEntity savedCompany = companyRepository.save(company);

        assertNotNull(savedCompany.getId());
        assertEquals(company.getName(), savedCompany.getName());
        assertEquals(company.getUsername(), savedCompany.getUsername());
        assertEquals(company.getEmail(), savedCompany.getEmail());
        assertEquals(company.getPassword(), savedCompany.getPassword());
        assertEquals(company.getWebsite(), savedCompany.getWebsite());
        assertEquals(company.getDescription(), savedCompany.getDescription());
        assertTrue(savedCompany.isActive());
    }

    @Test
    void findById_ShouldReturnCompany_WhenIdExists() {
        CompanyEntity savedCompany = entityManager.persist(company);

        Optional<CompanyEntity> result = companyRepository.findById(savedCompany.getId());

        assertTrue(result.isPresent());
        assertEquals(savedCompany.getId(), result.get().getId());
        assertEquals(savedCompany.getUsername(), result.get().getUsername());
        assertEquals(savedCompany.getEmail(), result.get().getEmail());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenIdDoesNotExist() {
        Optional<CompanyEntity> result = companyRepository.findById(UUID.randomUUID());

        assertTrue(result.isEmpty());
    }
}
