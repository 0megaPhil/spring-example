package com.arryved.sdk;

import static org.awaitility.Awaitility.await;

import com.arryved.sdk.models.EchoResponse;
import java.time.Duration;
import java.util.Random;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

public class EchoApiTest extends ExampleBase {
  
  @Test
  void fluxed() {
    Flux<Integer> integerFlux = Flux.fromStream(() -> Stream.of(1, 2, 3, 4, 5, 6))
        .share().delayElements(Duration.ofMillis(new Random().nextInt(1000)));
    Flux<String> stringFlux = integerFlux.map(String::valueOf).publish().autoConnect();
    Flux<Long> longFlux = integerFlux.map(Long::valueOf).publish().autoConnect();
    
    Flux<?> combined = Flux.merge(stringFlux, longFlux);
    
    Disposable disposable = combined.take(Duration.ofSeconds(300))
        .subscribe(o -> System.out.println(o.getClass() + ": " + o));
    await().atMost(Duration.ofSeconds(30)).until(disposable::isDisposed);
  }
  
  @Test
  void echoAsync() {
    Disposable disposable = echoResponseProcessor(echoReqFlux)
        .take(Duration.ofSeconds(10)).subscribe();
    await().atMost(Duration.ofSeconds(300)).until(disposable::isDisposed);
  }
  
  @Test
  void yellAsync() {
    Disposable disposable = yellResponseProcessor(yellReqFlux)
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
  
}
