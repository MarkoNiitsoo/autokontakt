package ee.smit.controller;

import ee.smit.autokontakt.AutokontaktApplication;
import ee.smit.model.Contact;
import ee.smit.repository.CarBrandRepository;
import ee.smit.repository.ContactRepository;
import ee.smit.service.ContactService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = AutokontaktApplication.class)
@AutoConfigureMockMvc
public class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContactService contactService;
    @MockBean
    private ContactRepository contactRepository;
    @MockBean
    private CarBrandRepository carBrandRepository;

    @Test
    void testShowContactForm_NewContact() throws Exception {
        Mockito.when(carBrandRepository.findAllByParentIsNullOrderByNameAsc()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/contact"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.view().name("contactForm"))
               .andExpect(MockMvcResultMatchers.model().attributeExists("contact"))
               .andExpect(MockMvcResultMatchers.model().attribute("contact", new Contact())) // Kontrollime, kas mudelis on uus tühi Contact objekt
               .andExpect(MockMvcResultMatchers.model().attributeExists("carBrands"));
    }

    @Test
    void testShowContactForm_ExistingContactInSession() throws Exception {
        // Arrange
        Long contactId = 1L;
        Contact existingContact = new Contact();
        existingContact.setId(contactId);
        existingContact.setFullName("Existing Contact");
        Mockito.when(contactRepository.findById(contactId)).thenReturn(Optional.of(existingContact)); // Mock'ime olemasoleva kontakti leidmist
        Mockito.when(carBrandRepository.findAllByParentIsNullOrderByNameAsc()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/contact").sessionAttr("contactId", contactId)) // Lisame seansi atribuudi contactId
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.view().name("contactForm"))
               .andExpect(MockMvcResultMatchers.model().attributeExists("contact"))
               .andExpect(MockMvcResultMatchers.model().attribute("contact", existingContact)) // Kontrollime, kas mudelis on olemasolev Contact objekt
               .andExpect(MockMvcResultMatchers.model().attributeExists("carBrands"));
    }

    @Test
    void testShowContactForm_ExistingContactNotInDB() throws Exception {
        // Arrange
        Long contactId = 999L; // ID, mida andmebaasis ei ole
        Mockito.when(contactRepository.findById(contactId)).thenReturn(Optional.empty()); // Mock'ime, et kontakti ei leita ID jÃ¤rgi
        Mockito.when(carBrandRepository.findAllByParentIsNullOrderByNameAsc()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/contact").sessionAttr("contactId", contactId)) // Lisame seansi atribuudi contactId
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.view().name("contactForm"))
               .andExpect(MockMvcResultMatchers.model().attributeExists("contact"))
               .andExpect(MockMvcResultMatchers.model().attribute("contact", new Contact())) // Kontrollime, kas mudelis on uus tÃ¼hi Contact objekt
               .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("contactId")) // Kontrollime, kas seansist eemaldati contactId atribuut (kontrolli, kas see on vajalik)
               .andExpect(MockMvcResultMatchers.model().attributeExists("carBrands"));
    }
}