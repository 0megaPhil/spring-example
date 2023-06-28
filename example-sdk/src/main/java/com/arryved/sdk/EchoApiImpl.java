package com.arryved.sdk;

import com.arryved.sdk.api.EchoApi;
import com.arryved.sdk.models.EchoRequest;
import com.arryved.sdk.models.EchoResponse;
import com.arryved.sdk.models.WhisperRequest;
import com.arryved.sdk.models.WhisperResponse;
import com.arryved.sdk.models.YellRequest;
import com.arryved.sdk.models.YellResponse;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
  
  public Mono<EchoResponse> echo(Mono<EchoRequest> echoRequest) {
    return echo(echoRequest, null);
  }
  
  @Override
  public Mono<EchoResponse> echo(Mono<EchoRequest> echoRequest, String prefix) {
    return exchange(EchoRequest.class, EchoResponse.class, "/echo",
        Optional.ofNullable(prefix).map(p -> {
          MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
          params.put("prefix", List.of(prefix));
          return params;
        }).orElseGet(LinkedMultiValueMap::new)).apply(echoRequest);
  }
  
  @Override
  public Mono<WhisperResponse> whisper(Mono<WhisperRequest> whisperRequest) {
    return exchange(WhisperRequest.class, WhisperResponse.class, "/echo/whisper", null).apply(whisperRequest);
  }
  
  @Override
  public Mono<YellResponse> yell(Mono<YellRequest> yellRequest) {
    return exchange(YellRequest.class, YellResponse.class, "/echo/yell", null).apply(yellRequest);
  }
  
  private <S, R> Function<Mono<S>, Mono<R>> exchange(
      Class<S> requestClass,
      Class<R> responseClass,
      String uri,
      MultiValueMap<String, String> params) {
    return mono -> mono.flatMap(req -> client.post()
        .uri(uriBuilder -> uriBuilder.path(uri).queryParams(params).build())
        .body(BodyInserters.fromProducer(mono, requestClass))
        .retrieve()
        .bodyToMono(responseClass));
  }
  
}
