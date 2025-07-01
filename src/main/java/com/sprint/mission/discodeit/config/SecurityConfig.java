package com.sprint.mission.discodeit.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.exception.auth.CustomAccessDeniedHandler;
import com.sprint.mission.discodeit.security.CustomSessionInformationExpiredStrategy;
import com.sprint.mission.discodeit.security.JsonUsernamePasswordAuthenticationFilter;
import com.sprint.mission.discodeit.security.SessionRegistryLogoutHandler;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyAuthoritiesMapper;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@Profile("!test")
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, ObjectMapper objectMapper,
      SessionRegistry sessionRegistry,
      CustomAccessDeniedHandler accessDeniedHandler,
      PersistentTokenBasedRememberMeServices rememberMeServices)
      throws Exception {

    // 프론트에서 X-CSRF-TOKEN 헤더로 토큰을 보내주지만, CookieCsrfTokenRepository에선 X-XSRF-TOKEN을 기대함 -> 헤더명 명시적 지정
    CookieCsrfTokenRepository repository =
        CookieCsrfTokenRepository.withHttpOnlyFalse();
    repository.setHeaderName("X-CSRF-TOKEN");

    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/csrf-token").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/channels/public")
            .hasRole(Role.CHANNEL_MANAGER.name())
            .requestMatchers(HttpMethod.DELETE, "/api/channels/**")
            .hasRole(Role.CHANNEL_MANAGER.name())
            .requestMatchers(HttpMethod.PATCH, "/api/channels/**")
            .hasRole(Role.CHANNEL_MANAGER.name())
            .requestMatchers(HttpMethod.PATCH, "/api/auth/role").hasRole(Role.ADMIN.name())
            .requestMatchers("/api/**").hasRole(Role.USER.name())
            .anyRequest().permitAll()
        )
        .exceptionHandling(exception -> exception
            .accessDeniedHandler(accessDeniedHandler)
        )
        // 명시적으로 설정 안해줘도 자동으로 사용됨 (HttpSession을 통해 SecurityContext를 저장하고 복원)
        .securityContext(context ->
            context.securityContextRepository(new HttpSessionSecurityContextRepository()))
        .csrf(csrf -> csrf
            .csrfTokenRepository(repository)
            .ignoringRequestMatchers("/api/auth/logout"))
        .logout(logout -> logout
            .logoutRequestMatcher(new AntPathRequestMatcher("/api/auth/logout", "POST"))
            .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
            .addLogoutHandler(new SecurityContextLogoutHandler())
            .addLogoutHandler(new SessionRegistryLogoutHandler(sessionRegistry))
            .addLogoutHandler(rememberMeServices)
        )
        .with(new JsonUsernamePasswordAuthenticationFilter.Configurer(objectMapper),
            Customizer.withDefaults())
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
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }


  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider(
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
  public SessionAuthenticationStrategy sessionAuthenticationStrategy(
      SessionRegistry sessionRegistry) {
    return new RegisterSessionAuthenticationStrategy(sessionRegistry);
  }

  @Bean
  public RoleHierarchy roleHierarchy() {
    return RoleHierarchyImpl.withDefaultRolePrefix()
        .role(Role.ADMIN.name())
        .implies(Role.CHANNEL_MANAGER.name())
        .role(Role.CHANNEL_MANAGER.name())
        .implies(Role.USER.name())
        .build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
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
