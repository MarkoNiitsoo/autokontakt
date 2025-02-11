package ee.smit.repository;

import ee.smit.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    // Siia saad lisada ka kohandatud päringumeetodeid vastavalt vajadusele.
}
