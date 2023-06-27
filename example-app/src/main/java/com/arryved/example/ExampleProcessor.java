package com.arryved.example;

import com.arryved.core.AbstractModel;
import com.arryved.core.Listener;
import java.time.Duration;
import java.util.Set;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ExampleProcessor {
  
  private final Set<Listener> listeners;
  
  public ExampleProcessor(Set<Listener> listeners) {
    this.listeners = listeners;
  }
  
  public <M extends AbstractModel> Mono<M> chainAppender(Mono<M> mono) {
    return mono
        .doOnNext(o -> listeners.forEach(l -> l.begin(o)))
        .doOnSuccess(o -> listeners.forEach(l -> l.success(o)))
        .doOnError(t -> listeners.forEach(l -> l.error(t)))
        .doFinally(s -> listeners.forEach(l -> l.complete(s)))
        .onErrorResume(e -> Mono.empty())
        .retryWhen(reactor.util.retry.Retry
            .backoff(1, Duration.ofMillis(1000)).jitter(0d));
  }
  
}
