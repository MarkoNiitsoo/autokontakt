package ee.smit.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ee.smit.model.Contact;
import ee.smit.repository.ContactRepository;
import org.springframework.stereotype.Service;

/**
 * Teenusklass kontaktide haldamiseks.
 * Sisaldab äriloogikat kontaktide töötlemiseks ja salvestamiseks.
 */
@Service
public class ContactService {
    
    private static final Logger log = LoggerFactory.getLogger(ContactService.class);
    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    /**
     * Salvestab kontakti andmebaasi.
     * 
     * @param contact Salvestatav kontakt
     * @return Salvestatud kontakt koos genereeritud ID-ga
     */
    public Contact saveContact(Contact contact) {
        log.debug("Alustan kontakti salvestamist: {}", contact.getFullName());
        try {
            Contact savedContact = contactRepository.save(contact);
            log.debug("Kontakt edukalt salvestatud ID-ga: {}", savedContact.getId());
            return savedContact;
        } catch (Exception e) {
            log.error("Viga kontakti salvestamisel: {}", e.getMessage());
            throw e;
        }
    }
}
