package com.arryved.example.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.arryved.example.ExampleProcessor;
import com.arryved.example.ExampleService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@Profile("!dev")
public class ExampleSecurityConfig {
  
  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    http.csrf(CsrfSpec::disable)
        .authorizeExchange(exchanges -> exchanges
            .pathMatchers(HttpMethod.POST)
            .permitAll()
            .anyExchange().permitAll()
        )
        .httpBasic(withDefaults())
        .formLogin(withDefaults());
    return http.build();
  }
  
}
