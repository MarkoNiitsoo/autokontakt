package ee.smit.converter;

import ee.smit.model.CarBrand;
import ee.smit.repository.CarBrandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Testid StringToCarBrandConverter klassi jaoks.
 * Kontrollib stringi teisendamist CarBrand objektiks erinevate sisendite puhul.
 */
class StringToCarBrandConverterTest {

    @Mock
    private CarBrandRepository carBrandRepository;

    private StringToCarBrandConverter converter;

    @BeforeEach
    void seadista() {
        MockitoAnnotations.openMocks(this);
        converter = new StringToCarBrandConverter(carBrandRepository);
    }

    @Test
    void testKorrektseIdTeisendamine() {
        // Ettevalmistus
        CarBrand eeldatavMark = new CarBrand();
        eeldatavMark.setId(1L);
        eeldatavMark.setName("Test Brand");

        when(carBrandRepository.findById(1L)).thenReturn(Optional.of(eeldatavMark));

        // Tegevus
        CarBrand tulemus = converter.convert("1");

        // Kontroll
        assertNotNull(tulemus);
        assertEquals(eeldatavMark.getId(), tulemus.getId());
        assertEquals(eeldatavMark.getName(), tulemus.getName());
    }

    @Test
    void testNullStringiTeisendamine() {
        // Tegevus ja kontroll
        assertNull(converter.convert(null));
    }

    @Test
    void testTuhjaStringiTeisendamine() {
        // Tegevus ja kontroll
        assertNull(converter.convert(""));
    }

    @Test
    void testOlematuIdTeisendamine() {
        // Ettevalmistus
        when(carBrandRepository.findById(999L)).thenReturn(Optional.empty());

        // Tegevus ja kontroll
        assertThrows(IllegalArgumentException.class, () -> converter.convert("999"),
                    "Peaks viskama erindi, kui automarki ei leita");
    }

    @Test
    void testMittearvuliseStringiTeisendamine() {
        // Tegevus ja kontroll
        assertThrows(NumberFormatException.class, () -> converter.convert("mitte-number"),
                    "Peaks viskama erindi, kui sisend pole arvuline");
    }
}
