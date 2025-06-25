package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final UserDetailsService userDetailsService;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder);

    return new ProviderManager(provider);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    AuthenticationManager manager = authenticationManager(http);
    JsonUsernamePasswordAuthenticationFilter loginFilter = new JsonUsernamePasswordAuthenticationFilter(
        manager);
    loginFilter.setAuthenticationSuccessHandler(successHandler());
    loginFilter.setAuthenticationFailureHandler(failureHandler());

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
        .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        )
        .securityContext(securityContext -> securityContext
            .securityContextRepository(new HttpSessionSecurityContextRepository())
        )
        .httpBasic(Customizer.withDefaults());

    SecurityFilterChain chain = http.build();

    // 필터 체인에서 LogoutFilter 제거
    chain.getFilters().removeIf(filter -> filter instanceof LogoutFilter);

    return chain;
  }

  private AuthenticationSuccessHandler successHandler() {
    return (request, response, authentication) -> {
      UserDto userDto = userMapper.toDto((User) authentication.getPrincipal());

      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      new ObjectMapper().writeValue(response.getWriter(), userDto);
    };
  }

  private AuthenticationFailureHandler failureHandler() {
    return (request, response, exception) -> {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      new ObjectMapper().writeValue(response.getWriter(),
          Map.of("message", "인증 실패", "error", exception.getMessage()));
    };
  }

}
