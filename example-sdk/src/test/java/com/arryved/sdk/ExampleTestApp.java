package com.arryved.sdk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class ExampleTestApp {
  public static void main(String[] args) {
    SpringApplication.run(ExampleTestApp.class, args);
  }
  
}
