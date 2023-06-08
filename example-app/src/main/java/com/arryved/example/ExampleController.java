package com.arryved.example;

import com.arryved.example.models.EchoRequest;
import com.arryved.example.models.EchoResponse;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.reactor.ratelimiter.operator.RateLimiterOperator;
import java.time.Duration;
import java.util.function.Function;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ExampleController {
  private final ExampleService exampleService;
  
  public ExampleController(ExampleService exampleService) {
    this.exampleService = exampleService;
  }
  
  @PostMapping(value = "/echo", consumes = MediaType.ALL_VALUE)
  public Mono<EchoResponse> echo(@RequestBody EchoRequest request) {
    return exampleService.exchangeEcho(request);
  }

}
