package com.arryved.sdk;

import static org.awaitility.Awaitility.await;

import com.arryved.example.ExampleController;
import com.arryved.example.ExampleSecurityConfiguration;
import com.arryved.sdk.models.EchoRequest;
import com.arryved.sdk.models.EchoResponse;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootTest(classes = {
    ExampleController.class,
    ExampleSecurityConfiguration.class,
    SdkTestConfig.class},
webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@EnableAutoConfiguration
public class EchoApiTest {
  
  @Autowired
  public EchoApiImpl echoApi;
  
  @Test
  void echoAsync() {
    AtomicInteger count = new AtomicInteger();
    Disposable disposable = echoResponseProcessor(reqFlux)
        .takeUntil(r -> count.getAndIncrement() == 10)
        .subscribe(m -> {
          System.out.println("Echo: " + m.getEcho());
          Assertions.assertThat(m).isNotNull();
          Assertions.assertThat(m.getEcho()).isNotEmpty();
        });
    await().atMost(Duration.ofSeconds(60)).until(disposable::isDisposed);
  }
  
  Flux<Mono<EchoRequest>> reqFlux = Flux.generate(() -> Instant.now().toString(),
      (echoText, sink) -> {
    sink.next(Mono.just(new EchoRequest().echo(echoText)));
    return Instant.now().toString();
  });
  
  Flux<EchoResponse> echoResponseProcessor(Flux<Mono<EchoRequest>> reqFlux) {
    return reqFlux
        .flatMap(r -> echoApi.echo(r).retryWhen(reactor.util.retry.Retry
            .backoff(2, Duration.ofMillis(1000)).jitter(0d)
            .doAfterRetry(rs -> System.out.println("retried at " + LocalTime.now() + ", attempt " + rs.totalRetries()))
            .onRetryExhaustedThrow((spec, rs) -> rs.failure())
        ).doOnError(e -> System.out.println(e.getMessage())));
  }
  
}
