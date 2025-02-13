package ee.smit.controller;

import ee.smit.autokontakt.AutokontaktApplication;
import ee.smit.model.CarBrand;
import ee.smit.model.Contact;
import ee.smit.service.ContactService;
import ee.smit.repository.ContactRepository;
import ee.smit.repository.CarBrandRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.FlashMap;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
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
               .andExpect(MockMvcResultMatchers.model().attribute("contact", new Contact()))
               .andExpect(MockMvcResultMatchers.model().attributeExists("carBrands"));
    }

    @Test
    void testShowContactForm_ExistingContactInSession() throws Exception {
        // Arrange
        Long contactId = 1L;
        Contact existingContact = new Contact();
        existingContact.setId(contactId);
        existingContact.setFullName("Existing Contact");
        Mockito.when(contactRepository.findById(contactId)).thenReturn(Optional.of(existingContact));
        Mockito.when(carBrandRepository.findAllByParentIsNullOrderByNameAsc()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/contact").sessionAttr("contactId", contactId))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.view().name("contactForm"))
               .andExpect(MockMvcResultMatchers.model().attributeExists("contact"))
               .andExpect(MockMvcResultMatchers.model().attribute("contact", existingContact))
               .andExpect(MockMvcResultMatchers.model().attributeExists("carBrands"));
    }

    @Test
    void testShowContactForm_ExistingContactNotInDB() throws Exception {
        // Arrange
        Long contactId = 999L;
        Mockito.when(contactRepository.findById(contactId)).thenReturn(Optional.empty());
        Mockito.when(carBrandRepository.findAllByParentIsNullOrderByNameAsc()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/contact").sessionAttr("contactId", contactId))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.view().name("contactForm"))
               .andExpect(MockMvcResultMatchers.model().attributeExists("contact"))
               .andExpect(MockMvcResultMatchers.model().attribute("contact", new Contact()))
               .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("contactId"))
               .andExpect(MockMvcResultMatchers.model().attributeExists("carBrands"));
    }


    @Test
    void testSaveContactSuccess() throws Exception {
        // Create test car brands
        CarBrand brand1 = new CarBrand();
        brand1.setId(1L);
        brand1.setName("Brand 1");

        CarBrand brand2 = new CarBrand();
        brand2.setId(2L);
        brand2.setName("Brand 2");

        // Mock findById for car brands
        Mockito.when(carBrandRepository.findById(1L)).thenReturn(Optional.of(brand1));
        Mockito.when(carBrandRepository.findById(2L)).thenReturn(Optional.of(brand2));

        // Create expected contact with selected car brands
        Contact expectedContact = new Contact();
        expectedContact.setFullName("Test Test");
        expectedContact.setPhone("12345678");
        expectedContact.setSelectedCarBrands(new HashSet<>(Arrays.asList(brand1, brand2)));

        // Mock successful contact save
        Contact savedContact = new Contact();
        savedContact.setId(1L);
        savedContact.setSelectedCarBrands(new HashSet<>(Arrays.asList(brand1, brand2)));

        // Verify that contact is saved with correct car brands
        Mockito.when(contactService.saveContact(Mockito.argThat(contact ->
            contact.getFullName().equals(expectedContact.getFullName()) &&
            contact.getPhone().equals(expectedContact.getPhone()) &&
            contact.getSelectedCarBrands() != null &&
            contact.getSelectedCarBrands().size() == 2 &&
            contact.getSelectedCarBrands().contains(brand1) &&
            contact.getSelectedCarBrands().contains(brand2)
        ))).thenReturn(savedContact);

        // Mock car brands repository for form display
        Mockito.when(carBrandRepository.findAllByParentIsNullOrderByNameAsc())
               .thenReturn(Arrays.asList(brand1, brand2));

        mockMvc.perform(MockMvcRequestBuilders.post("/contact")
                       .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                       .param("fullName", "Test Test")
                       .param("phone", "12345678")
                       .param("selectedCarBrands", "1", "2"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/contact"))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("message"));
    }


    @Test
    void testSaveContactValidationErrors_FullNameMissing() throws Exception {
        // Mock car brands repository for form redisplay
        CarBrand brand = new CarBrand();
        brand.setId(1L);
        brand.setName("Test Brand");
        Mockito.when(carBrandRepository.findAllByParentIsNullOrderByNameAsc())
               .thenReturn(Collections.singletonList(brand));

        mockMvc.perform(MockMvcRequestBuilders.post("/contact")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("fullName", "") // Eesnimi puudu
                        .param("phone", "12345678")
                        .param("selectedCarBrands", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("contactForm"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("contact", "fullName")); // Viga fullName väljal
    }

    @Test
    void testSaveContactValidationErrors_PhoneMissing() throws Exception {
        // Mock car brands repository for form redisplay
        CarBrand brand = new CarBrand();
        brand.setId(1L);
        brand.setName("Test Brand");
        Mockito.when(carBrandRepository.findAllByParentIsNullOrderByNameAsc())
               .thenReturn(Collections.singletonList(brand));

        mockMvc.perform(MockMvcRequestBuilders.post("/contact")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("fullName", "Test Test")
                        .param("phone", "") // Telefon puudu
                        .param("selectedCarBrands", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("contactForm"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("contact", "phone")); // Viga phone väljal
    }

    @Test
    void testSaveContactValidationErrors_CarBrandsMissing() throws Exception {
        // Mock car brands repository for form redisplay
        CarBrand brand = new CarBrand();
        brand.setId(1L);
        brand.setName("Test Brand");
        Mockito.when(carBrandRepository.findAllByParentIsNullOrderByNameAsc())
               .thenReturn(Collections.singletonList(brand));

        mockMvc.perform(MockMvcRequestBuilders.post("/contact")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("fullName", "Test Test")
                        .param("phone", "12345678")) // Automargid puudu
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("contactForm"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("contact", "selectedCarBrands")); // Viga selectedCarBrands väljal
    }

    @Test
    void testSaveContactValidationErrors_AllFieldsMissing() throws Exception {
        // Mock car brands repository for form redisplay
        CarBrand brand = new CarBrand();
        brand.setId(1L);
        brand.setName("Test Brand");
        Mockito.when(carBrandRepository.findAllByParentIsNullOrderByNameAsc())
               .thenReturn(Collections.singletonList(brand));

        mockMvc.perform(MockMvcRequestBuilders.post("/contact")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("fullName", "") // Kõik väljad puudu
                        .param("phone", ""))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("contactForm"))
                .andExpect(MockMvcResultMatchers.model().attributeHasErrors("contact")) // On olemas vigu "contact" objektis
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("contact", "fullName")) // Viga fullName väljal
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("contact", "phone")) // Viga phone väljal
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("contact", "selectedCarBrands")); // Viga selectedCarBrands väljal
    }
}
