package com.sprint.mission.discodeit.config;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.constant.Role;
import com.sprint.mission.discodeit.security.CustomSessionInformationExpiredStrategy;
import com.sprint.mission.discodeit.security.DiscodeitUsernamePasswordAuthenticationFilter;
import com.sprint.mission.discodeit.security.SessionRegistryLogoutHandler;
import java.time.Duration;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyAuthoritiesMapper;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(
        HttpSecurity http,
        DaoAuthenticationProvider daoAuthenticationProvider,
        ObjectMapper objectMapper,
        SessionRegistry sessionRegistry,
        PersistentTokenBasedRememberMeServices rememberMeServices
    ) throws Exception {

        http
            .formLogin(AbstractHttpConfigurer::disable)
            .logout(logout -> logout
                .logoutUrl("/api/auth/logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler((request, response, authentication) -> {
                    SecurityContextHolder.clearContext();
                    rememberMeServices.logout(request, response, authentication);
                })
                .addLogoutHandler(new SessionRegistryLogoutHandler(sessionRegistry))
                .clearAuthentication(true)
                .permitAll()
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(
                    request -> "POST".equals(request.getMethod()) && request.getRequestURI().equals("/api/users"),
                    request -> request.getRequestURI().equals("/api/auth/login"),
                    request -> request.getRequestURI().equals("/api/auth/logout")
                )
                .csrfTokenRepository(cookieCsrfTokenRepository())
            )
            .authenticationProvider(daoAuthenticationProvider)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/csrf-token", "/api/auth/login").permitAll()
                .requestMatchers(POST, "/api/users").permitAll()
                .requestMatchers(PATCH, "/api/auth/role").hasRole(Role.ADMIN.name())
                .requestMatchers(POST, "/api/channels/public", "/api/channels/private").hasRole(Role.CHANNEL_MANAGER.name())
                .requestMatchers(PATCH, "/api/channels").hasRole(Role.CHANNEL_MANAGER.name())
                .requestMatchers(DELETE, "api/channels").hasRole(Role.CHANNEL_MANAGER.name())
                .requestMatchers("/api/**").hasRole(Role.USER.name())
                .anyRequest().permitAll()
            )
            .sessionManagement(session -> session
                .sessionFixation().migrateSession()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .sessionRegistry(sessionRegistry)
                .expiredSessionStrategy(new CustomSessionInformationExpiredStrategy(objectMapper))
            )
            .with(new DiscodeitUsernamePasswordAuthenticationFilter.Configurer(objectMapper), Customizer.withDefaults())
            .rememberMe(rememberMe -> rememberMe.rememberMeServices(rememberMeServices));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(
        UserDetailsService userDetailsService,
        PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        provider.setAuthoritiesMapper(new RoleHierarchyAuthoritiesMapper(roleHierarchy()));
        return provider;
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withDefaultRolePrefix()
            .role(Role.ADMIN.name()).implies(Role.CHANNEL_MANAGER.name())
            .role(Role.CHANNEL_MANAGER.name()).implies(Role.USER.name())
            .build();
    }

    @Bean
    public PersistentTokenBasedRememberMeServices rememberMeServices(
        @Value("${discodeit.rememberme.key}") String key,
        @Value("${discodeit.rememberme.token-validity-day}") int tokenValidityDay,
        UserDetailsService userDetailsService,
        DataSource dataSource
    ) {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);

        PersistentTokenBasedRememberMeServices rememberMeServices = new PersistentTokenBasedRememberMeServices(
            key,
            userDetailsService,
            tokenRepository
        );

        Duration tokenValidity = Duration.ofDays(tokenValidityDay);
        rememberMeServices.setTokenValiditySeconds((int) tokenValidity.getSeconds());

        return rememberMeServices;
    }

    @Bean
    public SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(sessionRegistry());
    }

    @Bean
    public CookieCsrfTokenRepository cookieCsrfTokenRepository() {
        CookieCsrfTokenRepository repository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        repository.setCookieName("XSRF-TOKEN");
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }
}
