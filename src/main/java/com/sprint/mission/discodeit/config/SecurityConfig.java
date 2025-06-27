package com.sprint.mission.discodeit.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(csrf -> csrf
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
        .authorizeHttpRequests(authorizeRequests -> authorizeRequests
            .requestMatchers("/", "/assets/**", "/index.html", "/favicon.ico",
            "/swagger", "/v3/api-docs/**", "/swagger-resources/**",
            "/actuator/**",
            "/login").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/auth/csrf-token").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
            .anyRequest().authenticated()
        )
        .formLogin(withDefaults())
        .logout(AbstractHttpConfigurer::disable)
        .build();
  }
}
