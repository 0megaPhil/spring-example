package com.arryved.sdk;

import com.arryved.sdk.api.EchoApi;
import com.arryved.sdk.models.EchoRequest;
import com.arryved.sdk.models.EchoResponse;
import com.arryved.sdk.models.YellRequest;
import com.arryved.sdk.models.YellResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class EchoApiImpl implements EchoApi {
  
  private final WebClient client;
  
  public EchoApiImpl(String basePath) {
    this.client = WebClient.builder()
        .filter((request, next) -> next.exchange(request).flatMap(res -> {
          if (res.statusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
            res = res.mutate().rawStatusCode(299).build();
          }
          return Mono.just(res);
        }))
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
            .retrieve()
            .bodyToMono(EchoResponse.class));
  }
  
  @Override
  public Mono<YellResponse> yell(Mono<YellRequest> yellRequest) {
    return yellRequest.flatMap(req ->
        client.post()
            .uri("/echo/yell")
            .body(BodyInserters.fromProducer(yellRequest, YellRequest.class))
            .retrieve()
            .bodyToMono(YellResponse.class));
  }
  
}
