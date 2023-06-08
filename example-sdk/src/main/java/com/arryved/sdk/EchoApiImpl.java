package com.arryved.sdk;

import com.arryved.sdk.api.EchoApi;
import com.arryved.sdk.models.EchoRequest;
import com.arryved.sdk.models.EchoResponse;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class EchoApiImpl implements EchoApi {
  
  private final WebClient client;
  
  public EchoApiImpl(String basePath) {
    this.client = WebClient.builder()
        .defaultHeader("user-agent", "Application")
        .defaultHeader("accept", MediaType.ALL_VALUE)
        .defaultHeader("scope", "USER")
        .baseUrl(basePath)
        .build();
  }
  
  @Override
  public Mono<EchoResponse> echo(Mono<EchoRequest> echoRequest) {
    return echoRequest.flatMap(req ->
        client.post()
            .uri("/echo")
            .body(BodyInserters.fromProducer(echoRequest, EchoRequest.class))
            .retrieve().bodyToMono(EchoResponse.class));
  }
}
