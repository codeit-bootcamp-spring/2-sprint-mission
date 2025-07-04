package com.sprint.mission.discodeit.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.exception.auth.CustomAccessDeniedHandler;
import com.sprint.mission.discodeit.security.JsonUsernamePasswordAuthenticationFilter;
import com.sprint.mission.discodeit.security.jwt.JwtService;
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
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@Profile("!test")
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, ObjectMapper objectMapper,
      CustomAccessDeniedHandler accessDeniedHandler,
      JwtService jwtService,
      PersistentTokenBasedRememberMeServices rememberMeServices)
      throws Exception {

    // CSRF 토큰은 원래 세션 저장소 (spring_session_attributes의 attributes_bytes에 저장됨)
    // 요청 -> 헤더에 있는 CSRF 토큰을 꺼내 Session 저장소를 확인해서 검증
    // JWT는 Session을 사용하지 않음 (stateless) -> 토큰 검증이 세션에서 불가능 하므로,
    // CSRF 토큰을 쿠키에 저장 -> 클라이언트는 이 쿠키에서 CSRF 토큰 값을 읽어 요청 헤더에 포함시켜 서버에 전송
    // 쿠키는 브라우저가 자동으로 넣지만, 헤더는 JS로 명시적으로 넣어야만 전송됨
    // 서버는 요청 헤더에 포함된 CSRF 토큰 값과 쿠키에 저장된 CSRF 토큰을 비교하여 검증
    // 공격자는 쿠키에 있는 CSRF 토큰 값을 볼 수는 있지만, 헤더에 이 토큰 값을 넣어서 요청을 보낼 수는 없음 (헤더에 토큰 값을 넣는 것은 정상적인 JS 코드(정상 웹사이트)에서만 가능)
    CookieCsrfTokenRepository repository =
        CookieCsrfTokenRepository.withHttpOnlyFalse(); // 프론트엔드(JS)에서 토큰을 헤더에 포함할 수 있도록 함. (따로 띄우지 않았으므로 CORS는 필요 X)
    repository.setCookieName("XSRF-TOKEN");
    repository.setHeaderName("X-XSRF-TOKEN");

    // Spring Security 6.x의 기본 CSRF 처리 방식은 HTML 폼 기반 요청에 최적화
    // 기본 핸들러(XorCsrfTokenRequestAttributeHandler)는 주로 폼 데이터에서 토큰을 추출하고, JSON 요청(예: Content-Type: application/json)에서는 CSRF 토큰을 제대로 검증하지 못하는 경우가 많음
    // 이 때문에, 프론트엔드에서 JSON으로 로그인이나 기타 POST 요청을 보낼 때 CSRF 토큰이 있어도 403 Forbidden이 발생할 수 있다.
    // 핸들러를 명시적으로 등록하면, 헤더(X-XSRF-TOKEN 등)나 파라미터에서 CSRF 토큰을 추출해 JSON 요청도 정상적으로 검증할 수 있다.
    CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
    requestHandler.setCsrfRequestAttributeName("_csrf");

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
            .csrfTokenRequestHandler(requestHandler)
            .csrfTokenRepository(repository)
            .ignoringRequestMatchers("/api/auth/logout"))
        .sessionManagement(session ->
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .logout(logout -> logout
            .logoutRequestMatcher(new AntPathRequestMatcher("/api/auth/logout", "POST"))
            .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
            .addLogoutHandler(new SecurityContextLogoutHandler())
            .addLogoutHandler(rememberMeServices)
        )
        .with(new JsonUsernamePasswordAuthenticationFilter.Configurer(objectMapper, jwtService),
            Customizer.withDefaults())
        .rememberMe(rememberMe -> rememberMe.rememberMeServices(rememberMeServices));
    return http.build();
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
