package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.security.LoginAuthenticationFilter;
import com.sprint.mission.discodeit.security.LoginAuthenticationProvider;
import com.sprint.mission.discodeit.security.MultipartCsrfValidationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;

@Configuration
@EnableWebSecurity
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
                    .csrfTokenRepository(csrfTokenRepository())
                    .ignoringRequestMatchers("/api/auth/logout", "/api/auth/login", "/api/users"))
                .addFilterBefore(corsConfig.corsFilter(), UsernamePasswordAuthenticationFilter.class) // CORS 필터 추가
                .addFilterBefore(new MultipartCsrfValidationFilter(csrfTokenRepository()), CsrfFilter.class)
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers(
                        "/",
                        "/index.html",
                        "/assets/**",
                        "/favicon.ico",
                        "/api/auth/csrf-token",
                        "/api/users",
                        "/swagger-ui/**",
                        "/actuator/**",
                        "/favicon.ico",
                        "/api/auth/login",
                        "/api/auth/register",
                        "/error"
                    ).permitAll()
                    .requestMatchers("/api/channels/public/**").hasAnyRole("CHANNEL_MANAGER")
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

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        CookieCsrfTokenRepository repository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        repository.setCookieName("CSRF-TOKEN");
        repository.setHeaderName("X-CSRF-TOKEN");
        return repository;
    }
}
