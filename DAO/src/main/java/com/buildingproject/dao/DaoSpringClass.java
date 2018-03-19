package com.buildingproject.dao;

import com.buildingproject.commons.Building;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackageClasses = Building.class)
public class DaoSpringClass {
  public static void main(String[] args) {
    SpringApplication.run(DaoSpringClass.class, args);
  }
}
