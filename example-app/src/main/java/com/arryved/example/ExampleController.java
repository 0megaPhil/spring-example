package com.arryved.example;

import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ExampleController {
  
  @GetMapping("/echo")
  public Mono<String> echo(@RequestParam(value = "text", required = false) String text) {
    return Mono.just("Example Text: " + Optional.ofNullable(text).orElse(""));
  }
  
}
