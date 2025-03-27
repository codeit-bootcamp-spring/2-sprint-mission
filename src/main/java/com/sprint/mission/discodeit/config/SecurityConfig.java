package com.sprint.mission.discodeit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/users").permitAll() // 사용자 등록 경로 인증 없이 허용
                        .requestMatchers("/api/auth/login").permitAll() // 로그인 경로 인증 없이 허용
                        .requestMatchers("/api/users/findAll").permitAll() // 사용자 목록 조회 인증 없이 허용
                        .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                )
                .formLogin(Customizer.withDefaults()) // 기본 폼 로그인 설정
                .csrf(AbstractHttpConfigurer::disable); // CSRF 보호 기능 비활성화
        return http.build();
    }

}
