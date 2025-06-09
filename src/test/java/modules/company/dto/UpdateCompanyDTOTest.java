package br.com.eliel.gestao_vagas.modules.company.dto;

import org.junit.jupiter.api.DisplayName; 
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UpdateCompanyDTOTest {

    @Test
    @DisplayName("Should test the no-argument constructor and setter methods")
    void testNoArgsConstructorAndSetters() {
        UpdateCompanyDTO dto = new UpdateCompanyDTO();
        
        dto.setName("New Tech Co.");
        dto.setUsername("newtechco");
        dto.setEmail("contact@newtechco.com");
        dto.setWebsite("https://www.newtechco.com");
        dto.setDescription("Innovative tech solutions provider");

        assertEquals("New Tech Co.", dto.getName(), "The name should match the set value.");
        assertEquals("newtechco", dto.getUsername(), "The username should match the set value.");
        assertEquals("contact@newtechco.com", dto.getEmail(), "The email should match the set value.");
        assertEquals("https://www.newtechco.com", dto.getWebsite(), "The website should match the set value.");
        assertEquals("Innovative tech solutions provider", dto.getDescription(), "The description should match the set value.");
    }

    @Test
    @DisplayName("Should test the all-arguments constructor")
    void testAllArgsConstructor() {
        UpdateCompanyDTO dto = new UpdateCompanyDTO(
                "Global Innovations", 
                "globalinnovations", 
                "info@globalinnovations.net",
                "https://www.globalinnovations.net", 
                "A leading innovator in various industries"
        );
        
        assertEquals("Global Innovations", dto.getName(), "The name should be correctly initialized.");
        assertEquals("globalinnovations", dto.getUsername(), "The username should be correctly initialized.");
        assertEquals("info@globalinnovations.net", dto.getEmail(), "The email should be correctly initialized.");
        assertEquals("https://www.globalinnovations.net", dto.getWebsite(), "The website should be correctly initialized.");
        assertEquals("A leading innovator in various industries", dto.getDescription(), "The description should be correctly initialized.");
    }

    @Test
    @DisplayName("Should test object creation using the Builder pattern")
    void testBuilder() {
        UpdateCompanyDTO dto = UpdateCompanyDTO.builder()
                .name("Apex Systems")
                .username("apexsys")
                .email("support@apexsys.co")
                .website("https://www.apexsys.co")
                .description("Providing cutting-edge IT services")
                .build();

        assertEquals("Apex Systems", dto.getName(), "The name built should be 'Apex Systems'.");
        assertEquals("apexsys", dto.getUsername(), "The username built should be 'apexsys'.");
        assertEquals("support@apexsys.co", dto.getEmail(), "The email built should be 'support@apexsys.co'.");
        assertEquals("https://www.apexsys.co", dto.getWebsite(), "The website built should be 'https://www.apexsys.co'.");
        assertEquals("Providing cutting-edge IT services", dto.getDescription(), "The description built should be correct.");
    }

    @Test
    @DisplayName("Should test equals and hashCode for identical, differing, null, and different class objects")
    void testEqualsAndHashCode() {
        UpdateCompanyDTO dto1 = new UpdateCompanyDTO(
                "Company X", "companyX", "emailX@test.com", "siteX.com", "Desc X"
        );
        UpdateCompanyDTO dto2 = new UpdateCompanyDTO(
                "Company X", "companyX", "emailX@test.com", "siteX.com", "Desc X"
        );
        assertEquals(dto1, dto2, "DTOs with identical values should be equal.");
        assertEquals(dto1.hashCode(), dto2.hashCode(), "Hash codes of identical DTOs should be equal.");

        UpdateCompanyDTO dto3 = new UpdateCompanyDTO(
                "Company Y", "companyX", "emailX@test.com", "siteX.com", "Desc X"
        );
        assertNotEquals(dto1, dto3, "DTOs with different names should not be equal.");

        UpdateCompanyDTO dto4 = new UpdateCompanyDTO(
                "Company X", "companyY", "emailX@test.com", "siteX.com", "Desc X"
        );
        assertNotEquals(dto1, dto4, "DTOs with different usernames should not be equal.");
        
        UpdateCompanyDTO dto5 = new UpdateCompanyDTO(
                "Company X", "companyX", "emailY@test.com", "siteX.com", "Desc X"
        );
        assertNotEquals(dto1, dto5, "DTOs with different emails should not be equal.");

        assertNotEquals(dto1, null, "An object should not be equal to null.");
        
        assertNotEquals(dto1, new Object(), "An object should not be equal to an instance of a different class.");
    }

    @Test
    @DisplayName("Should test the string representation (toString) of the class")
    void testToString() {
        UpdateCompanyDTO dto = new UpdateCompanyDTO(
                "Sample Company", "sampleuser", "sample@company.com",
                "https://www.samplecompany.org", "A short description for testing"
        );
        String str = dto.toString();
        
        assertTrue(str.contains("name=Sample Company"), "The string should contain the name.");
        assertTrue(str.contains("username=sampleuser"), "The string should contain the username.");
        assertTrue(str.contains("email=sample@company.com"), "The string should contain the email.");
        assertTrue(str.contains("website=https://www.samplecompany.org"), "The string should contain the website.");
        assertTrue(str.contains("description=A short description for testing"), "The string should contain the description.");
        assertTrue(str.startsWith("UpdateCompanyDTO("), "The string should start with the class name.");
    }
}