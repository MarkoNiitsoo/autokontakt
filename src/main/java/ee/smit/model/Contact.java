package ee.smit.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "contact")
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
    @jakarta.validation.constraints.NotEmpty(message = "Palun valige vähemalt üks automark")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        // For new objects, all fields will be null/default values
        return validDriverLicense == contact.validDriverLicense &&
               (id == null ? contact.id == null : id.equals(contact.id)) &&
               (fullName == null ? contact.fullName == null : fullName.equals(contact.fullName)) &&
               (phone == null ? contact.phone == null : phone.equals(contact.phone)) &&
               (selectedCarBrands == null ? contact.selectedCarBrands == null : selectedCarBrands.equals(contact.selectedCarBrands));
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (validDriverLicense ? 1 : 0);
        result = 31 * result + (selectedCarBrands != null ? selectedCarBrands.hashCode() : 0);
        return result;
    }
}
