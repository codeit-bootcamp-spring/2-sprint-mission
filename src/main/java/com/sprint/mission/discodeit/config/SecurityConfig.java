package com.sprint.mission.discodeit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())  // CSR 대응
        )
        .authorizeHttpRequests(auth -> auth
            // 인증 제외할 경로
            .requestMatchers(
                "/api/auth/csrf-token",
                "/api/users",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/actuator/**",
                "/favicon.ico",
                "/css/**", "/js/**", "/images/**"
            ).permitAll()
            // /api/** 요청은 인증 필요

            .anyRequest().authenticated()
        )

        .httpBasic(Customizer.withDefaults());

    SecurityFilterChain chain = http.build();

    // 필터 체인에서 LogoutFilter 제거
    chain.getFilters().removeIf(filter -> filter instanceof LogoutFilter);

    return chain;
  }
}
