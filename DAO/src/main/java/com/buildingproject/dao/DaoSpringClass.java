package com.buildingproject.dao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"com.buildingproject.dao", "com.buildingproject.commons"})
public class DaoSpringClass {
  public static void main(String[] args) {
    SpringApplication.run(DaoSpringClass.class, args);
  }
}
