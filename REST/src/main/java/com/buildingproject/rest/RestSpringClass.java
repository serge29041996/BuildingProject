package com.buildingproject.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.buildingproject.service", "com.buildingproject.dao", "com.buildingproject.rest"})
public class RestSpringClass {
  public static void main(String[] args) {
    SpringApplication.run(RestSpringClass.class, args);
  }
}
