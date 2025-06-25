package com.sprint.mission.discodeit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(
    HttpSecurity http
  ) throws Exception {
    http
      .logout(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(auth -> auth
        // api의 모든 경로 인증 필요
        .requestMatchers("/api/**").authenticated()
        // api를 제외한 모든 요청에 대한 인증 불필요
        .anyRequest().permitAll()
      )
      .httpBasic(Customizer.withDefaults());

    return http.build();
  }
}
