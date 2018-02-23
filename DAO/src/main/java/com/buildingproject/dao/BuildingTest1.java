package com.buildingproject.dao;

import com.buildingproject.commons.Building;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.sql.DataSource;

@SpringBootApplication
@EntityScan(basePackageClasses=Building.class)              ///what need for the finding entity
public class BuildingTest1 implements CommandLineRunner {
    //private static final Logger log = LoggerFactory.getLogger(BuildingTest1.class);

    @Autowired
    DataSource dataSource;

    public static void main(String[] args) {
        SpringApplication.run(BuildingTest1.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("DATASOURCE = " + dataSource);

    }

    /*
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
    } */
}
