package ee.smit.converter;

import ee.smit.model.CarBrand;
import ee.smit.repository.CarBrandRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Teisendab stringi (ID) CarBrand objektiks.
 * Kasutatakse vormist saadud automargivaliku teisendamiseks andmebaasi objektiks.
 */
@Component
public class StringToCarBrandConverter implements Converter<String, CarBrand> {

    private final CarBrandRepository carBrandRepository;

    public StringToCarBrandConverter(CarBrandRepository carBrandRepository) {
        this.carBrandRepository = carBrandRepository;
    }

    @Override
    public CarBrand convert(@NonNull String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        return carBrandRepository.findById(Long.parseLong(source))
                .orElseThrow(() -> new IllegalArgumentException("Automarki ID-ga " + source + " ei leitud"));
    }
}
