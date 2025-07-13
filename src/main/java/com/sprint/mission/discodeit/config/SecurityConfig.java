package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.security.JsonUsernamePasswordAuthenticationFilter;
import com.sprint.mission.discodeit.security.SecurityMatchers;
import com.sprint.mission.discodeit.security.jwt.JwtAuthenticationFilter;
import com.sprint.mission.discodeit.security.jwt.JwtLogoutHandler;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyAuthoritiesMapper;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(
        HttpSecurity http,
        ObjectMapper objectMapper,
        DaoAuthenticationProvider daoAuthenticationProvider,
        JwtService jwtService
    )
        throws Exception {
        CookieCsrfTokenRepository repository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        repository.setCookieName("XSRF-TOKEN");
        repository.setHeaderName("X-XSRF-TOKEN");

        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName("_csrf");

        http
            .authenticationProvider(daoAuthenticationProvider)
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                    SecurityMatchers.NON_API,
                    SecurityMatchers.GET_CSRF_TOKEN,
                    SecurityMatchers.SIGN_UP,
                    SecurityMatchers.LOGIN
                ).permitAll()
                .anyRequest().hasRole(Role.USER.name())
            )
            .csrf(csrf -> csrf
                .csrfTokenRepository(repository) // HttpOnly 속성 -> false
                .csrfTokenRequestHandler(requestHandler)
                .ignoringRequestMatchers(
                    SecurityMatchers.LOGIN,
                    SecurityMatchers.LOGOUT
                )
            )
            .logout(logout ->
                logout
                    .logoutRequestMatcher(SecurityMatchers.LOGOUT)
                    .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                    .addLogoutHandler(new JwtLogoutHandler(jwtService)) // JWT 기반 로그아웃 처리로 교체
            )
            .with(new JsonUsernamePasswordAuthenticationFilter.Configurer(objectMapper, jwtService),
                Customizer.withDefaults())
            .sessionManagement(session ->
                session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtService,
            objectMapper);
        http.addFilterBefore(jwtAuthenticationFilter,
            JsonUsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public String debugFilterChain(SecurityFilterChain chain) {
        log.debug("Debug Filter Chain...");
        int filterSize = chain.getFilters().size();
        IntStream.range(0, filterSize)
            .forEach(idx -> {
                log.debug("[{}/{}] {}", idx + 1, filterSize, chain.getFilters().get(idx));
            });
        return "debugFilterChain";
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(
        UserDetailsService userDetailsService,
        PasswordEncoder passwordEncoder,
        RoleHierarchy roleHierarchy
    ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        provider.setAuthoritiesMapper(new RoleHierarchyAuthoritiesMapper(roleHierarchy));
        return provider;
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withDefaultRolePrefix()
            .role(Role.ADMIN.name())
            .implies(Role.USER.name(), Role.CHANNEL_MANAGER.name())

            .role(Role.CHANNEL_MANAGER.name())
            .implies(Role.USER.name())

            .build();
    }
}
