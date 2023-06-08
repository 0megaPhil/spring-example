package com.arryved.sdk;

import com.arryved.example.ExampleController;
import com.arryved.example.ExampleSecurityConfiguration;
import java.time.Instant;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {
    ExampleController.class,
    ExampleSecurityConfiguration.class,
    SdkTestConfig.class})
public class EchoApiTest {
  
  @Autowired
  public EchoApiImpl echoApi;
  
  @Test
  void echo() {
    String echoText = Instant.now().toString();
    String response = echoApi.echo(echoText).block();
    Assertions.assertThat(response).contains(echoText);
  }
  
}
