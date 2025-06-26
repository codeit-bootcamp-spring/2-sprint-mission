package com.sprint.mission.discodeit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.
            authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                    "/",
                    "/swagger-ui/**",
                    "/actuator/**",
                    "/favicon.ico",
                    "/index.html",
                    "/assets/**",
                    "/api/auth/login",
                    "/error"
                ).permitAll()
                .anyRequest().authenticated()

            ).formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .build();
    }
}