package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import java.util.Map;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final UserDetailsService userDetailsService;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;
  private final DataSource dataSource;

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
                "api/auth/login",
                "api/auth/register",
                "/css/**", "/js/**", "/images/**"
            ).permitAll()
            // /api/** 요청은 인증 필요
            .requestMatchers("/sensitive/**").
            access(new WebExpressionAuthorizationManager("isFullyAuthenticated()"))
            .requestMatchers("/", "/home", "/about").permitAll()
            .anyRequest().authenticated()
            // 관리자 페이지: ROLE_ADMIN만 접근 가능
            .requestMatchers(HttpMethod.PUT, "/api/auth/role").hasRole("ADMIN")

            // 채널 매니저 페이지: ROLE_CHANNEL_MANAGER 이상 접근 가능
            // (ADMIN도 계층에 의해 포함됨)
            .requestMatchers("/api/channels/public/").hasRole("CHANNEL_MANAGER")

            // 일반 사용자 페이지: ROLE_USER 이상 접근 가능
            // (CHANNEL_MANAGER, ADMIN도 접근 가능)

            .anyRequest().hasRole("USER")
        )
        .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .maximumSessions(1)
            .maxSessionsPreventsLogin(false)
            .sessionRegistry(sessionRegistry())
        )
        .securityContext(securityContext -> securityContext
            .securityContextRepository(new HttpSessionSecurityContextRepository())
        )
        .rememberMe(remember -> remember
            .useSecureCookie(true)   // HTTPS에서만 전송
            .tokenRepository(persistentTokenRepository())
            .tokenValiditySeconds(1814400)  // 3주
            .userDetailsService(userDetailsService)
            .rememberMeCookieName("remember-me")
            .rememberMeCookieDomain(".example.com")
            .alwaysRemember(false))      // 명시적 선택 시에만 활성화
        .logout(logout -> logout
            .logoutUrl("/logout")
            .deleteCookies("JSESSIONID", "remember-me") // 쿠키 삭제
            .invalidateHttpSession(true) // 세션 만료
        )
        .httpBasic(Customizer.withDefaults());

    SecurityFilterChain chain = http.build();

    // 필터 체인에서 LogoutFilter 제거
    chain.getFilters().removeIf(filter -> filter instanceof LogoutFilter);

    return chain;
  }

  @Bean
  public PersistentTokenRepository persistentTokenRepository() {
    JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
    tokenRepository.setDataSource(dataSource);
    return tokenRepository;
  }

  @Bean
  public RoleHierarchy roleHierarchy() {
    RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
    hierarchy.setHierarchy("ROLE_ADMIN > ROLE_CHANNEL_MANAGER \n ROLE_CHANNEL_MANAGER > ROLE_USER");
    return hierarchy;
  }


  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
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
