package ee.smit.model;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testid Contact klassi equals() ja hashCode() meetodite kontrollimiseks.
 * Kontrollib võrdlemisloogika korrektsust erinevate stsenaariumite puhul.
 */
public class ContactTest {

    @Test
    void testVordlemineSamaObjektiga() {
        Contact contact1 = new Contact();
        assertTrue(contact1.equals(contact1)); // Objekt iseendaga on võrdne
    }

    @Test
    void testVordlemineNullObjektiga() {
        Contact contact1 = new Contact();
        assertFalse(contact1.equals(null)); // Objekt null-objektiga ei ole võrdne
    }

    @Test
    void testVordlemineTeiseKlassiObjektiga() {
        Contact contact1 = new Contact();
        assertFalse(contact1.equals("Not a Contact object")); // Objekt teise klassi objektiga ei ole võrdne
    }

    @Test
    void testVordlemineVordseteObjektidega() {
        Contact contact1 = new Contact();
        contact1.setId(1L);
        contact1.setFullName("Test Name");
        contact1.setPhone("12345678");
        contact1.setValidDriverLicense(true);
        Set<CarBrand> carBrands1 = new HashSet<>();
        carBrands1.add(new CarBrand("Brand1", null));
        contact1.setSelectedCarBrands(carBrands1);

        Contact contact2 = new Contact();
        contact2.setId(1L);
        contact2.setFullName("Test Name");
        contact2.setPhone("12345678");
        contact2.setValidDriverLicense(true);
        Set<CarBrand> carBrands2 = new HashSet<>();
        carBrands2.add(new CarBrand("Brand1", null));
        contact2.setSelectedCarBrands(carBrands2);

        assertEquals(contact1, contact2); // Võrdsed objektid on võrdsed
    }

    @Test
    void testVordlemineErinvateIdDega() {
        Contact contact1 = new Contact();
        contact1.setId(1L);
        Contact contact2 = new Contact();
        contact2.setId(2L);
        assertNotEquals(contact1, contact2); // Erinevad ID-d - ei ole võrdsed
    }

    @Test
    void testVordlemineErinevateNimedega() {
        Contact contact1 = new Contact();
        contact1.setFullName("Name 1");
        Contact contact2 = new Contact();
        contact2.setFullName("Name 2");
        assertNotEquals(contact1, contact2); // Erinevad nimed - ei ole võrdsed
    }

    @Test
    void testVordlemineErinevateTelefonidega() {
        Contact contact1 = new Contact();
        contact1.setPhone("111");
        Contact contact2 = new Contact();
        contact2.setPhone("222");
        assertNotEquals(contact1, contact2); // Erinevad telefoninumbrid - ei ole võrdsed
    }

    @Test
    void testVordlemineErinevateJuhilubadega() {
        Contact contact1 = new Contact();
        contact1.setValidDriverLicense(true);
        Contact contact2 = new Contact();
        contact2.setValidDriverLicense(false);
        assertNotEquals(contact1, contact2); // Erinevad juhiloa staatused - ei ole võrdsed
    }

    @Test
    void testVordlemineErinevateAutomarkidega() {
        Contact contact1 = new Contact();
        contact1.setSelectedCarBrands(Set.of(new CarBrand("Brand1", null)));
        Contact contact2 = new Contact();
        contact2.setSelectedCarBrands(Set.of(new CarBrand("Brand2", null)));
        assertNotEquals(contact1, contact2); // Erinevad automargid - ei ole võrdsed
    }

    @Test
    void testHashCodeVordsetelObjektidel() {
        Contact contact1 = new Contact();
        contact1.setId(1L);
        Contact contact2 = new Contact();
        contact2.setId(1L);
        assertEquals(contact1.hashCode(), contact2.hashCode()); // Võrdsetel objektidel peab olema sama hashCode
    }

    @Test
    void testHashCodeMitteVordsetelObjektidel() {
        Contact contact1 = new Contact();
        contact1.setId(1L);
        Contact contact2 = new Contact();
        contact2.setId(2L);
        assertNotEquals(contact1.hashCode(), contact2.hashCode()); // Mittevõrdsetel objektidel peaks olema erinev hashCode (ei ole kohustuslik, aga soovituslik)
    }
}
