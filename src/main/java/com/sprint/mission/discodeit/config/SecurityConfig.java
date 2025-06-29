package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.security.LoginAuthenticationFilter;
import com.sprint.mission.discodeit.security.LoginAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
public class SecurityConfig {

    private final CorsConfig corsConfig;

    public SecurityConfig(CorsConfig corsConfig) {
        this.corsConfig = corsConfig;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, LoginAuthenticationProvider provider) throws Exception {

        AuthenticationManager manager = new ProviderManager(provider);

        LoginAuthenticationFilter loginFilter = new LoginAuthenticationFilter(manager);

        http
                .csrf(csrf -> csrf
                    .ignoringRequestMatchers("/api/auth/logout")
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .addFilterBefore(corsConfig.corsFilter(), UsernamePasswordAuthenticationFilter.class) // CORS 필터 추가
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/swagger/**", "/actuator/**",
                        "/static/**", "/api/auth/csrf-token",
                        "/api/users", "/api/auth/login",
                        "/api/auth/logout"
                        ).permitAll()
                    .requestMatchers("/api/channels/**").hasRole("CHANNEL_MANAGER")
                    .requestMatchers("/api/auth/role").hasRole("ADMIN")
                    .requestMatchers("/api/**").hasRole("USER")
                    .anyRequest().denyAll()
                )
            .logout(logout -> logout.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
        LoginAuthenticationProvider provider) {
        return new ProviderManager(provider);
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}
