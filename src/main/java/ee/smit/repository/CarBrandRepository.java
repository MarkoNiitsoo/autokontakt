package ee.smit.repository;

import ee.smit.model.CarBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository automarkide andmete haldamiseks.
 * Võimaldab automarke salvestada, otsida ja kuvada hierarhiliselt.
 */
public interface CarBrandRepository extends JpaRepository<CarBrand, Long> {

    /**
     * Laeb kõik automargid koos nende ülemkategooriatega.
     * Järjestab tulemused esmalt ülemkategooria ja seejärel automargi nime järgi.
     * Ülemkategooriata margid kuvatakse esimesena.
     */
    @Query("SELECT cb FROM CarBrand cb LEFT JOIN FETCH cb.parent ORDER BY cb.parent.name ASC NULLS FIRST, cb.name ASC")
    List<CarBrand> findAllHierarchical();

    /**
     * Tagastab kõik tippkategooria automargid (ilma ülemkategooriata) tähestikulises järjekorras.
     */
    List<CarBrand> findAllByParentIsNullOrderByNameAsc();

    /**
     * Tagastab kõik alamkategooria automargid antud ülemkategooria ID järgi tähestikulises järjekorras.
     * 
     * @param parentId Ülemkategooria ID, mille alammarke otsitakse
     * @return Leitud alamkategooria automargid
     */
    List<CarBrand> findByParentIdOrderByNameAsc(Long parentId);
}
