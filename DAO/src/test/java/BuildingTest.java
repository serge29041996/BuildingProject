import com.buildingproject.commons.Building;
import com.buildingproject.dao.BuildingRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BuildingTest {
    private static final Logger log = LoggerFactory.getLogger(BuildingTest.class);

    public static void main(String[] args) {
        SpringApplication.run(BuildingTest.class);
    }

    @Bean
    public CommandLineRunner demo(BuildingRepository repository) {
        return (args) -> {
            // save a couple of building
            repository.save(new Building("Jack", "12"));
            repository.save(new Building("Chloe", "13"));
            repository.save(new Building("Kim", "15"));
            repository.save(new Building("David", "239"));
            repository.save(new Building("Michelle", "762"));

            // fetch all building
            log.info("Customers found with findAll():");
            log.info("-------------------------------");
            for (Building customer : repository.findAll()) {
                log.info(customer.toString());
            }
            log.info("");

            // fetch an individual building by ID
            Building customer = repository.findOne(1L);
            log.info("Building found with findOne(1L):");
            log.info("--------------------------------");
            log.info(customer.toString());
            log.info("");

            // fetch customers by last name
            log.info("Building found with findByLastName('Bauer'):");
            log.info("--------------------------------------------");
            for (Building bauer : repository.findByNameBuilding("Bauer")) {
                log.info(bauer.toString());
            }
            log.info("");
        };
    }
}
