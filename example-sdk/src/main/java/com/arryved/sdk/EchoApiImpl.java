package com.arryved.sdk;

import com.arryved.sdk.api.EchoApi;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class EchoApiImpl implements EchoApi {
  
  private final WebClient client;
  
  public EchoApiImpl(String basePath) {
    this.client = WebClient.builder().baseUrl(basePath).build();
  }
  
  @Override
  public Mono<String> echo(String text) {
    return client.get().uri("/echo?text=" + text).retrieve().bodyToMono(String.class);
  }
}
