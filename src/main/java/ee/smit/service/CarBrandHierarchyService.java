package ee.smit.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    
    private static final Logger log = LoggerFactory.getLogger(CarBrandHierarchyService.class);
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
        log.debug("Laen hierarhilised automargid. ParentId: {}, Tase: {}", parentId, level);
        List<CarBrand> hierarchicalBrands = new ArrayList<>();
        List<CarBrand> currentLevelBrands;

        try {
            if (parentId == null) {
                log.debug("Laen tippkategooria margid");
                currentLevelBrands = carBrandRepository.findAllByParentIsNullOrderByNameAsc();
            } else {
                log.debug("Laen alamkategooria margid parent ID-ga: {}", parentId);
                currentLevelBrands = carBrandRepository.findByParentIdOrderByNameAsc(parentId);
            }
            log.debug("Leitud {} marki tasemel {}", currentLevelBrands.size(), level);
        } catch (Exception e) {
            log.error("Viga automarkide laadimisel: {}", e.getMessage());
            throw e;
        }

        for (CarBrand brand : currentLevelBrands) {
            StringBuilder indent = new StringBuilder();
            for (int i = 0; i < level; i++) {
                indent.append("    "); // 4 tyhikut iga taseme kohta
            }
            brand.setName(indent.toString() + brand.getName());
            hierarchicalBrands.add(brand);

            log.trace("Lisan rekursiivselt alamargid ID-le: {}", brand.getId());
            hierarchicalBrands.addAll(getHierarchicalCarBrands(brand.getId(), level + 1));
        }
        log.debug("Tagastan kokku {} marki", hierarchicalBrands.size());
        return hierarchicalBrands;
    }
}
