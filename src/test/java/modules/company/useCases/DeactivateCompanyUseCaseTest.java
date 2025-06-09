package br.com.eliel.gestao_vagas.modules.company.useCases;

import br.com.eliel.gestao_vagas.modules.company.entites.CompanyEntity;
import br.com.eliel.gestao_vagas.modules.company.repositories.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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
        newCompany.setName("Nova Empresa Tech");
        newCompany.setUsername("novatech");
        newCompany.setEmail("nova@tech.com");
        newCompany.setPassword("senhaForte123!");
        newCompany.setWebsite("http://novatech.com");
        newCompany.setDescription("Uma startup inovadora no setor de tecnologia.");
        newCompany.setActive(true); 

        existingCompanyByUsername = new CompanyEntity();
        existingCompanyByUsername.setId(UUID.randomUUID());
        existingCompanyByUsername.setUsername("novatech"); 
        existingCompanyByUsername.setEmail("outro@email.com");
        existingCompanyByUsername.setPassword("senhaExistenteHash");

        existingCompanyByEmail = new CompanyEntity();
        existingCompanyByEmail.setId(UUID.randomUUID());
        existingCompanyByEmail.setUsername("outro_user");
        existingCompanyByEmail.setEmail("nova@tech.com"); 
        existingCompanyByEmail.setPassword("senhaExistenteHash");
    }

    @Test
    @DisplayName("Deve criar uma nova empresa com sucesso e retornar status 200 OK com a entidade criada.")
    void shouldCreateNewCompanySuccessfullyAndReturnOk() {

        when(companyRepository.findByUsernameOrEmail(newCompany.getUsername(), newCompany.getEmail()))
                .thenReturn(Optional.empty());

        String encodedPassword = "senha_codificada_gerada_pelo_encoder";
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
        assertEquals(HttpStatus.OK, response.getStatusCode(), "O status da resposta deve ser 200 OK.");
        assertNotNull(response.getBody(), "O corpo da resposta não deve ser nulo.");
        
        assertTrue(response.getBody() instanceof CompanyEntity, "O corpo da resposta deve ser uma instância de CompanyEntity.");
        CompanyEntity returnedCompany = (CompanyEntity) response.getBody();
        assertEquals(savedCompany.getUsername(), returnedCompany.getUsername(), "O username retornado deve ser o esperado.");
        assertEquals(encodedPassword, returnedCompany.getPassword(), "A senha retornada deve ser a codificada.");
        assertNotNull(returnedCompany.getId(), "A empresa retornada deve ter um ID gerado.");

        verify(companyRepository, times(1)).findByUsernameOrEmail(newCompany.getUsername(), newCompany.getEmail());
        verify(passwordEncoder, times(1)).encode(newCompany.getPassword());
       
        ArgumentCaptor<CompanyEntity> companyCaptor = ArgumentCaptor.forClass(CompanyEntity.class);
        verify(companyRepository, times(1)).save(companyCaptor.capture());
        assertEquals(encodedPassword, companyCaptor.getValue().getPassword(), "A entidade salva deve ter a senha codificada.");
    }

    @Test
    @DisplayName("Deve retornar status 400 Bad Request quando o username já existe.")
    void shouldReturnBadRequestWhenUsernameAlreadyExists() {
        
        when(companyRepository.findByUsernameOrEmail(newCompany.getUsername(), newCompany.getEmail()))
                .thenReturn(Optional.of(existingCompanyByUsername)); 
        ResponseEntity<Object> response = createCompanyUseCase.execute(newCompany);

        assertNotNull(response, "A resposta não deve ser nula.");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "O status da resposta deve ser 400 Bad Request.");
        assertEquals("Username or email already exists", response.getBody(), "A mensagem de erro deve indicar que username/email já existe.");

        
        verify(companyRepository, times(1)).findByUsernameOrEmail(newCompany.getUsername(), newCompany.getEmail());
        verify(passwordEncoder, never()).encode(anyString());
        verify(companyRepository, never()).save(any(CompanyEntity.class));
    }

    @Test
    @DisplayName("Deve retornar status 400 Bad Request quando o email já existe.")
    void shouldReturnBadRequestWhenEmailAlreadyExists() {
       

        when(companyRepository.findByUsernameOrEmail(newCompany.getUsername(), newCompany.getEmail()))
                .thenReturn(Optional.of(existingCompanyByEmail)); 

        ResponseEntity<Object> response = createCompanyUseCase.execute(newCompany);

        assertNotNull(response, "A resposta não deve ser nula.");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "O status da resposta deve ser 400 Bad Request.");
        assertEquals("Username or email already exists", response.getBody(), "A mensagem de erro deve indicar que username/email já existe.");

        
        verify(companyRepository, times(1)).findByUsernameOrEmail(newCompany.getUsername(), newCompany.getEmail());
        verify(passwordEncoder, never()).encode(anyString());
        verify(companyRepository, never()).save(any(CompanyEntity.class));
    }

    @Test
    @DisplayName("Deve retornar status 400 Bad Request para outras exceções inesperadas durante a criação.")
    void shouldReturnBadRequestForOtherUnexpectedExceptions() {
        

        when(companyRepository.findByUsernameOrEmail(newCompany.getUsername(), newCompany.getEmail()))
                .thenReturn(Optional.empty());

        String encodedPassword = "senha_codificada";
        when(passwordEncoder.encode(newCompany.getPassword())).thenReturn(encodedPassword);
        
        when(companyRepository.save(any(CompanyEntity.class)))
                .thenThrow(new RuntimeException("Erro inesperado de persistência de dados."));

        ResponseEntity<Object> response = createCompanyUseCase.execute(newCompany);

        assertNotNull(response, "A resposta não deve ser nula.");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "O status da resposta deve ser 400 Bad Request.");
        assertEquals("Erro inesperado de persistência de dados.", response.getBody(), "A mensagem de erro deve corresponder à exceção lançada.");

        verify(companyRepository, times(1)).findByUsernameOrEmail(newCompany.getUsername(), newCompany.getEmail());
        verify(passwordEncoder, times(1)).encode(newCompany.getPassword());
        verify(companyRepository, times(1)).save(any(CompanyEntity.class)); 
    }
}