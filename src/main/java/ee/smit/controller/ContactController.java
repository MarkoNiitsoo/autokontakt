package ee.smit.controller;

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

@Controller
public class ContactController {

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

    @GetMapping("/contact")
    public String showContactForm(Model model, HttpSession session) {
        Long contactId = (Long) session.getAttribute("contactId");
        Contact contact;
        if (contactId != null) {
            contact = contactRepository.findById(contactId).orElse(null); // Muudetud: orElse(null)
            if (contact == null) { // Kui ID-ga kontakti ei leita (võibolla andmebaasist kustutatud?), loome uue
                contact = new Contact();
                session.removeAttribute("contactId"); // Eemaldame vigase contactId seansist
            }
        } else {
            contact = new Contact();
        }
        model.addAttribute("contact", contact);

        // Laadime andmebaasist automargid hierarhiliselt
        List<CarBrand> carBrands = carBrandHierarchyService.getHierarchicalCarBrands(null, 0);
        model.addAttribute("carBrands", carBrands);

        return "contactForm";
    }

    private List<CarBrand> getHierarchicalCarBrands(Long parentId, int level) {
        List<CarBrand> hierarchicalBrands = new ArrayList<>();
        List<CarBrand> currentLevelBrands;

        if (parentId == null) {
            currentLevelBrands = carBrandRepository.findAllByParentIsNullOrderByNameAsc(); // Top-level brands
        } else {
            currentLevelBrands = carBrandRepository.findByParentIdOrderByNameAsc(parentId); // Sub-brands
        }

        for (CarBrand brand : currentLevelBrands) {
            StringBuilder indent = new StringBuilder();
            for (int i = 0; i < level; i++) {
                indent.append("    "); // 4 spaces for each level
            }
            brand.setName(indent.toString() + brand.getName()); // Add indentation to the name
            hierarchicalBrands.add(brand); // Add current brand

            // Recursive call for sub-brands
            hierarchicalBrands.addAll(getHierarchicalCarBrands(brand.getId(), level + 1)); // Level + 1 for sub-brands
        }
        return hierarchicalBrands;
    }


    @PostMapping("/contact")
    public String saveContact(@Valid @ModelAttribute("contact") Contact contact,
                            BindingResult bindingResult,
                            @RequestParam(value = "selectedCarBrands", required = false) Set<Long> selectedCarBrandIds,
                            HttpSession session,
                            RedirectAttributes redirectAttributes,
                            Model model) {
        
        // Kui valideerimisel ilmneb vigu, tagastame vormi uuesti koos vigadega
        if (bindingResult.hasErrors()) {
            List<CarBrand> carBrands = getHierarchicalCarBrands(null, 0);
            model.addAttribute("carBrands", carBrands);
            return "contactForm";
        }

        // Convert IDs to CarBrand objects
        if (selectedCarBrandIds != null && !selectedCarBrandIds.isEmpty()) {
            Set<CarBrand> selectedCarBrands = selectedCarBrandIds.stream()
                .map(id -> carBrandRepository.findById(id).orElse(null))
                .filter(brand -> brand != null)
                .collect(Collectors.toSet());
            contact.setSelectedCarBrands(selectedCarBrands);
        }

        // Kui validatsioon läbitud, salvestame kontaktandmed andmebaasi
        Contact savedContact = contactService.saveContact(contact);
        session.setAttribute("contactId", savedContact.getId());
        redirectAttributes.addFlashAttribute("message", "Andmed salvestatud edukalt!");
        return "redirect:/contact";
    }
}
