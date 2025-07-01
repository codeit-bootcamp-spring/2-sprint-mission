package com.sprint.mission.discodeit.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.core.user.entity.Role;
import com.sprint.mission.discodeit.security.SecurityMatchers;
import com.sprint.mission.discodeit.security.filter.JsonUsernamePasswordAuthenticationFilter.Configurer;
import com.sprint.mission.discodeit.security.handler.SessionRegistryLogoutHandler;
import jakarta.servlet.http.HttpServletResponse;
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

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(
      HttpSecurity http,
      ObjectMapper objectMapper,
      SessionRegistry sessionRegistry,
      DaoAuthenticationProvider daoAuthenticationProvider) throws Exception {
    http
        .authenticationProvider(daoAuthenticationProvider)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                SecurityMatchers.NON_API,
                SecurityMatchers.LOGIN,
                SecurityMatchers.GET_CSRF_TOKEN,
                SecurityMatchers.SIGN_UP
            ).permitAll()
            .requestMatchers(
                SecurityMatchers.CREATE_CHANNEL,
                SecurityMatchers.UPDATE_CHANNEL,
                SecurityMatchers.DELETE_CHANNEL
            ).hasRole("CHANNEL_MANAGER")
            .requestMatchers(SecurityMatchers.UPDATE_ROLE).hasRole("ADMIN")
            .anyRequest().hasRole("USER"))
        .csrf(csrf -> csrf.ignoringRequestMatchers(SecurityMatchers.LOGOUT))
        .logout(logout -> logout
            .logoutRequestMatcher(SecurityMatchers.LOGOUT)
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
            .logoutSuccessHandler((request, response, authentication) -> response.setStatus(
                HttpServletResponse.SC_OK)
            ))
        .with(new Configurer(objectMapper),
            Customizer.withDefaults())
        .sessionManagement(session -> session
            .sessionFixation().migrateSession()
            .maximumSessions(1)
            .maxSessionsPreventsLogin(false)
            .sessionRegistry(sessionRegistry));
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
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
    String hierarchy = "ROLE_ADMIN > ROLE_CHANNEL_MANAGE\n" + "ROLE_CHANNEL_MANAGE > ROLE_USER";
    return RoleHierarchyImpl.fromHierarchy(hierarchy);
  }
}
