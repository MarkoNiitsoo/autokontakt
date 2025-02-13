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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

/**
 * Testid ContactController klassi jaoks.
 * Kontrollib kontaktide lisamise, muutmise ja kuvamise funktsionaalsust.
 * Kasutab MockMvc-d HTTP päringute simuleerimiseks.
 */
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
    @WithMockUser
    void testUueKontaktiVormiKuvamine() throws Exception {
        // Ettevalmistus
        Mockito.when(carBrandRepository.findAllByParentIsNullOrderByNameAsc()).thenReturn(Collections.emptyList());

        // Tegevus ja kontroll
        mockMvc.perform(MockMvcRequestBuilders.get("/contact"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.view().name("contactForm"))
               .andExpect(MockMvcResultMatchers.model().attributeExists("contact"))
               .andExpect(MockMvcResultMatchers.model().attribute("contact", new Contact()))
               .andExpect(MockMvcResultMatchers.model().attributeExists("carBrands"));
    }

    @Test
    @WithMockUser
    void testOlemasolevKontaktiVormiKuvamine() throws Exception {
        // Ettevalmistus
        Long kontaktiId = 1L;
        Contact olemasolevKontakt = new Contact();
        olemasolevKontakt.setId(kontaktiId);
        olemasolevKontakt.setFullName("Olemasolev Kontakt");
        Mockito.when(contactRepository.findById(kontaktiId)).thenReturn(Optional.of(olemasolevKontakt));
        Mockito.when(carBrandRepository.findAllByParentIsNullOrderByNameAsc()).thenReturn(Collections.emptyList());

        // Tegevus ja kontroll
        mockMvc.perform(MockMvcRequestBuilders.get("/contact").sessionAttr("contactId", kontaktiId))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.view().name("contactForm"))
               .andExpect(MockMvcResultMatchers.model().attributeExists("contact"))
               .andExpect(MockMvcResultMatchers.model().attribute("contact", olemasolevKontakt))
               .andExpect(MockMvcResultMatchers.model().attributeExists("carBrands"));
    }

    @Test
    @WithMockUser
    void testKustutaudKontaktiVormiKuvamine() throws Exception {
        // Ettevalmistus
        Long kontaktiId = 999L;
        Mockito.when(contactRepository.findById(kontaktiId)).thenReturn(Optional.empty());
        Mockito.when(carBrandRepository.findAllByParentIsNullOrderByNameAsc()).thenReturn(Collections.emptyList());

        // Tegevus ja kontroll
        mockMvc.perform(MockMvcRequestBuilders.get("/contact").sessionAttr("contactId", kontaktiId))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.view().name("contactForm"))
               .andExpect(MockMvcResultMatchers.model().attributeExists("contact"))
               .andExpect(MockMvcResultMatchers.model().attribute("contact", new Contact()))
               .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("contactId"))
               .andExpect(MockMvcResultMatchers.model().attributeExists("carBrands"));
    }

    @Test
    @WithMockUser
    void testKontaktiEdukaSalvestamine() throws Exception {
        // Ettevalmistus - automarkide loomine
        CarBrand mark1 = new CarBrand();
        mark1.setId(1L);
        mark1.setName("Mark 1");

        CarBrand mark2 = new CarBrand();
        mark2.setId(2L);
        mark2.setName("Mark 2");

        // Mock vastused automarkide päringutele
        Mockito.when(carBrandRepository.findById(1L)).thenReturn(Optional.of(mark1));
        Mockito.when(carBrandRepository.findById(2L)).thenReturn(Optional.of(mark2));

        // Oodatava kontakti loomine
        Contact oodatavKontakt = new Contact();
        oodatavKontakt.setFullName("Test Test");
        oodatavKontakt.setPhone("12345678");
        oodatavKontakt.setSelectedCarBrands(new HashSet<>(Arrays.asList(mark1, mark2)));

        // Salvestatud kontakti mock
        Contact salvestatudKontakt = new Contact();
        salvestatudKontakt.setId(1L);
        salvestatudKontakt.setSelectedCarBrands(new HashSet<>(Arrays.asList(mark1, mark2)));

        // Kontakti salvestamise mock
        Mockito.when(contactService.saveContact(Mockito.argThat(kontakt ->
            kontakt.getFullName().equals(oodatavKontakt.getFullName()) &&
            kontakt.getPhone().equals(oodatavKontakt.getPhone()) &&
            kontakt.getSelectedCarBrands() != null &&
            kontakt.getSelectedCarBrands().size() == 2 &&
            kontakt.getSelectedCarBrands().contains(mark1) &&
            kontakt.getSelectedCarBrands().contains(mark2)
        ))).thenReturn(salvestatudKontakt);

        // Mock automarkide nimekiri vormi jaoks
        Mockito.when(carBrandRepository.findAllByParentIsNullOrderByNameAsc())
               .thenReturn(Arrays.asList(mark1, mark2));

        // Tegevus ja kontroll
        mockMvc.perform(MockMvcRequestBuilders.post("/contact")
                       .with(SecurityMockMvcRequestPostProcessors.csrf())
                       .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                       .param("fullName", "Test Test")
                       .param("phone", "12345678")
                       .param("selectedCarBrands", "1", "2"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/contact"))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("message"));
    }

    @Test
    @WithMockUser
    void testKontaktiSalvestamineIlmaNimeta() throws Exception {
        // Ettevalmistus
        CarBrand mark = new CarBrand();
        mark.setId(1L);
        mark.setName("Test Mark");
        Mockito.when(carBrandRepository.findAllByParentIsNullOrderByNameAsc())
               .thenReturn(Collections.singletonList(mark));

        // Tegevus ja kontroll
        mockMvc.perform(MockMvcRequestBuilders.post("/contact")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("fullName", "") // Nimi puudu
                        .param("phone", "12345678")
                        .param("selectedCarBrands", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("contactForm"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("contact", "fullName"));
    }

    @Test
    @WithMockUser
    void testKontaktiSalvestamineIlmaTelefonita() throws Exception {
        // Ettevalmistus
        CarBrand mark = new CarBrand();
        mark.setId(1L);
        mark.setName("Test Mark");
        Mockito.when(carBrandRepository.findAllByParentIsNullOrderByNameAsc())
               .thenReturn(Collections.singletonList(mark));

        // Tegevus ja kontroll
        mockMvc.perform(MockMvcRequestBuilders.post("/contact")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("fullName", "Test Test")
                        .param("phone", "") // Telefon puudu
                        .param("selectedCarBrands", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("contactForm"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("contact", "phone"));
    }

    @Test
    @WithMockUser
    void testKontaktiSalvestamineIlmaAutomarkideta() throws Exception {
        // Ettevalmistus
        CarBrand mark = new CarBrand();
        mark.setId(1L);
        mark.setName("Test Mark");
        Mockito.when(carBrandRepository.findAllByParentIsNullOrderByNameAsc())
               .thenReturn(Collections.singletonList(mark));

        // Tegevus ja kontroll
        mockMvc.perform(MockMvcRequestBuilders.post("/contact")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("fullName", "Test Test")
                        .param("phone", "12345678")) // Automargid puudu
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("contactForm"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("contact", "selectedCarBrands"));
    }

    @Test
    @WithMockUser
    void testKontaktiSalvestamineKoikValjadTuhjad() throws Exception {
        // Ettevalmistus
        CarBrand mark = new CarBrand();
        mark.setId(1L);
        mark.setName("Test Mark");
        Mockito.when(carBrandRepository.findAllByParentIsNullOrderByNameAsc())
               .thenReturn(Collections.singletonList(mark));

        // Tegevus ja kontroll
        mockMvc.perform(MockMvcRequestBuilders.post("/contact")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("fullName", "")
                        .param("phone", ""))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("contactForm"))
                .andExpect(MockMvcResultMatchers.model().attributeHasErrors("contact"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("contact", "fullName"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("contact", "phone"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("contact", "selectedCarBrands"));
    }
}
