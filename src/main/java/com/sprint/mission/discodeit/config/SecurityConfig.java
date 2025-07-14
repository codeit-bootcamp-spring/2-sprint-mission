package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.domain.Role;
import com.sprint.mission.discodeit.security.CustomLoginFailureHandler;
import com.sprint.mission.discodeit.security.CustomLoginSuccessHandler;
import com.sprint.mission.discodeit.security.JsonUsernamePasswordAuthenticationFilter;
import com.sprint.mission.discodeit.security.jwt.JwtAuthenticationFilter;
import com.sprint.mission.discodeit.security.jwt.JwtLogoutHandler;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.security.jwt.SecurityMatchers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyAuthoritiesMapper;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(
      HttpSecurity http,
      AuthenticationProvider daoAuthenticationProvider,
      ObjectMapper objectMapper,
      JwtService jwtService
  ) throws Exception {

    http
        .authenticationProvider(daoAuthenticationProvider)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(SecurityMatchers.PUBLIC_MATCHERS).permitAll()
            .anyRequest().hasRole(Role.USER.name())
        )
        .csrf(csrf -> csrf
            .csrfTokenRepository(cookieCsrfTokenRepository())
            .ignoringRequestMatchers(SecurityMatchers.LOGOUT)
            .csrfTokenRequestHandler(
                new CsrfTokenRequestAttributeHandler()) // CSRF 토큰을 request attribute에 넣어줌
            .sessionAuthenticationStrategy(new NullAuthenticatedSessionStrategy()) // 세션 관련 처리 비활성화
        )
        .logout(logout -> logout
            .logoutRequestMatcher(SecurityMatchers.LOGOUT)
            .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
            .addLogoutHandler(new JwtLogoutHandler(jwtService))
        )
        .with(
            new JsonUsernamePasswordAuthenticationFilter.Configurer(objectMapper),
            configurer ->
                configurer
                    .successHandler(new CustomLoginSuccessHandler(objectMapper, jwtService))
                    .failureHandler(new CustomLoginFailureHandler(objectMapper))
        )
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .addFilterBefore(
            new JwtAuthenticationFilter(jwtService, objectMapper),
            JsonUsernamePasswordAuthenticationFilter.class
        );

    return http.build();
  }

  @Bean
  public CsrfTokenRepository cookieCsrfTokenRepository() {
    CookieCsrfTokenRepository repository = CookieCsrfTokenRepository.withHttpOnlyFalse();
    repository.setCookieName("XSRF-TOKEN"); // 클라이언트에 심어줄 쿠키 이름
    repository.setHeaderName("X-XSRF-TOKEN");  // 클라이언트가 보낼 헤더 이름
    return repository;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider daoAuthenticationProvider(
      UserDetailsService userDetailsService,
      PasswordEncoder passwordEncoder,
      RoleHierarchy roleHierarchy) {
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
