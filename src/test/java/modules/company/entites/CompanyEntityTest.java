package br.com.eliel.gestao_vagas.modules.company.entites;

import org.junit.jupiter.api.DisplayName; 
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CompanyEntityTest {

    @Test
    @DisplayName("Deve testar o construtor sem argumentos e os métodos setters e getters")
    void testNoArgsConstructorAndSetters() {
        CompanyEntity entity = new CompanyEntity();
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        entity.setId(id);
        entity.setName("Nova Companhia S.A.");
        entity.setUsername("novacompanhia");
        entity.setEmail("contato@novacompanhia.com.br");
        entity.setPassword("minhaSenhaMuitoSegura123"); 
        entity.setWebsite("https://www.novacompanhia.com.br");
        entity.setDescription("Empresa líder em soluções inovadoras");
        entity.setCreatedAt(now);
        entity.setActive(false); 

        assertEquals(id, entity.getId(), "O ID deve corresponder ao definido.");
        assertEquals("Nova Companhia S.A.", entity.getName(), "O nome deve corresponder.");
        assertEquals("novacompanhia", entity.getUsername(), "O username deve corresponder.");
        assertEquals("contato@novacompanhia.com.br", entity.getEmail(), "O email deve corresponder.");
        assertEquals("minhaSenhaMuitoSegura123", entity.getPassword(), "A senha deve corresponder.");
        assertEquals("https://www.novacompanhia.com.br", entity.getWebsite(), "O website deve corresponder.");
        assertEquals("Empresa líder em soluções inovadoras", entity.getDescription(), "A descrição deve corresponder.");
        assertEquals(now, entity.getCreatedAt(), "A data de criação deve corresponder.");
        assertFalse(entity.isActive(), "O status 'active' deve ser false após ser definido.");
    }

    @Test
    @DisplayName("Deve testar se 'active' é true por padrão")
    void testActiveDefaultValue() {
        CompanyEntity entity = new CompanyEntity();
        assertTrue(entity.isActive(), "O status 'active' deve ser true por padrão.");
    }

    @Test
    @DisplayName("Deve testar a igualdade e o hashcode para entidades com os mesmos valores")
    void testEqualsAndHashCode_SameValues() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        CompanyEntity entity1 = new CompanyEntity();
        entity1.setId(id);
        entity1.setName("Empresa Teste");
        entity1.setUsername("empresa_teste");
        entity1.setEmail("teste@email.com");
        entity1.setPassword("senhaSegura123");
        entity1.setWebsite("http://test.com");
        entity1.setDescription("Descrição teste");
        entity1.setCreatedAt(now);
        entity1.setActive(true);

        CompanyEntity entity2 = new CompanyEntity();
        entity2.setId(id); 
        entity2.setName("Empresa Teste");
        entity2.setUsername("empresa_teste");
        entity2.setEmail("teste@email.com");
        entity2.setPassword("senhaSegura123");
        entity2.setWebsite("http://test.com");
        entity2.setDescription("Descrição teste");
        entity2.setCreatedAt(now);
        entity2.setActive(true);

        assertEquals(entity1, entity2, "Entidades com os mesmos atributos devem ser iguais.");
        assertEquals(entity1.hashCode(), entity2.hashCode(), "Hash codes de entidades iguais devem ser iguais.");
    }

    @Test
    @DisplayName("Deve testar a desigualdade e o hashcode para entidades com valores diferentes")
    void testEqualsAndHashCode_DifferentValues() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID(); 
        LocalDateTime now = LocalDateTime.now();

        CompanyEntity entity1 = new CompanyEntity();
        entity1.setId(id1);
        entity1.setName("Empresa A");
        entity1.setUsername("empresa_a");
        entity1.setEmail("a@email.com");
        entity1.setPassword("senhaA123");
        entity1.setWebsite("http://a.com");
        entity1.setDescription("Desc A");
        entity1.setCreatedAt(now);
        entity1.setActive(true);

        CompanyEntity entityDiffId = new CompanyEntity();
        entityDiffId.setId(id2);
        entityDiffId.setName("Empresa A");
        entityDiffId.setUsername("empresa_a");
        entityDiffId.setEmail("a@email.com");
        entityDiffId.setPassword("senhaA123");
        entityDiffId.setWebsite("http://a.com");
        entityDiffId.setDescription("Desc A");
        entityDiffId.setCreatedAt(now);
        entityDiffId.setActive(true);
        assertNotEquals(entity1, entityDiffId, "Entidades com IDs diferentes não devem ser iguais.");

        CompanyEntity entityDiffName = new CompanyEntity();
        entityDiffName.setId(id1);
        entityDiffName.setName("Empresa B"); 
        entityDiffName.setUsername("empresa_a");
        entityDiffName.setEmail("a@email.com");
        entityDiffName.setPassword("senhaA123");
        entityDiffName.setWebsite("http://a.com");
        entityDiffName.setDescription("Desc A");
        entityDiffName.setCreatedAt(now);
        entityDiffName.setActive(true);
        assertNotEquals(entity1, entityDiffName, "Entidades com nomes diferentes não devem ser iguais.");
        
        assertNotEquals(entity1, null, "Uma entidade não deve ser igual a null.");
        
        assertNotEquals(entity1, new Object(), "Uma entidade não deve ser igual a uma instância de outra classe.");
    }


    @Test
    @DisplayName("Deve testar a representação em string (toString) da entidade")
    void testToString() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        CompanyEntity entity = new CompanyEntity();
        entity.setId(id);
        entity.setName("My Company");
        entity.setUsername("mycompany");
        entity.setEmail("my@company.com");
        entity.setPassword("securePassword123");
        entity.setWebsite("https://mycompany.com");
        entity.setDescription("Our awesome company");
        entity.setCreatedAt(now);
        entity.setActive(true);

        String str = entity.toString();

        assertTrue(str.contains("id=" + id.toString()), "A string toString deve conter o ID.");
        assertTrue(str.contains("name=My Company"), "A string toString deve conter o nome.");
        assertTrue(str.contains("username=mycompany"), "A string toString deve conter o username.");
        assertTrue(str.contains("email=my@company.com"), "A string toString deve conter o email.");
        assertTrue(str.contains("password=securePassword123"), "A string toString deve conter a senha.");
        assertTrue(str.contains("website=https://mycompany.com"), "A string toString deve conter o website.");
        assertTrue(str.contains("description=Our awesome company"), "A string toString deve conter a descrição.");
        assertTrue(str.contains("createdAt=" + now.toString()), "A string toString deve conter a data de criação.");
        assertTrue(str.contains("active=true"), "A string toString deve conter o status 'active'.");
        assertTrue(str.startsWith("CompanyEntity("), "A string toString deve começar com o nome da classe.");
    }
}