package br.com.eliel.gestao_vagas.modules.company.repositories;

import br.com.eliel.gestao_vagas.modules.company.entites.CompanyEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@ActiveProfiles("test") 
class CompanyRepositoryIntegrationTest {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private TestEntityManager entityManager;

    private CompanyEntity empresaBase; 

    @BeforeEach
    void setup() {
        companyRepository.deleteAll(); 

        empresaBase = new CompanyEntity();
        empresaBase.setName("Empresa Teste de Repositório");
        empresaBase.setUsername("empresa_repo_test");
        empresaBase.setEmail("repo_test@email.com");
        empresaBase.setPassword("senhaSegura123");
        empresaBase.setWebsite("http://repotest.com");
        empresaBase.setDescription("Descrição da empresa para teste de repositório");
        empresaBase.setCreatedAt(LocalDateTime.now());
        empresaBase.setActive(true);
        
        entityManager.persistAndFlush(empresaBase);
    }

 
    @AfterEach
    void tearDown() {
        companyRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve encontrar uma empresa pelo username ou email quando o username existe")
    void shouldFindCompanyByUsernameOrEmail_WhenUsernameExists() {
        Optional<CompanyEntity> result = companyRepository.findByUsernameOrEmail(empresaBase.getUsername(), "email_ficticio@naoexiste.com");

        assertTrue(result.isPresent(), "A empresa deve ser encontrada pelo username existente.");
        assertEquals(empresaBase.getUsername(), result.get().getUsername(), "O username da empresa encontrada deve ser o esperado.");
        assertEquals(empresaBase.getEmail(), result.get().getEmail(), "O email da empresa encontrada deve ser o esperado.");
    }

    @Test
    @DisplayName("Deve encontrar uma empresa pelo username ou email quando o email existe")
    void shouldFindCompanyByUsernameOrEmail_WhenEmailExists() {
        Optional<CompanyEntity> result = companyRepository.findByUsernameOrEmail("username_ficticio", empresaBase.getEmail());

        assertTrue(result.isPresent(), "A empresa deve ser encontrada pelo email existente.");
        assertEquals(empresaBase.getUsername(), result.get().getUsername(), "O username da empresa encontrada deve ser o esperado.");
        assertEquals(empresaBase.getEmail(), result.get().getEmail(), "O email da empresa encontrada deve ser o esperado.");
    }

    @Test
    @DisplayName("Não deve encontrar uma empresa se nem o username nem o email existirem")
    void shouldNotFindCompanyByUsernameOrEmail_WhenNeitherExist() {
        Optional<CompanyEntity> notFound = companyRepository.findByUsernameOrEmail("nao_existe", "inexistente@email.com");

        assertFalse(notFound.isPresent(), "Não deve encontrar a empresa com username ou email inexistentes.");
    }

    @Test
    @DisplayName("Deve encontrar uma empresa pelo username com sucesso")
    void shouldFindCompanyByUsernameSuccessfully() {
        CompanyEntity foundCompany = companyRepository.findByUsername(empresaBase.getUsername());

        assertNotNull(foundCompany, "A empresa deve ser encontrada pelo username.");
        assertEquals(empresaBase.getUsername(), foundCompany.getUsername(), "O username da empresa encontrada deve ser o esperado.");
        assertEquals(empresaBase.getEmail(), foundCompany.getEmail(), "O email da empresa encontrada deve ser o esperado.");
    }

    @Test
    @DisplayName("Deve retornar null quando o username não for encontrado")
    void shouldReturnNullWhenUsernameNotFound() {
        CompanyEntity notFoundCompany = companyRepository.findByUsername("username_nao_cadastrado");

        assertNull(notFoundCompany, "Deve retornar null quando o username não for encontrado.");
    }

    @Test
    @DisplayName("Deve salvar uma nova empresa no banco de dados")
    void shouldSaveNewCompanySuccessfully() {
        CompanyEntity novaEmpresa = new CompanyEntity();
        novaEmpresa.setName("Empresa Nova para Salvar");
        novaEmpresa.setUsername("nova_empresa_salva");
        novaEmpresa.setEmail("salvar@empresa.com");
        novaEmpresa.setPassword("senhaParaSalvar123");
        novaEmpresa.setCreatedAt(LocalDateTime.now());
        novaEmpresa.setActive(true);

        CompanyEntity savedCompany = companyRepository.save(novaEmpresa);

        assertNotNull(savedCompany, "A empresa salva não deve ser nula.");
        assertNotNull(savedCompany.getId(), "A empresa salva deve ter um ID gerado automaticamente.");
        assertEquals(novaEmpresa.getUsername(), savedCompany.getUsername(), "O username da empresa salva deve ser o mesmo do original.");

        Optional<CompanyEntity> found = companyRepository.findById(savedCompany.getId());
        assertTrue(found.isPresent(), "A empresa salva deve ser encontrada ao buscar pelo ID.");
        assertEquals(savedCompany.getUsername(), found.get().getUsername(), "O username da empresa buscada deve ser o mesmo da salva.");
    }
    
    @Test
    @DisplayName("Deve atualizar uma empresa existente no banco de dados")
    void shouldUpdateExistingCompanySuccessfully() {
        String novoNome = "Empresa Teste SA Atualizada";
        String novaDescricao = "Nova descrição da empresa de teste";
        
        empresaBase.setName(novoNome);
        empresaBase.setDescription(novaDescricao);

        CompanyEntity updatedCompany = companyRepository.save(empresaBase);

        assertNotNull(updatedCompany, "A empresa atualizada não deve ser nula.");
        assertEquals(novoNome, updatedCompany.getName(), "O nome da empresa deve ser atualizado.");
        assertEquals(novaDescricao, updatedCompany.getDescription(), "A descrição da empresa deve ser atualizada.");

        Optional<CompanyEntity> found = companyRepository.findById(empresaBase.getId());
        assertTrue(found.isPresent(), "A empresa atualizada deve ser encontrada.");
        assertEquals(novoNome, found.get().getName(), "O nome da empresa buscada deve ser o atualizado.");
    }

    @Test
    @DisplayName("Deve deletar uma empresa do banco de dados")
    void shouldDeleteCompanySuccessfully() {
        UUID idParaDeletar = empresaBase.getId();

        companyRepository.delete(empresaBase);

        Optional<CompanyEntity> foundAfterDelete = companyRepository.findById(idParaDeletar);
        assertFalse(foundAfterDelete.isPresent(), "A empresa não deve ser encontrada após a exclusão.");
    }
}