package ee.smit.service;

import ee.smit.model.CarBrand;
import ee.smit.repository.CarBrandRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarBrandHierarchyService {

    private final CarBrandRepository carBrandRepository;

    public CarBrandHierarchyService(CarBrandRepository carBrandRepository) {
        this.carBrandRepository = carBrandRepository;
    }

    public List<CarBrand> getHierarchicalCarBrands(Long parentId, int level) {
        List<CarBrand> hierarchicalBrands = new ArrayList<>();
        List<CarBrand> currentLevelBrands;

        if (parentId == null) {
            currentLevelBrands = carBrandRepository.findAllByParentIsNullOrderByNameAsc();
        } else {
            currentLevelBrands = carBrandRepository.findByParentIdOrderByNameAsc(parentId);
        }

        for (CarBrand brand : currentLevelBrands) {
            StringBuilder indent = new StringBuilder();
            for (int i = 0; i < level; i++) {
                indent.append("    "); // 4 spaces for each level
            }
            brand.setName(indent.toString() + brand.getName());
            hierarchicalBrands.add(brand);

            // Recursive call for sub-brands
            hierarchicalBrands.addAll(getHierarchicalCarBrands(brand.getId(), level + 1));
        }
        return hierarchicalBrands;
    }
}