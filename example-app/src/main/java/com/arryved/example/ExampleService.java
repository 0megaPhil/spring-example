package com.arryved.example;

import com.arryved.core.AbstractService;
import com.arryved.example.models.EchoRequest;
import com.arryved.example.models.EchoResponse;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Mono;

@Getter
@Setter
public class ExampleService extends AbstractService {
  @RateLimiter(name = "basic")
  public Mono<EchoResponse> exchangeEcho(EchoRequest request) {
    return Mono.defer(() -> Mono.just(new EchoResponse(request.getEcho())));
  }

}
