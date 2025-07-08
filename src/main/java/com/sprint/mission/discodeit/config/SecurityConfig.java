package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.domain.Role;
import com.sprint.mission.discodeit.security.CustomLogoutSuccessHandler;
import com.sprint.mission.discodeit.security.CustomSessionInformationExpiredStrategy;
import com.sprint.mission.discodeit.security.JsonUsernamePasswordAuthenticationFilter;
import com.sprint.mission.discodeit.security.SessionRegistryLogoutHandler;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyAuthoritiesMapper;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  private static final String[] WHITELIST = {
      "/", "/index.html", "/assets/**", "/favicon.ico", "/css/**", "/js/**", "/images/**",
      "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/actuator/**",
      "/api/auth/csrf-token", "/api/auth/me", "/api/auth/login", "/api/auth/logout"
  };

  @Bean
  public SecurityFilterChain filterChain(
      HttpSecurity http,
      AuthenticationProvider daoAuthenticationProvider,
      ObjectMapper objectMapper,
      SessionRegistry sessionRegistry,
      PersistentTokenBasedRememberMeServices rememberMeServices
  ) throws Exception {

    CookieCsrfTokenRepository customCookieCsrfTokenRepository =
        CookieCsrfTokenRepository.withHttpOnlyFalse();
    customCookieCsrfTokenRepository.setHeaderName("X-CSRF-TOKEN");

    http
        .authenticationProvider(daoAuthenticationProvider)
        .csrf(csrf -> csrf
            .csrfTokenRepository(customCookieCsrfTokenRepository)
            .ignoringRequestMatchers("/api/auth/logout")
        )
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(WHITELIST).permitAll()
            .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
            .requestMatchers("/api/**").authenticated()
            .anyRequest().hasRole(Role.USER.name())
        )
        .securityContext(security -> security
            .securityContextRepository(securityContextRepository())
        )
        .with(new JsonUsernamePasswordAuthenticationFilter.Configurer(objectMapper,
                rememberMeServices),
            Customizer.withDefaults())
        .logout(logout -> logout
            .logoutUrl("/api/auth/logout")
            .invalidateHttpSession(true)   // 세션 무효화
            .clearAuthentication(true)     // SecurityContext 초기화
            .deleteCookies("remember-me")  // Remember-me 쿠키 및 토큰 삭제
            .logoutSuccessHandler(new CustomLogoutSuccessHandler(objectMapper))
            .addLogoutHandler(new SessionRegistryLogoutHandler(sessionRegistry))
        )
        .sessionManagement(session ->
            session
                .sessionFixation().migrateSession()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .sessionRegistry(sessionRegistry)
                .expiredSessionStrategy(new CustomSessionInformationExpiredStrategy(objectMapper))
        )
        .rememberMe(rememberMe -> rememberMe.rememberMeServices(rememberMeServices));

    return http.build();
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
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public SecurityContextRepository securityContextRepository() {
    return new HttpSessionSecurityContextRepository(); // 인증 결과를 세션에 저장
  }

  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }

  @Bean
  public RoleHierarchy roleHierarchy() {
    RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
    String hierarchy = """
        ROLE_ADMIN > ROLE_CHANNEL_MANAGER
        ROLE_CHANNEL_MANAGER > ROLE_USER
        """;
    roleHierarchy.setHierarchy(hierarchy);
    return roleHierarchy;
  }

  @Bean
  public PersistentTokenBasedRememberMeServices rememberMeServices(
      @Value("${security.remember-me.key}") String key,
      @Value("${security.remember-me.token-validity-seconds}") int tokenValiditySeconds,
      UserDetailsService userDetailsService,
      DataSource dataSource
  ) {
    JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
    tokenRepository.setDataSource(dataSource);

    PersistentTokenBasedRememberMeServices rememberMeServices = new PersistentTokenBasedRememberMeServices(
        key, userDetailsService, tokenRepository);
    rememberMeServices.setTokenValiditySeconds(tokenValiditySeconds);

    return rememberMeServices;
  }


}
