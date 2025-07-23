package com.sprint.mission.discodeit.security.config;

import static com.sprint.mission.discodeit.security.config.SecurityMatchers.GET_CSRF_TOKEN;
import static com.sprint.mission.discodeit.security.config.SecurityMatchers.LOGOUT;
import static com.sprint.mission.discodeit.security.config.SecurityMatchers.MESSAGE_DELETE;
import static com.sprint.mission.discodeit.security.config.SecurityMatchers.MESSAGE_UPDATE;
import static com.sprint.mission.discodeit.security.config.SecurityMatchers.NON_API;
import static com.sprint.mission.discodeit.security.config.SecurityMatchers.PUBLIC_CHANNEL_ACCESS;
import static com.sprint.mission.discodeit.security.config.SecurityMatchers.READ_STATUS_CREATE;
import static com.sprint.mission.discodeit.security.config.SecurityMatchers.READ_STATUS_UPDATE;
import static com.sprint.mission.discodeit.security.config.SecurityMatchers.ROLE_UPDATE;
import static com.sprint.mission.discodeit.security.config.SecurityMatchers.SIGN_UP;
import static com.sprint.mission.discodeit.security.config.SecurityMatchers.USER_DELETE;
import static com.sprint.mission.discodeit.security.config.SecurityMatchers.USER_UPDATE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.domain.user.entity.Role;
import com.sprint.mission.discodeit.security.filter.JsonUsernamePasswordAuthenticationFilter;
import com.sprint.mission.discodeit.security.filter.JsonUsernamePasswordAuthenticationFilter.Configurer;
import com.sprint.mission.discodeit.security.filter.TokenLogoutHandler;
import com.sprint.mission.discodeit.security.jwt.JwtAuthenticationFilter;
import com.sprint.mission.discodeit.security.jwt.accessmanager.MessageDeleteAuthorizationManager;
import com.sprint.mission.discodeit.security.jwt.accessmanager.MessageOwnerAuthorizationManager;
import com.sprint.mission.discodeit.security.jwt.accessmanager.ReadStatusSelfAuthorizationManager;
import com.sprint.mission.discodeit.security.jwt.accessmanager.UserSelfAuthorizationManager;
import com.sprint.mission.discodeit.security.JwtService;
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
      JwtService jwtService,
      JwtAuthenticationFilter jwtAuthenticationFilter,
      UserSelfAuthorizationManager userSelfAuthorizationManager,
      MessageOwnerAuthorizationManager messageOwnerAuthorizationManager,
      MessageDeleteAuthorizationManager messageDeleteAuthorizationManager,
      ReadStatusSelfAuthorizationManager readStatusSelfAuthorizationManager
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
            .requestMatchers(USER_DELETE, USER_UPDATE).access(userSelfAuthorizationManager)
            .requestMatchers(MESSAGE_UPDATE).access(messageOwnerAuthorizationManager)
            .requestMatchers(MESSAGE_DELETE).access(messageDeleteAuthorizationManager)
            .requestMatchers(READ_STATUS_CREATE, READ_STATUS_UPDATE).access(readStatusSelfAuthorizationManager)
            .anyRequest().hasRole(Role.USER.name())
        )
        .with(new Configurer(objectMapper, jwtService), Customizer.withDefaults())
        .authenticationProvider(daoAuthenticationProvider)
        .logout(logout -> logout
            .logoutRequestMatcher(LOGOUT)
            .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
            .addLogoutHandler(new TokenLogoutHandler(jwtService))
        )
        .csrf(csrf -> csrf
            .ignoringRequestMatchers(LOGOUT)
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
        )
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
  public RoleHierarchy roleHierarchy() {
    return RoleHierarchyImpl.withDefaultRolePrefix()
        .role(Role.ADMIN.name())
        .implies(Role.CHANNEL_MANAGER.name())
        .role(Role.CHANNEL_MANAGER.name())
        .implies(Role.USER.name())
        .build();
  }

}
