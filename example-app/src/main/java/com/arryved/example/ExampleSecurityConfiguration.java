package com.arryved.example;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class ExampleSecurityConfiguration {
  
  private static final String[] AUTH_WHITELIST = {
      // -- swagger ui
      "/v2/api-docs",
      "/v3/api-docs",
      "/swagger-resources/**",
      "/swagger-ui/**",
  };
  
  @Bean
  public MapReactiveUserDetailsService userDetailsService() {
    UserDetails user = User.withDefaultPasswordEncoder()
        .username("user")
        .password("user")
        .roles("USER")
        .build();
    return new MapReactiveUserDetailsService(user);
  }
  
  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    http
        .csrf()
        .disable()
        .authorizeExchange(exchanges -> exchanges
            .pathMatchers(HttpMethod.POST)
            .permitAll()
            .anyExchange().permitAll()
        )
        .httpBasic(withDefaults())
        .formLogin(withDefaults());
    return http.build();
  }
  
  @Bean
  public ExampleService exampleService() {
    return new ExampleService();
  }
  
}
