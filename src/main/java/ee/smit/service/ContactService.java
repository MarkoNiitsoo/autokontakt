package ee.smit.service;

import ee.smit.model.Contact;
import ee.smit.repository.ContactRepository;
import org.springframework.stereotype.Service;

/**
 * Teenusklass kontaktide haldamiseks.
 * Sisaldab äriloogikat kontaktide töötlemiseks ja salvestamiseks.
 */
@Service
public class ContactService {

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
        // Siin saab vajadusel lisada täiendavat äriloogikat enne salvestamist
        return contactRepository.save(contact);
    }
}
