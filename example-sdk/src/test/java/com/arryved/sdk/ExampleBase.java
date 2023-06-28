package com.arryved.sdk;

import com.arryved.example.ExampleController;
import com.arryved.example.ExampleEchoListener;
import com.arryved.example.ExampleProcessor;
import com.arryved.example.config.ExampleSecurityConfig;
import com.arryved.example.config.ExampleServiceConfig;
import com.arryved.sdk.models.Echo;
import com.arryved.sdk.models.EchoRequest;
import com.arryved.sdk.models.EchoResponse;
import com.arryved.sdk.models.YellRequest;
import com.arryved.sdk.models.YellResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.AsyncConfigurationSelector;
import org.springframework.scheduling.annotation.SchedulingConfiguration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootTest(classes = {
    ExampleController.class,
    ExampleSecurityConfig.class,
    ExampleProcessor.class,
    ExampleServiceConfig.class,
    ExampleEchoListener.class,
    AsyncConfigurationSelector.class,
    SchedulingConfiguration.class,
    SdkTestConfig.class},
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@EnableAutoConfiguration
@Execution(ExecutionMode.CONCURRENT)
public class ExampleBase {
  
  @Autowired
  public EchoApiImpl echoApi;
  
  protected Flux<Mono<EchoRequest>> echoReqFlux = Flux.generate(() -> Instant.now().toString(),
      (echoText, sink) -> {
        sink.next(Mono.defer(() -> Mono.just(new EchoRequest().echo(new Echo().message(echoText)))));
        return Instant.now().toString();
      });
  
  protected Flux<Mono<YellRequest>> yellReqFlux = Flux.generate(() -> Instant.now().toString(),
      (echoText, sink) -> {
        sink.next(Mono.defer(() -> Mono.just(new YellRequest().echo(new Echo().message(echoText)))));
        return Instant.now().toString();
      });
  
  protected Flux<EchoResponse> echoResponseProcessor(Flux<Mono<EchoRequest>> reqFlux) {
    return reqFlux
        .flatMap(r -> echoApi.echo(r)
            .retryWhen(reactor.util.retry.Retry
                .backoff(2, Duration.ofMillis(1000)).jitter(0d)));
  }
  
  protected Flux<YellResponse> yellResponseProcessor(Flux<Mono<YellRequest>> reqFlux) {
    return reqFlux
        .flatMap(r -> echoApi.yell(r)
            .retryWhen(reactor.util.retry.Retry
                .backoff(2, Duration.ofMillis(1000)).jitter(0d)));
  }
  
  protected Flux<YellResponse> translateToYelling(Flux<EchoResponse> echoResponseFlux) {
    return echoResponseFlux
        .map(echoResponse -> Mono.defer(() -> Mono.just(
            new YellRequest().echo(new Echo().message(echoResponse.getEcho().getMessage().toUpperCase())))))
        .flatMap(r -> echoApi.yell(r)).retry();
  }
  
  protected Flux<EchoResponse> translateToEcho(Flux<YellResponse> echoResponseFlux) {
    return echoResponseFlux
        .map(echoResponse -> Mono.defer(() -> Mono.just(
            new EchoRequest().echo(new Echo().message(echoResponse.getEcho().getMessage().toLowerCase())))))
        .flatMap(m -> echoApi.echo(m));
  }
  
}
