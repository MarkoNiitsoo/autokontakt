package ee.smit.repository;

import ee.smit.model.CarBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarBrandRepository extends JpaRepository<CarBrand, Long> {
    // Varem: List<CarBrand> findAllByOrderByNameAsc();

    @Query("SELECT cb FROM CarBrand cb LEFT JOIN FETCH cb.parent ORDER BY cb.parent.name ASC NULLS FIRST, cb.name ASC")
    List<CarBrand> findAllHierarchical();

    List<CarBrand> findAllByParentIsNullOrderByNameAsc(); // Peamised margid tähestikujärjekorras
    List<CarBrand> findByParentIdOrderByNameAsc(Long parentId); // Alamargid tähestikujärjekorras, antud peamise margi jaoks

}