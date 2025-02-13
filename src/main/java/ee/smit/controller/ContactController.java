package ee.smit.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ee.smit.model.CarBrand;
import ee.smit.model.Contact;
import ee.smit.repository.CarBrandRepository;
import ee.smit.repository.ContactRepository;
import ee.smit.service.ContactService;
import ee.smit.service.CarBrandHierarchyService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Kontrollerklass kontaktide haldamiseks.
 * Käsitleb kontaktide lisamist, muutmist ja kuvamist veebiliideses.
 * Võimaldab kontaktidele lisada seoseid automarkidega.
 */
@Controller
public class ContactController {
    
    private static final Logger log = LoggerFactory.getLogger(ContactController.class);
    private final ContactService contactService;
    private final ContactRepository contactRepository;
    private final CarBrandRepository carBrandRepository;
    private final CarBrandHierarchyService carBrandHierarchyService;

    public ContactController(ContactService contactService,
                           ContactRepository contactRepository,
                           CarBrandRepository carBrandRepository,
                           CarBrandHierarchyService carBrandHierarchyService) {
        this.contactService = contactService;
        this.contactRepository = contactRepository;
        this.carBrandRepository = carBrandRepository;
        this.carBrandHierarchyService = carBrandHierarchyService;
    }

    /**
     * Kuvab kontakti vormi.
     * Kui seansist leitakse kontakti ID, laetakse olemasolev kontakt muutmiseks.
     * Vastasel juhul kuvatakse tühi vorm uue kontakti lisamiseks.
     */
    @GetMapping("/contact")
    public String showContactForm(Model model, HttpSession session) {
        log.debug("Kontaktivormi kuvamine");
        Long contactId = (Long) session.getAttribute("contactId");
        Contact contact;
        if (contactId != null) {
            log.debug("Laen olemasoleva kontakti ID-ga: {}", contactId);
            contact = contactRepository.findById(contactId).orElse(null);
            if (contact == null) {
                log.warn("Kontakti ID-ga {} ei leitud, loon uue kontakti", contactId);
                contact = new Contact();
                session.removeAttribute("contactId");
            }
        } else {
            log.debug("Loon uue kontakti");
            contact = new Contact();
        }
        model.addAttribute("contact", contact);

        // Laadime andmebaasist automargid hierarhiliselt
        List<CarBrand> carBrands = carBrandHierarchyService.getHierarchicalCarBrands(null, 0);
        model.addAttribute("carBrands", carBrands);

        return "contactForm";
    }

    /**
     * Abimeetod automarkide hierarhiliseks kuvamiseks.
     * Lisab taanded vastavalt hierarhia tasemele.
     * 
     * @param parentId Ülemkategooria ID või null tippkategooria jaoks
     * @param level Praegune hierarhia tase (määrab taande suuruse)
     * @return Hierarhiliselt vormindatud automarkide nimekiri
     */
    private List<CarBrand> getHierarchicalCarBrands(Long parentId, int level) {
        List<CarBrand> hierarchicalBrands = new ArrayList<>();
        List<CarBrand> currentLevelBrands;

        if (parentId == null) {
            currentLevelBrands = carBrandRepository.findAllByParentIsNullOrderByNameAsc(); // Tippkategooria margid
        } else {
            currentLevelBrands = carBrandRepository.findByParentIdOrderByNameAsc(parentId); // Alamkategooria margid
        }

        for (CarBrand brand : currentLevelBrands) {
            StringBuilder indent = new StringBuilder();
            for (int i = 0; i < level; i++) {
                indent.append("    "); // 4 tühikut iga taseme kohta
            }
            brand.setName(indent.toString() + brand.getName()); // Lisame taande nimele
            hierarchicalBrands.add(brand);

            // Rekursiivne väljakutse alammarkide jaoks
            hierarchicalBrands.addAll(getHierarchicalCarBrands(brand.getId(), level + 1));
        }
        return hierarchicalBrands;
    }

    /**
     * Käsitleb kontakti salvestamist.
     * Valideerib sisendandmed ja salvestab kontakti koos valitud automarkidega.
     */
    @PostMapping("/contact")
    public String saveContact(@Valid @ModelAttribute("contact") Contact contact,
                            BindingResult bindingResult,
                            @RequestParam(value = "selectedCarBrands", required = false) Set<Long> selectedCarBrandIds,
                            HttpSession session,
                            RedirectAttributes redirectAttributes,
                            Model model) {
        
        log.debug("Kontakti salvestamine: {}", contact.getFullName());
        
        if (bindingResult.hasErrors()) {
            log.warn("Kontakti valideerimisel tekkisid vead: {}", bindingResult.getAllErrors());
            List<CarBrand> carBrands = getHierarchicalCarBrands(null, 0);
            model.addAttribute("carBrands", carBrands);
            return "contactForm";
        }

        log.debug("Valitud automarkide arv: {}", selectedCarBrandIds != null ? selectedCarBrandIds.size() : 0);
        if (selectedCarBrandIds != null && !selectedCarBrandIds.isEmpty()) {
            Set<CarBrand> selectedCarBrands = selectedCarBrandIds.stream()
                .map(id -> carBrandRepository.findById(id).orElse(null))
                .filter(brand -> brand != null)
                .collect(Collectors.toSet());
            contact.setSelectedCarBrands(selectedCarBrands);
        }

        Contact savedContact = contactService.saveContact(contact);
        log.info("Kontakt edukalt salvestatud ID-ga: {}", savedContact.getId());
        session.setAttribute("contactId", savedContact.getId());
        redirectAttributes.addFlashAttribute("message", "Andmed salvestatud edukalt!");
        return "redirect:/contact";
    }
}
