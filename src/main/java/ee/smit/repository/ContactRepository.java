package ee.smit.repository;

import ee.smit.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository kontaktide andmete haldamiseks.
 * Võimaldab kontakte salvestada, otsida ja muuta.
 * Pärib standardsed CRUD operatsioonid JpaRepository liidesest.
 * 
 * Põhilised operatsioonid:
 * - findById: kontakti otsimine ID järgi
 * - save: kontakti salvestamine või uuendamine
 * - delete: kontakti kustutamine
 * - findAll: kõikide kontaktide pärimine
 */
@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    // Siia saab vajadusel lisada kohandatud päringumeetodeid,
    // näiteks otsimine nime või telefoninumbri järgi
}
