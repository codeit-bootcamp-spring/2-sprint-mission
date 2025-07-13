package com.sprint.mission.discodeit.security.config;

import static com.sprint.mission.discodeit.security.config.SecurityMatchers.GET_CSRF_TOKEN;
import static com.sprint.mission.discodeit.security.config.SecurityMatchers.LOGOUT;
import static com.sprint.mission.discodeit.security.config.SecurityMatchers.NON_API;
import static com.sprint.mission.discodeit.security.config.SecurityMatchers.PUBLIC_CHANNEL_ACCESS;
import static com.sprint.mission.discodeit.security.config.SecurityMatchers.ROLE_UPDATE;
import static com.sprint.mission.discodeit.security.config.SecurityMatchers.SIGN_UP;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.domain.user.entity.Role;
import com.sprint.mission.discodeit.security.filter.CustomSessionInformationExpiredStrategy;
import com.sprint.mission.discodeit.security.filter.JsonUsernamePasswordAuthenticationFilter;
import com.sprint.mission.discodeit.security.filter.JsonUsernamePasswordAuthenticationFilter.Configurer;
import com.sprint.mission.discodeit.security.filter.SessionRegistryLogoutHandler;
import com.sprint.mission.discodeit.security.jwt.JwtAuthenticationFilter;
import com.sprint.mission.discodeit.security.jwt.service.JwtService;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain configure(
      HttpSecurity httpSecurity,
      ObjectMapper objectMapper,
      DaoAuthenticationProvider daoAuthenticationProvider,
      SessionRegistry sessionRegistry,
      PersistentTokenBasedRememberMeServices rememberMeServices,
      JwtService jwtService,
      JwtAuthenticationFilter jwtAuthenticationFilter
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
        .with(new Configurer(objectMapper, jwtService), Customizer.withDefaults())
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
        .csrf(csrf -> csrf
            .ignoringRequestMatchers(LOGOUT)
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
        )
        .rememberMe(rememberMe -> rememberMe.rememberMeServices(rememberMeServices))
        .addFilterBefore(jwtAuthenticationFilter, JsonUsernamePasswordAuthenticationFilter.class);

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
