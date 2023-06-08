package com.arryved.sdk;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class SdkTestConfig {
  
  @Value("${server.host}")
  public String hostname;
  
  @Value("${server.port}")
  public String port;
  
  @Bean
  public EchoApiImpl echoApi() {
    return new EchoApiImpl("http://" + hostname + ":" + port);
  }
}
