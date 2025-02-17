package ee.smit.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "car_brand")
public class CarBrand {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Näiteks: "Mercedes-Benz" või "BMW"
    private String name;
    
    // Võimalik seos, kui on alamkategooriaid (näiteks "C klass" on "Mercedes-Benz" alam)
    @ManyToOne
    private CarBrand parent;
    
    // Konstruktorid, getterid ja setterid
    public CarBrand() { }

    public CarBrand(String name, CarBrand parent) {
        this.name = name;
        this.parent = parent;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public CarBrand getParent() {
        return parent;
    }
    
    public void setParent(CarBrand parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarBrand carBrand = (CarBrand) o;
        return (name == null ? carBrand.name == null : name.equals(carBrand.name)) &&
               (parent == null ? carBrand.parent == null : parent.equals(carBrand.parent));
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        return result;
    }
}
