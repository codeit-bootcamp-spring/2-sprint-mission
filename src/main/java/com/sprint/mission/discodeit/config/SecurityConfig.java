package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.security.CustomSessionInformationExpiredStrategy;
import com.sprint.mission.discodeit.security.JsonUsernamePasswordAuthenticationFilter;
import com.sprint.mission.discodeit.security.SecurityMatchers;
import com.sprint.mission.discodeit.security.SessionRegistryLogoutHandler;
import java.util.stream.IntStream;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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

@Slf4j
@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity
public class SecurityConfig {

  // 이미 있는 필터 or 커스텀 필터를 활성화
  @Bean
  public SecurityFilterChain filterChain(
      HttpSecurity http,
      ObjectMapper objectMapper,
      DaoAuthenticationProvider daoAuthenticationProvider,
      SessionRegistry sessionRegistry,
      PersistentTokenBasedRememberMeServices rememberMeServices
  ) throws Exception {
    http
        .authenticationProvider(daoAuthenticationProvider)
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(
                // CSRF 토큰 발급 API 및 회원가입 API는 인증 없이 접근 허용
                SecurityMatchers.NON_API, // /api/** 제외한 모든 접근 허용
                SecurityMatchers.GET_CSRF_TOKEN, // GET / /api/auth/csrf-token
                SecurityMatchers.SIGN_UP, // POST / /api/users
                SecurityMatchers.LOG_IN //
            ).permitAll()
            .anyRequest().hasRole(Role.USER.name())
        )
        .csrf(csrf -> csrf.ignoringRequestMatchers(SecurityMatchers.LOG_OUT))
        .logout(logout ->
            logout
                .logoutRequestMatcher(SecurityMatchers.LOG_OUT)
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                .addLogoutHandler(new SessionRegistryLogoutHandler(sessionRegistry))
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
  public String debugFilterChain(@Qualifier("filterChain") SecurityFilterChain chain) {
    log.debug("Debug Filter Chain...");
    int filterSize = chain.getFilters().size();
    IntStream.range(0, filterSize)
        .forEach(idx -> {
          log.debug("[{}/{}] {}", idx + 1, filterSize, chain.getFilters().get(idx));
        });
    return "debugFilterChain";
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    // 회원 가입 시 비밀번호를 해시로 저장하기 위해 BCryptPasswordEncoder 사용
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider(
      UserDetailsService userDetailsService,
      PasswordEncoder passwordEncoder,
      RoleHierarchy roleHierarchy
  ) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder); // BCryptPasswordEncoder 를 통해 비밀번호 해시 설정
    provider.setAuthoritiesMapper(new RoleHierarchyAuthoritiesMapper(roleHierarchy));
    return provider;
  }

  @Bean
  public RoleHierarchy roleHierarchy() {
    return RoleHierarchyImpl.withDefaultRolePrefix()
        .role(Role.ADMIN.name())
        .implies(Role.USER.name(), Role.CHANNEL_MANAGER.name())
        .role(Role.CHANNEL_MANAGER.name())
        .implies(Role.USER.name())
        .build();
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
