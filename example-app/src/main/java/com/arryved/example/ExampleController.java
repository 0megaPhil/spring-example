package com.arryved.example;

import com.arryved.core.AbstractController;
import com.arryved.example.data.Echo;
import com.arryved.example.models.EchoRequest;
import com.arryved.example.models.EchoResponse;
import com.arryved.example.models.WhisperRequest;
import com.arryved.example.models.WhisperResponse;
import com.arryved.example.models.YellRequest;
import com.arryved.example.models.YellResponse;
import java.util.Optional;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ExampleController extends AbstractController {
  private final ExampleService exampleService;
  
  public ExampleController(ExampleService exampleService) {
    this.exampleService = exampleService;
  }
  
  @PostMapping(value = "/echo", consumes = MediaType.ALL_VALUE)
  public Mono<EchoResponse> echo(@RequestBody EchoRequest request, @RequestParam(required = false) String prefix) {
    return exampleService.exchangeEcho(new EchoRequest(new Echo(Optional.ofNullable(prefix)
        .orElse("") + request.getEcho().getMessage())));
  }
  
  @PostMapping(value = "/echo/yell", consumes = MediaType.ALL_VALUE)
  public Mono<YellResponse> yell(@RequestBody YellRequest request) {
    return exampleService.exchangeYell(request);
  }
  
  @PostMapping(value = "/echo/whisper", consumes = MediaType.ALL_VALUE)
  public Mono<WhisperResponse> whisper(@RequestBody WhisperRequest request) {
    return exampleService.exchangeWhisper(request);
  }

}
