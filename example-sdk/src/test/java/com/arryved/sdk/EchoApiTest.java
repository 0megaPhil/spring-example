package com.arryved.sdk;

import static org.awaitility.Awaitility.await;

import com.arryved.example.ExampleController;
import com.arryved.example.ExampleEchoListener;
import com.arryved.example.ExampleProcessor;
import com.arryved.example.config.ExampleProcessConfig;
import com.arryved.example.config.ExampleSecurityConfig;
import com.arryved.sdk.models.YellRequest;
import com.arryved.sdk.models.Echo;
import com.arryved.sdk.models.EchoRequest;
import com.arryved.sdk.models.EchoResponse;
import com.arryved.sdk.models.YellResponse;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.AsyncConfigurationSelector;
import org.springframework.scheduling.annotation.SchedulingConfiguration;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootTest(classes = {
    ExampleController.class,
    ExampleSecurityConfig.class,
    ExampleProcessor.class,
    ExampleProcessConfig.class,
    ExampleEchoListener.class,
    AsyncConfigurationSelector.class,
    SchedulingConfiguration.class,
    SdkTestConfig.class},
webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@EnableAutoConfiguration
public class EchoApiTest {
  
  @Autowired
  public EchoApiImpl echoApi;
  
  @Test
  void echoAsync() {
    AtomicInteger count = new AtomicInteger();
    Disposable disposable = echoResponseProcessor(echoReqFlux)
        .take(Duration.ofSeconds(10)).subscribe();
    await().atMost(Duration.ofSeconds(300)).until(disposable::isDisposed);
  }
  
  @Test
  void combinedAsync() {
    Flux<EchoResponse> echoResponseFlux = echoResponseProcessor(echoReqFlux).share();
    Disposable disposable0 = echoResponseFlux.take(Duration.ofSeconds(10)).subscribe();
    
    Disposable disposable1 = Flux.merge(
            translateToYelling(echoResponseFlux)).take(Duration.ofSeconds(10)).subscribe();
    
    await().atMost(Duration.ofSeconds(300)).until(() -> Stream.of(disposable1, disposable0).allMatch(Disposable::isDisposed));
  }
  
  Flux<Mono<EchoRequest>> echoReqFlux = Flux.generate(() -> Instant.now().toString(),
      (echoText, sink) -> {
    sink.next(Mono.defer(() -> Mono.just(new EchoRequest().echo(new Echo().message(echoText)))));
    return Instant.now().toString();
  });
  
  Flux<Mono<YellRequest>> yellReqFlux = Flux.generate(() -> Instant.now().toString(),
      (echoText, sink) -> {
        sink.next(Mono.defer(() -> Mono.just(new YellRequest().echo(new Echo().message(echoText)))));
        return Instant.now().toString();
      });
  
  Flux<EchoResponse> echoResponseProcessor(Flux<Mono<EchoRequest>> reqFlux) {
    return reqFlux
        .flatMap(r -> echoApi.echo(r).retryWhen(reactor.util.retry.Retry
            .backoff(2, Duration.ofMillis(1000)).jitter(0d)));
  }
  
  Flux<YellResponse> yellResponseProcessor(Flux<Mono<YellRequest>> reqFlux) {
    return reqFlux
        .flatMap(r -> echoApi.yell(r)
            .retryWhen(reactor.util.retry.Retry
            .backoff(2, Duration.ofMillis(1000)).jitter(0d)));
  }
  
  Flux<YellResponse> translateToYelling(Flux<EchoResponse> echoResponseFlux) {
    return echoResponseFlux
        .map(echoResponse -> Mono.defer(() -> Mono.just(
            new YellRequest().echo(new Echo().message(echoResponse.getEcho().getMessage().toUpperCase())))))
        .flatMap(r -> echoApi.yell(r)).retry();
  }
  
  Flux<EchoResponse> translateToEcho(Flux<YellResponse> echoResponseFlux) {
    return echoResponseFlux
        .map(echoResponse -> Mono.defer(() -> Mono.just(
            new EchoRequest().echo(new Echo().message(echoResponse.getEcho().getMessage().toLowerCase())))))
        .flatMap(m -> echoApi.echo(m));
  }
  
}
