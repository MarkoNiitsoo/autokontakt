package ee.smit.service;

import ee.smit.model.CarBrand;
import ee.smit.repository.CarBrandRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Teenus automarkide hierarhilise struktuuri haldamiseks.
 * Võimaldab kuvada automarke ja nende alammarke struktureeritud kujul,
 * kasutades taandeid erinevate tasemete eristamiseks.
 */
@Service
public class CarBrandHierarchyService {

    private final CarBrandRepository carBrandRepository;

    public CarBrandHierarchyService(CarBrandRepository carBrandRepository) {
        this.carBrandRepository = carBrandRepository;
    }

    /**
     * Tagastab automarkide hierarhilise nimekirja, kus alamkategooriad on taandega.
     * 
     * @param parentId Ülemkategooria ID või null tippkategooria jaoks
     * @param level Praegune hierarhia tase (määrab taande suuruse)
     * @return Hierarhiliselt vormindatud automarkide nimekiri
     */
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
                indent.append("    "); // 4 tyhikut iga taseme kohta
            }
            brand.setName(indent.toString() + brand.getName());
            hierarchicalBrands.add(brand);

            // Rekursiivne väljakutse alammarkide jaoks
            hierarchicalBrands.addAll(getHierarchicalCarBrands(brand.getId(), level + 1));
        }
        return hierarchicalBrands;
    }
}
