package com.sprint.mission.discodeit.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.security.filter.CustomSessionInformationExpiredStrategy;
import com.sprint.mission.discodeit.security.filter.JsonUsernamePasswordAuthenticationFilter;
import com.sprint.mission.discodeit.security.filter.SessionRegistryLogoutHandler;
import com.sprint.mission.discodeit.domain.user.entity.Role;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  private static final RequestMatcher NON_API = new NegatedRequestMatcher(
      new AntPathRequestMatcher("/api/**"));
  private static final RequestMatcher GET_CSRF_TOKEN = new AntPathRequestMatcher(
      "/api/auth/csrf-token");
  private static final RequestMatcher SIGN_UP = new AntPathRequestMatcher("/api/users",
      HttpMethod.POST.name());
  public static final RequestMatcher LOGOUT = new AntPathRequestMatcher("/api/auth/logout",
      HttpMethod.POST.name());
  public static final RequestMatcher PUBLIC_CHANNEL_ACCESS = new AntPathRequestMatcher(
      "/api/channels/public/**");
  public static final RequestMatcher ROLE_UPDATE = new AntPathRequestMatcher(
      "/api/auth/role");

  @Bean
  public SecurityFilterChain configure(
      HttpSecurity httpSecurity,
      ObjectMapper objectMapper,
      DaoAuthenticationProvider daoAuthenticationProvider,
      SessionRegistry sessionRegistry,
      PersistentTokenBasedRememberMeServices rememberMeServices
  ) throws Exception {
    httpSecurity
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                NON_API,
                GET_CSRF_TOKEN,
                SIGN_UP
            ).permitAll()
            .requestMatchers(ROLE_UPDATE).hasRole(Role.ADMIN.name())
            .requestMatchers(PUBLIC_CHANNEL_ACCESS).hasRole(Role.CHANNEL_MANAGER.name())
            .anyRequest().hasRole(Role.USER.name())
        )
        .with(new JsonUsernamePasswordAuthenticationFilter.Configurer(objectMapper),
            Customizer.withDefaults())
        .authenticationProvider(daoAuthenticationProvider)
        .logout(logout -> logout
            .logoutRequestMatcher(LOGOUT)
            .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
            .addLogoutHandler(new SessionRegistryLogoutHandler(sessionRegistry))
            .addLogoutHandler(rememberMeServices)
        )
        .sessionManagement(session ->
            session
                .sessionFixation().migrateSession()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .sessionRegistry(sessionRegistry)
                .expiredSessionStrategy(new CustomSessionInformationExpiredStrategy(objectMapper))
        )
        .csrf(csrf -> csrf.ignoringRequestMatchers(LOGOUT))
        .rememberMe(rememberMe -> rememberMe.rememberMeServices(rememberMeServices));

    return httpSecurity.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider(
      UserDetailsService userDetailsService,
      PasswordEncoder passwordEncoder
  ) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder);
    return provider;
  }

  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
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
