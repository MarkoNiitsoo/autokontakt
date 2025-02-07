package ee.smit.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;

@Entity
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
}
