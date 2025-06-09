package br.com.eliel.gestao_vagas.modules.company.useCases;

import br.com.eliel.gestao_vagas.modules.company.entites.CompanyEntity;
import br.com.eliel.gestao_vagas.modules.company.repositories.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateCompanyUseCaseTest {

    @InjectMocks 
    private CreateCompanyUseCase createCompanyUseCase;

    @Mock 
    private CompanyRepository companyRepository;

    @Mock 
    private PasswordEncoder passwordEncoder;

    private CompanyEntity newCompany;
    private CompanyEntity existingCompanyByUsername;
    private CompanyEntity existingCompanyByEmail;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        newCompany = new CompanyEntity();
        newCompany.setName("Nova Empresa de TI");
        newCompany.setUsername("novatech_user");
        newCompany.setEmail("nova@tech.com");
        newCompany.setPassword("senhaSegura123@");
        newCompany.setWebsite("http://novatech.com.br");
        newCompany.setDescription("Empresa inovadora em soluções de software");
        newCompany.setActive(true); 

        existingCompanyByUsername = new CompanyEntity();
        existingCompanyByUsername.setId(UUID.randomUUID());
        existingCompanyByUsername.setUsername("novatech_user"); 
        existingCompanyByUsername.setEmail("outro@email.com"); 
        existingCompanyByUsername.setPassword("hashedExistingPassword");

        existingCompanyByEmail = new CompanyEntity();
        existingCompanyByEmail.setId(UUID.randomUUID());
        existingCompanyByEmail.setUsername("outro_username"); 
        existingCompanyByEmail.setEmail("nova@tech.com"); 
        existingCompanyByEmail.setPassword("hashedExistingPassword");
    }

    @Test
    @DisplayName("Deve criar uma nova empresa com sucesso e retornar status 200 OK")
    void shouldCreateNewCompanySuccessfully() {
        when(companyRepository.findByUsernameOrEmail(newCompany.getUsername(), newCompany.getEmail()))
                .thenReturn(Optional.empty());

        String encodedPassword = "encodedPassword123ABC";
        when(passwordEncoder.encode(newCompany.getPassword())).thenReturn(encodedPassword);

        CompanyEntity savedCompany = new CompanyEntity();
        savedCompany.setId(UUID.randomUUID()); 
        savedCompany.setName(newCompany.getName());
        savedCompany.setUsername(newCompany.getUsername());
        savedCompany.setEmail(newCompany.getEmail());
        savedCompany.setPassword(encodedPassword); 
        savedCompany.setWebsite(newCompany.getWebsite());
        savedCompany.setDescription(newCompany.getDescription());
        savedCompany.setCreatedAt(LocalDateTime.now()); 
        savedCompany.setActive(newCompany.isActive());

        when(companyRepository.save(any(CompanyEntity.class))).thenReturn(savedCompany);

        ResponseEntity<Object> response = createCompanyUseCase.execute(newCompany);

        assertNotNull(response, "A resposta não deve ser nula.");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "O status da resposta deve ser OK (200).");
        assertNotNull(response.getBody(), "O corpo da resposta não deve ser nulo.");
        
        assertTrue(response.getBody() instanceof CompanyEntity, "O corpo da resposta deve ser uma instância de CompanyEntity.");
        CompanyEntity returnedCompany = (CompanyEntity) response.getBody();
        assertEquals(savedCompany.getUsername(), returnedCompany.getUsername(), "O username da empresa retornada deve ser o esperado.");
        assertEquals(encodedPassword, returnedCompany.getPassword(), "A senha da empresa retornada deve ser a codificada.");
        assertNotNull(returnedCompany.getId(), "A empresa retornada deve ter um ID.");

        verify(companyRepository, times(1)).findByUsernameOrEmail(newCompany.getUsername(), newCompany.getEmail());
        verify(passwordEncoder, times(1)).encode(newCompany.getPassword());
        ArgumentCaptor<CompanyEntity> companyCaptor = ArgumentCaptor.forClass(CompanyEntity.class);
        verify(companyRepository, times(1)).save(companyCaptor.capture());
        assertEquals(encodedPassword, companyCaptor.getValue().getPassword(), "A empresa salva deve ter a senha codificada.");
    }

    @Test
    @DisplayName("Deve retornar status 400 Bad Request quando o username já existe")
    void shouldReturnBadRequestWhenUsernameAlreadyExists() {
        when(companyRepository.findByUsernameOrEmail(newCompany.getUsername(), newCompany.getEmail()))
                .thenReturn(Optional.of(existingCompanyByUsername));

        ResponseEntity<Object> response = createCompanyUseCase.execute(newCompany);

        assertNotNull(response, "A resposta não deve ser nula.");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "O status da resposta deve ser BAD_REQUEST (400).");
        assertEquals("Username or email already exists", response.getBody(), "A mensagem de erro deve ser 'Username or email already exists'.");

        verify(companyRepository, times(1)).findByUsernameOrEmail(newCompany.getUsername(), newCompany.getEmail());
        // passwordEncoder.encode NÃO deve ser chamado
        verify(passwordEncoder, never()).encode(anyString());
        // companyRepository.save NÃO deve ser chamado
        verify(companyRepository, never()).save(any(CompanyEntity.class));
    }

    @Test
    @DisplayName("Deve retornar status 400 Bad Request quando o email já existe")
    void shouldReturnBadRequestWhenEmailAlreadyExists() {
        when(companyRepository.findByUsernameOrEmail(newCompany.getUsername(), newCompany.getEmail()))
                .thenReturn(Optional.of(existingCompanyByEmail));

        ResponseEntity<Object> response = createCompanyUseCase.execute(newCompany);

        //