package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.security.CustomLoginFailureHandler;
import com.sprint.mission.discodeit.security.CustomLoginSuccessHandler;
import com.sprint.mission.discodeit.security.JsonUsernamePasswordAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
        HttpSecurity http,
        JsonUsernamePasswordAuthenticationFilter jsonLoginFilter
    ) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                    "/swagger-ui/**", "/v3/api-docs/**",
                    "/actuator/**",
                    "/", "/favicon.ico", "/index.html",
                    "/static/**", "/assets/**", "/profile-images/**",
                    "/api/auth/csrf-token",
                    "/api/users"
                ).permitAll()
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
            )
            .addFilterBefore(jsonLoginFilter, UsernamePasswordAuthenticationFilter.class)
            .formLogin(form -> form.disable())
            .logout(logout -> logout.disable()); // LogoutFilter 제외

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
        PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        // DaoAuthenticationProvider가 사용할 UserDetailsService를 지정
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
        DaoAuthenticationProvider provider) throws Exception {

        return http.getSharedObject(AuthenticationManagerBuilder.class)
            .authenticationProvider(provider) // DaoAuthenticationProvider 등록
            .build(); // AuthenticationManager 생성
    }

    @Bean
    public JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordAuthenticationFilter(
        AuthenticationManager authenticationManager,
        ObjectMapper objectMapper,
        UserMapper userMapper
    ) {
        JsonUsernamePasswordAuthenticationFilter filter =
            new JsonUsernamePasswordAuthenticationFilter(objectMapper);

        filter.setAuthenticationManager(authenticationManager);
        filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());

        filter.setAuthenticationSuccessHandler(
            new CustomLoginSuccessHandler(objectMapper, userMapper));
        filter.setAuthenticationFailureHandler(new CustomLoginFailureHandler(objectMapper));

        return filter;
    }
}
