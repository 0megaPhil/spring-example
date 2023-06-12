package com.arryved.example;

import com.arryved.core.AbstractController;
import com.arryved.example.models.EchoRequest;
import com.arryved.example.models.EchoResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ExampleController extends AbstractController {
  private final ExampleService exampleService;
  
  public ExampleController(ExampleService exampleService) {
    this.exampleService = exampleService;
  }
  
  @PostMapping(value = "/echo", consumes = MediaType.ALL_VALUE)
  public Mono<EchoResponse> echo(@RequestBody EchoRequest request) {
    return exampleService.exchangeEcho(request);
  }

}
