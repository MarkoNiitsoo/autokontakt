package ee.smit.model;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ContactTest {

    @Test
    void equals_SameObject() {
        Contact contact1 = new Contact();
        assertTrue(contact1.equals(contact1)); // Objekt iseendaga on võrdne
    }

    @Test
    void equals_NullObject() {
        Contact contact1 = new Contact();
        assertFalse(contact1.equals(null)); // Objekt null-objektiga ei ole võrdne
    }

    @Test
    void equals_DifferentClass() {
        Contact contact1 = new Contact();
        assertFalse(contact1.equals("Not a Contact object")); // Objekt teise klassi objektiga ei ole võrdne
    }

    @Test
    void equals_EqualObjects() {
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
    void equals_NotEqualDifferentId() {
        Contact contact1 = new Contact();
        contact1.setId(1L);
        Contact contact2 = new Contact();
        contact2.setId(2L);
        assertNotEquals(contact1, contact2); // Erinevad ID-d - ei ole võrdsed
    }

    @Test
    void equals_NotEqualDifferentFullName() {
        Contact contact1 = new Contact();
        contact1.setFullName("Name 1");
        Contact contact2 = new Contact();
        contact2.setFullName("Name 2");
        assertNotEquals(contact1, contact2); // Erinevad nimed - ei ole võrdsed
    }

    @Test
    void equals_NotEqualDifferentPhone() {
        Contact contact1 = new Contact();
        contact1.setPhone("111");
        Contact contact2 = new Contact();
        contact2.setPhone("222");
        assertNotEquals(contact1, contact2); // Erinevad telefoninumbrid - ei ole võrdsed
    }

    @Test
    void equals_NotEqualDifferentDriverLicense() {
        Contact contact1 = new Contact();
        contact1.setValidDriverLicense(true);
        Contact contact2 = new Contact();
        contact2.setValidDriverLicense(false);
        assertNotEquals(contact1, contact2); // Erinevad juhiloa staatused - ei ole võrdsed
    }

    @Test
    void equals_NotEqualDifferentSelectedCarBrands() {
        Contact contact1 = new Contact();
        contact1.setSelectedCarBrands(Set.of(new CarBrand("Brand1", null)));
        Contact contact2 = new Contact();
        contact2.setSelectedCarBrands(Set.of(new CarBrand("Brand2", null)));
        assertNotEquals(contact1, contact2); // Erinevad automargid - ei ole võrdsed
    }

    @Test
    void hashCode_EqualObjects() {
        Contact contact1 = new Contact();
        contact1.setId(1L);
        Contact contact2 = new Contact();
        contact2.setId(1L);
        assertEquals(contact1.hashCode(), contact2.hashCode()); // Võrdsetel objektidel peab olema sama hashCode
    }

    @Test
    void hashCode_NotEqualObjects() {
        Contact contact1 = new Contact();
        contact1.setId(1L);
        Contact contact2 = new Contact();
        contact2.setId(2L);
        assertNotEquals(contact1.hashCode(), contact2.hashCode()); // Mittevõrdsetel objektidel peaks olema erinev hashCode (ei ole kohustuslik, aga soovituslik)
    }
}