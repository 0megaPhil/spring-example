package com.arryved.example;

import com.arryved.core.AbstractService;
import com.arryved.example.data.Echo;
import com.arryved.example.models.EchoRequest;
import com.arryved.example.models.EchoResponse;
import com.arryved.example.models.YellRequest;
import com.arryved.example.models.YellResponse;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Mono;

@Getter
@Setter
public class ExampleService extends AbstractService<Echo> {
  private final ExampleProcessor processor;
  
  public ExampleService(ExampleProcessor processor) {
    this.processor = processor;
  }
  
  @RateLimiter(name = "basic")
  public Mono<EchoResponse> exchangeEcho(EchoRequest request) {
    return processor.chainAppender(Mono.defer(() -> Mono.just(new EchoResponse(request.getEcho()))));
  }
  
  @RateLimiter(name = "polite")
  public Mono<YellResponse> exchangeYell(YellRequest request) {
    return processor.chainAppender(Mono.defer(() -> Mono.just(new YellResponse(request.getEcho()))));
  }

}
