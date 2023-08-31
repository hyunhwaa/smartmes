package mes.smartmes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class SmartmesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartmesApplication.class, args);

	}

}
