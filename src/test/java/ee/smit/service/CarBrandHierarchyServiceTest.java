package ee.smit.service;

import ee.smit.model.CarBrand;
import ee.smit.repository.CarBrandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Testid CarBrandHierarchyService klassi jaoks.
 * Kontrollib automarkide hierarhilise struktuuri loomist ja kuvamist.
 */
public class CarBrandHierarchyServiceTest {

    @Mock
    private CarBrandRepository carBrandRepository;

    @InjectMocks
    private CarBrandHierarchyService carBrandHierarchyService;

    @BeforeEach
    public void seadista() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAinultTippTasemeMarkidega() {
        // Ettevalmistus
        CarBrand mark1 = new CarBrand();
        mark1.setId(1L);
        mark1.setName("Mercedes-Benz");

        CarBrand mark2 = new CarBrand();
        mark2.setId(2L);
        mark2.setName("BMW");

        when(carBrandRepository.findAllByParentIsNullOrderByNameAsc()).thenReturn(Arrays.asList(mark1, mark2));
        when(carBrandRepository.findByParentIdOrderByNameAsc(1L)).thenReturn(Collections.emptyList());
        when(carBrandRepository.findByParentIdOrderByNameAsc(2L)).thenReturn(Collections.emptyList());

        // Tegevus
        List<CarBrand> hierarhilisedMargid = carBrandHierarchyService.getHierarchicalCarBrands(null, 0);

        // Kontroll
        assertEquals(2, hierarhilisedMargid.size());
        assertEquals("Mercedes-Benz", hierarhilisedMargid.get(0).getName());
        assertEquals("BMW", hierarhilisedMargid.get(1).getName());
    }

    @Test
    void testAlamMarkidega() {
        // Ettevalmistus
        CarBrand mark1 = new CarBrand();
        mark1.setId(1L);
        mark1.setName("Mercedes-Benz");

        CarBrand alamMark1 = new CarBrand();
        alamMark1.setId(3L);
        alamMark1.setName("C-Klass");
        alamMark1.setParent(mark1);

        CarBrand alamMark2 = new CarBrand();
        alamMark2.setId(4L);
        alamMark2.setName("E-Klass");
        alamMark2.setParent(mark1);

        when(carBrandRepository.findAllByParentIsNullOrderByNameAsc()).thenReturn(Collections.singletonList(mark1));
        when(carBrandRepository.findByParentIdOrderByNameAsc(1L)).thenReturn(Arrays.asList(alamMark1, alamMark2));
        when(carBrandRepository.findByParentIdOrderByNameAsc(3L)).thenReturn(Collections.emptyList());
        when(carBrandRepository.findByParentIdOrderByNameAsc(4L)).thenReturn(Collections.emptyList());

        // Tegevus
        List<CarBrand> hierarhilisedMargid = carBrandHierarchyService.getHierarchicalCarBrands(null, 0);

        // Kontroll
        assertEquals(3, hierarhilisedMargid.size());
        assertEquals("Mercedes-Benz", hierarhilisedMargid.get(0).getName());
        String alamMark1Nimi = hierarhilisedMargid.get(1).getName();
        String alamMark2Nimi = hierarhilisedMargid.get(2).getName();
        
        // Kontrollime taanet (4 tühikut) ja sisu eraldi
        assertEquals(4, alamMark1Nimi.indexOf("C-Klass")); // Kontrollime, et enne nime on 4 tühikut
        assertEquals(4, alamMark2Nimi.indexOf("E-Klass")); // Kontrollime, et enne nime on 4 tühikut
        assertTrue(alamMark1Nimi.endsWith("C-Klass")); // Kontrollime tegelikku nime
        assertTrue(alamMark2Nimi.endsWith("E-Klass")); // Kontrollime tegelikku nime
    }

    // Siia saab lisada veel teste erinevate hierarhia tasemete ja stsenaariumide jaoks
}
