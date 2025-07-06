package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.security.CustomLoginFailureHandler;
import com.sprint.mission.discodeit.security.CustomLoginSuccessHandler;
import com.sprint.mission.discodeit.security.JsonUsernamePasswordAuthenticationFilter;
import com.sprint.mission.discodeit.security.LogoutAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

  private final UserMapper userMapper;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public DaoAuthenticationProvider daoAuthenticationprovider(
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
    RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
    roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_CHANNEL_MANAGER > ROLE_USER");
    return roleHierarchy;
  }

  @Bean
  public DefaultWebSecurityExpressionHandler customWebSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
    DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
    expressionHandler.setRoleHierarchy(roleHierarchy);
    return expressionHandler;
  }

  @Bean
  public SecurityContextRepository securityContextRepository() {
    return new HttpSessionSecurityContextRepository();
  }

  @Bean
  public JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordAuthenticationFilter(
      AuthenticationManager authenticationManager) {

    JsonUsernamePasswordAuthenticationFilter filter = new JsonUsernamePasswordAuthenticationFilter();
    filter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/auth/login", "POST"));
    filter.setAuthenticationManager(authenticationManager);
    filter.setAuthenticationSuccessHandler(new CustomLoginSuccessHandler(userMapper));
    filter.setAuthenticationFailureHandler(new CustomLoginFailureHandler());
    filter.setSecurityContextRepository(securityContextRepository());

    return filter;
  }

  @Bean
  public LogoutAuthenticationFilter logoutAuthenticationFilter() {
    return new LogoutAuthenticationFilter();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordAuthenticationFilter,
      LogoutAuthenticationFilter logoutAuthenticationFilter,
      DefaultWebSecurityExpressionHandler customWebSecurityExpressionHandler) throws Exception {

    http
        .csrf(csrf -> csrf
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
            .ignoringRequestMatchers( "/api/auth/logout")
        )
        .httpBasic(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .maximumSessions(1)
            .maxSessionsPreventsLogin(false)
        )
        .logout(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(HttpMethod.PUT, "/api/auth/role").hasRole("ADMIN")
            .requestMatchers("/api/auth/csrf-token").permitAll()
            .requestMatchers("/api/auth/login").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
            .requestMatchers(request -> !request.getRequestURI().contains("/api/")).permitAll()
            .anyRequest().authenticated()
        )
        .addFilterBefore(jsonUsernamePasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(logoutAuthenticationFilter, LogoutFilter.class);

    SecurityFilterChain filterChain = http.build();

//    // 필터 목록 출력
//    System.out.println("=== Security Filter Chain ===");
//    filterChain.getFilters().forEach(filter ->
//        System.out.println(filter.getClass().getSimpleName())
//    );
//    System.out.println("==============================");

    return filterChain;
  }
}