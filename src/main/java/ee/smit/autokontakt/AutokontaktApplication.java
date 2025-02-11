package ee.smit.autokontakt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "ee.smit")
@EnableJpaRepositories(basePackages = "ee.smit.repository")
@EntityScan(basePackages = "ee.smit.model")
public class AutokontaktApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutokontaktApplication.class, args);
	}

}
