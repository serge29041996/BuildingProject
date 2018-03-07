package com.buildingproject.rest;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.buildingproject.service"})
public class RestSpringClass {
  public static void main(String[] args) {
    SpringApplication.run(RestSpringClass.class, args);
  }
}
