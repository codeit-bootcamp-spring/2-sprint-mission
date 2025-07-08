package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.filter.LoginFilter;
import com.sprint.mission.discodeit.service.basic.CsrfTokenDbService;
import com.sprint.mission.discodeit.service.basic.CsrfTokenLogout;
import com.sprint.mission.discodeit.service.basic.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity()
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final CsrfTokenDbService csrfTokenDbService;
    private final SessionRegistry sessionRegistry;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
        AuthenticationManager authenticationManager, CsrfTokenLogout csrfTokenLogout)
        throws Exception {

        LoginFilter loginFilter = new LoginFilter(authenticationManager);

        CompositeSessionAuthenticationStrategy sessionAuthStrategy = new CompositeSessionAuthenticationStrategy(
            Arrays.asList(
                new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry),
                new SessionFixationProtectionStrategy(),
                new RegisterSessionAuthenticationStrategy(sessionRegistry)
            )
        );
        loginFilter.setSessionAuthenticationStrategy(sessionAuthStrategy);

        loginFilter.setAuthenticationSuccessHandler((request, response, authentication) -> {
            String rememberMe = request.getParameter("remember-me");
            boolean isRememberMe = "true".equals(rememberMe);

            try {
                String newSessionId = request.getSession().getId();

                var newToken = csrfTokenDbService.generateToken(request);
                csrfTokenDbService.saveToken(newToken, request, response);
                log.info("🔄 CSRF 토큰 마이그레이션 완료 - 새 세션: {}", newSessionId);
            } catch (Exception e) {
                log.error("🔄 CSRF 토큰 마이그레이션 실패: {}", e.getMessage());
            }

            response.setStatus(HttpStatus.OK.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(
                "{\"success\": true, \"message\": \"Login successful\", \"rememberMe\": "
                    + isRememberMe + ", \"csrfTokenRefreshRequired\": true}");
            response.getWriter().flush();
        });

        loginFilter.setAuthenticationFailureHandler((request, response, exception) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"success\": false, \"message\": \"Login failed\"}");
            response.getWriter().flush();
        });

        return http
            .addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class)

            .securityContext(securityContext -> securityContext
                .requireExplicitSave(false))

            .csrf(csrf -> csrf.csrfTokenRepository(csrfTokenDbService)
                .ignoringRequestMatchers(SecurityMachers.CSRF_TOKEN, SecurityMachers.LOGOUT,
                    SecurityMachers.SIGN_UP, SecurityMachers.LOGIN, SecurityMachers.USERS_LIST,
                    SecurityMachers.CHANNELS_LIST, SecurityMachers.AUTH_ME)
                .ignoringRequestMatchers("/api/auth/login", "/api/users", "/api/users/**",
                    "/api/channels/public", "/api/channels/private", "/api/messages/**",
                    "/api/binaryContents/**"))

            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/error", "/error/**", "/favicon.ico").permitAll()
                .requestMatchers(SecurityMachers.NON_API).permitAll() // API가 아닌 경로 허용
                .requestMatchers(SecurityMachers.CSRF_TOKEN, SecurityMachers.SIGN_UP,
                    SecurityMachers.USERS_LIST, SecurityMachers.CHANNELS_LIST,
                    SecurityMachers.AUTH_ME).permitAll()
                .requestMatchers("/api/auth/login", "/api/users", "/api/channels/public")
                .permitAll() // 로그인, 회원가입, 공개 채널 목록 조회 허용
                .anyRequest().authenticated())

            .sessionManagement(session -> session
                .sessionFixation().migrateSession()
                .invalidSessionUrl("/login")
                .sessionCreationPolicy(
                    SessionCreationPolicy.IF_REQUIRED)
                .maximumSessions(1) //  1개만
                .maxSessionsPreventsLogin(false)
                .sessionRegistry(sessionRegistry))

            .rememberMe(remember -> remember
                .key("uniqueAndSecret")
                .tokenValiditySeconds(3 * 60 * 60) // 3시간 기본값
                .userDetailsService(customUserDetailsService))

            .logout(logout -> logout
                .logoutUrl("/api/auth/logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("XSRF-TOKEN", "_csrf", "JSESSIONID")
                .addLogoutHandler(csrfTokenLogout)
                .logoutSuccessHandler(
                    (req, res, auth) -> res.setStatus(HttpStatus.NO_CONTENT.value())))

            .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
        throws Exception {
        return config.getAuthenticationManager();
    }
}
