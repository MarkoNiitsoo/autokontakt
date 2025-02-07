package ee.smit.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Ees- ja perekonnanimi on kohustuslik")
    private String fullName;
    
    @NotBlank(message = "Kontakttelefon on kohustuslik")
    private String phone;

    // Näide: kas kasutajal on kehtiv juhiluba
    private boolean validDriverLicense;

    // Seos automarkidega – mitme omavaheline seos
    @ManyToMany
    @JoinTable(
        name = "contact_carbrands",
        joinColumns = @JoinColumn(name = "contact_id"),
        inverseJoinColumns = @JoinColumn(name = "carbrand_id")
    )
    private Set<CarBrand> selectedCarBrands;

    // Konstruktor, getterid ja setterid
    public Contact() { }

    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public boolean isValidDriverLicense() {
        return validDriverLicense;
    }
    
    public void setValidDriverLicense(boolean validDriverLicense) {
        this.validDriverLicense = validDriverLicense;
    }
    
    public Set<CarBrand> getSelectedCarBrands() {
        return selectedCarBrands;
    }
    
    public void setSelectedCarBrands(Set<CarBrand> selectedCarBrands) {
        this.selectedCarBrands = selectedCarBrands;
    }
}
