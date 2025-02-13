package ee.smit.autokontakt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Autokontakt rakenduse peaklass.
 * Käivitab Spring Boot rakenduse ja seadistab vajalikud komponendid.
 * 
 * Konfiguratsioon:
 * - ComponentScan: otsib Spring komponente ee.smit paketist
 * - EnableJpaRepositories: aktiveerib JPA repositooriumid ee.smit.repository paketist
 * - EntityScan: otsib JPA üksusi ee.smit.model paketist
 */
@SpringBootApplication
@ComponentScan(basePackages = "ee.smit")
@EnableJpaRepositories(basePackages = "ee.smit.repository")
@EntityScan(basePackages = "ee.smit.model")
public class AutokontaktApplication {

    /**
     * Rakenduse käivitamise meetod.
     * 
     * @param args Käsurea argumendid (ei ole kasutusel)
     */
    public static void main(String[] args) {
        SpringApplication.run(AutokontaktApplication.class, args);
    }

}
