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

class StringToCarBrandConverterTest {

    @Mock
    private CarBrandRepository carBrandRepository;

    private StringToCarBrandConverter converter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        converter = new StringToCarBrandConverter(carBrandRepository);
    }

    @Test
    void convertValidId() {
        // Arrange
        CarBrand expectedBrand = new CarBrand();
        expectedBrand.setId(1L);
        expectedBrand.setName("Test Brand");

        when(carBrandRepository.findById(1L)).thenReturn(Optional.of(expectedBrand));

        // Act
        CarBrand result = converter.convert("1");

        // Assert
        assertNotNull(result);
        assertEquals(expectedBrand.getId(), result.getId());
        assertEquals(expectedBrand.getName(), result.getName());
    }

    @Test
    void convertNullString() {
        // Act & Assert
        assertNull(converter.convert(null));
    }

    @Test
    void convertEmptyString() {
        // Act & Assert
        assertNull(converter.convert(""));
    }

    @Test
    void convertNonExistentId() {
        // Arrange
        when(carBrandRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> converter.convert("999"));
    }

    @Test
    void convertInvalidNumber() {
        // Act & Assert
        assertThrows(NumberFormatException.class, () -> converter.convert("not-a-number"));
    }
}
