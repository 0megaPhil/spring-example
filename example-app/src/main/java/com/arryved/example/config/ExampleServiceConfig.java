package com.arryved.example.config;

import com.arryved.example.ExampleProcessor;
import com.arryved.example.ExampleService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExampleServiceConfig {
  
  @Bean
  public ScheduledExecutorService scheduledExecutorService(){
    return Executors.newScheduledThreadPool(8);
  }
  
  @Bean
  public ExampleService exampleService(ExampleProcessor processor) {
    return new ExampleService(processor);
  }
  
}
