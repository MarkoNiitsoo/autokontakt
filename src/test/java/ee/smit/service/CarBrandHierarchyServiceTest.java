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

public class CarBrandHierarchyServiceTest {

    @Mock
    private CarBrandRepository carBrandRepository;

    @InjectMocks
    private CarBrandHierarchyService carBrandHierarchyService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getHierarchicalCarBrands_TopLevelBrandsOnly() {
        // Arrange
        CarBrand brand1 = new CarBrand();
        brand1.setId(1L);
        brand1.setName("Brand 1");

        CarBrand brand2 = new CarBrand();
        brand2.setId(2L);
        brand2.setName("Brand 2");

        when(carBrandRepository.findAllByParentIsNullOrderByNameAsc()).thenReturn(Arrays.asList(brand1, brand2));
        when(carBrandRepository.findByParentIdOrderByNameAsc(1L)).thenReturn(Collections.emptyList());
        when(carBrandRepository.findByParentIdOrderByNameAsc(2L)).thenReturn(Collections.emptyList());


        // Act
        List<CarBrand> hierarchicalBrands = carBrandHierarchyService.getHierarchicalCarBrands(null, 0);

        // Assert
        assertEquals(2, hierarchicalBrands.size());
        assertEquals("Brand 1", hierarchicalBrands.get(0).getName());
        assertEquals("Brand 2", hierarchicalBrands.get(1).getName());
    }

    @Test
    void getHierarchicalCarBrands_WithSubBrands() {
        // Arrange
        CarBrand brand1 = new CarBrand();
        brand1.setId(1L);
        brand1.setName("Brand 1");

        CarBrand subBrand1 = new CarBrand();
        subBrand1.setId(3L);
        subBrand1.setName("Sub-brand 1");
        subBrand1.setParent(brand1);

        CarBrand subBrand2 = new CarBrand();
        subBrand2.setId(4L);
        subBrand2.setName("Sub-brand 2");
        subBrand2.setParent(brand1);


        when(carBrandRepository.findAllByParentIsNullOrderByNameAsc()).thenReturn(Collections.singletonList(brand1));
        when(carBrandRepository.findByParentIdOrderByNameAsc(1L)).thenReturn(Arrays.asList(subBrand1, subBrand2));
        when(carBrandRepository.findByParentIdOrderByNameAsc(3L)).thenReturn(Collections.emptyList());
        when(carBrandRepository.findByParentIdOrderByNameAsc(4L)).thenReturn(Collections.emptyList());


        // Act
        List<CarBrand> hierarchicalBrands = carBrandHierarchyService.getHierarchicalCarBrands(null, 0);

        // Assert
        assertEquals(3, hierarchicalBrands.size());
        assertEquals("Brand 1", hierarchicalBrands.get(0).getName());
        String subBrand1Name = hierarchicalBrands.get(1).getName();
        String subBrand2Name = hierarchicalBrands.get(2).getName();
        
        // Check indentation (4 spaces) and content separately
        assertEquals(4, subBrand1Name.indexOf("Sub-brand 1")); // Verify 4 spaces before name
        assertEquals(4, subBrand2Name.indexOf("Sub-brand 2")); // Verify 4 spaces before name
        assertTrue(subBrand1Name.endsWith("Sub-brand 1")); // Verify the actual name
        assertTrue(subBrand2Name.endsWith("Sub-brand 2")); // Verify the actual name
    }

    // Siia saad lisada veel teste erinevate hierarhia tasemete ja stsenaariumide jaoks
}
