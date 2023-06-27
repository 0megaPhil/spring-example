package com.arryved.sdk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ExampleTestApp {
  public static void main(String[] args) {
    SpringApplication.run(ExampleTestApp.class, args);
  }
  
}
