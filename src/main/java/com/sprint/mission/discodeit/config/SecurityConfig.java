package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.CustomSessionInformationExpiredStrategy;
import com.sprint.mission.discodeit.security.DatabaseUserDetailsService;
import com.sprint.mission.discodeit.security.JsonUsernamePasswordAuthenticationFilter;
import com.sprint.mission.discodeit.security.Role;
import com.sprint.mission.discodeit.security.RoleIntegrityCheckFilter;
import com.sprint.mission.discodeit.security.SecurityMatchers;
import com.sprint.mission.discodeit.security.SessionInvalidateLogoutHandler;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(
      HttpSecurity http,
      DaoAuthenticationProvider daoAuthenticationProvider,
      SessionRegistry sessionRegistry,
      RoleIntegrityCheckFilter roleIntegrityCheckFilter,
      ObjectMapper objectMapper,
      PersistentTokenBasedRememberMeServices rememberMeServices)
      throws Exception {
    http
        .authenticationProvider(daoAuthenticationProvider)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                SecurityMatchers.NON_API,
                SecurityMatchers.CSRF_TOKEN,
                SecurityMatchers.SIGN_UP,
                SecurityMatchers.LOGIN
            ).permitAll()
            .anyRequest().hasRole(Role.USER.name())
        )
        .csrf(csrf ->
            csrf.ignoringRequestMatchers(SecurityMatchers.LOGOUT))
        .logout(logout -> logout
            .logoutRequestMatcher(SecurityMatchers.LOGOUT)
            .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
            .addLogoutHandler(new SessionInvalidateLogoutHandler(sessionRegistry))
        )
        .with(new JsonUsernamePasswordAuthenticationFilter.Configurer(objectMapper),
            Customizer.withDefaults())
        .addFilterAfter(roleIntegrityCheckFilter, JsonUsernamePasswordAuthenticationFilter.class)
        .rememberMe(rememberMe -> rememberMe.rememberMeServices(rememberMeServices))
        .sessionManagement(session -> session
            .sessionFixation().migrateSession()
            .maximumSessions(1)
            .maxSessionsPreventsLogin(false)
            .sessionRegistry(sessionRegistry)
            .expiredSessionStrategy(new CustomSessionInformationExpiredStrategy(objectMapper))
        )
    ;
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider(
      PasswordEncoder passwordEncoder,
      UserDetailsService userDetailsService,
      RoleHierarchy roleHierarchy
  ) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(passwordEncoder);
    provider.setUserDetailsService(userDetailsService);
    provider.setAuthoritiesMapper(new RoleHierarchyAuthoritiesMapper(roleHierarchy));
    return provider;
  }

  @Bean
  public UserDetailsService userDetailsService(UserRepository userRepository,
      UserMapper userMapper) {
    return new DatabaseUserDetailsService(userRepository, userMapper);
  }

  @Bean
  public RoleHierarchy roleHierarchy() {
    return RoleHierarchyImpl.withDefaultRolePrefix()
        .role(Role.ADMIN.name()).implies(Role.CHANNEL_MANAGER.name())
        .role(Role.CHANNEL_MANAGER.name()).implies(Role.USER.name())
        .build();
  }

  @Bean
  public RoleIntegrityCheckFilter roleIntegrityCheckFilter(UserRepository userRepository) {
    return new RoleIntegrityCheckFilter(userRepository);
  }

  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
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

    PersistentTokenBasedRememberMeServices rememberMeServices =
        new PersistentTokenBasedRememberMeServices(key, userDetailsService, tokenRepository);
    rememberMeServices.setTokenValiditySeconds(tokenValiditySeconds);

    return rememberMeServices;
  }
}
