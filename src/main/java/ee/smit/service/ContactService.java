package ee.smit.service;

import ee.smit.model.Contact;
import ee.smit.repository.ContactRepository;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public Contact saveContact(Contact contact) {
        // Siin saad lisada ka lisaloogikat enne salvestamist
        return contactRepository.save(contact);
    }
}
