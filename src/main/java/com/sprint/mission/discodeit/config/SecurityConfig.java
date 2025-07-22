package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.security.filter.CustomCsrfDebugFilter;
import com.sprint.mission.discodeit.security.filter.JsonLogoutFilter;
import com.sprint.mission.discodeit.security.filter.JsonUsernamePasswordAuthenticationFilter;
import com.sprint.mission.discodeit.security.LoginFailureHandler;
import com.sprint.mission.discodeit.security.LoginSuccessHandler;
import com.sprint.mission.discodeit.security.filter.JwtAuthenticationFilter;
import com.sprint.mission.discodeit.security.filter.UserActivityFilter;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.service.UserActivityService;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer.SessionFixationConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    @Bean
    public JsonLogoutFilter jsonLogoutFilter(JwtService jwtService) {
        return new JsonLogoutFilter(jwtService);
    }

    @Bean
    public JsonUsernamePasswordAuthenticationFilter jsonLoginFilter(
        ObjectMapper objectMapper,
        UserMapper userMapper,
        AuthenticationManager authenticationManager,
        CsrfTokenRepository csrfTokenRepository,
        JwtService jwtService,
        LoginSuccessHandler loginSuccessHandler
    ) {
        // 로그인 요청 파싱해서 커스텀 필터 객체 생성
        JsonUsernamePasswordAuthenticationFilter loginFilter =
            new JsonUsernamePasswordAuthenticationFilter(objectMapper);
        // 인증을 위임할 매니저 생성
        loginFilter.setAuthenticationManager(authenticationManager);
        // 인증 성공시 SecurityContext를 저장할 장소 지정
        loginFilter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());
        // 인증 성공/실패 핸들러 지정
        loginFilter.setAuthenticationSuccessHandler(loginSuccessHandler);
        loginFilter.setAuthenticationFailureHandler(new LoginFailureHandler(objectMapper));
        // 필터 처리 경로 설정
        loginFilter.setFilterProcessesUrl("/api/auth/login");
        return loginFilter;
    }

    // 권한 계층 설정
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_ADMIN > ROLE_CHANNEL_MANAGER \nROLE_CHANNEL_MANAGER > ROLE_USER");
        return hierarchy;
    }


    /**
     * Spring Security 필터 체인 설정
     */
    @Bean
    public SecurityFilterChain filterChain(
        HttpSecurity http,
        JsonLogoutFilter logoutFilter,
        JsonUsernamePasswordAuthenticationFilter loginFilter,
            UserActivityFilter userActivityFilter,
        JwtAuthenticationFilter jwtAuthenticationFilter

    ) throws Exception {

        CsrfTokenRequestAttributeHandler handler = new CsrfTokenRequestAttributeHandler();
        handler.setCsrfRequestAttributeName("_csrf");


        http
            .securityContext(context -> context
                .securityContextRepository(new HttpSessionSecurityContextRepository())
            )
            .csrf(csrf -> csrf
                .csrfTokenRepository(csrfTokenRepository())
                    .csrfTokenRequestHandler(handler)
                .ignoringRequestMatchers(
                    "/api/auth/login",
                    "/api/users", // 회원가입
                    "/api/auth/csrf-token",
                    "/api/auth/logout"
                )
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/auth/login",
                    "/api/auth/csrf-token",
                    "/api/users",
                    "/api/auth/logout",
                    "/actuator/**"
                ).permitAll()

                // 사용자 권한 수정은 ROLE_ADMIN만
                .requestMatchers("/api/auth/role")
                .hasRole("ADMIN")

                // 퍼블릭 채널 관리는 ROLE_CHANNEL_MANAGER부터
                .requestMatchers(HttpMethod.POST, "/api/channels/public")
                .hasRole("CHANNEL_MANAGER")
                // 퍼블릭 채널 수정
                .requestMatchers(HttpMethod.PATCH, "/api/channels/*")
                .hasRole("CHANNEL_MANAGER")
                // 퍼블릭 채널 삭제(경로를 프라이빗 채널과 공유하고 있어서 서비스 내부에서 isPublic 여부에 따라 체크)
                .requestMatchers(HttpMethod.DELETE, "/api/channels/*")
                .hasRole("CHANNEL_MANAGER")
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
            )
            .sessionManagement(session -> {
                session
                    .sessionFixation(SessionFixationConfigurer::migrateSession); // 세션 고정 보호
                session
                    .maximumSessions(1) // 최대 세션 1개로 고정.
                    .sessionRegistry(sessionRegistry()); // 동시 세션 추적
            })
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            // login -> remember-me -> logout
            .addFilterBefore(jwtAuthenticationFilter, JsonUsernamePasswordAuthenticationFilter.class)
            .addFilterAt(loginFilter, JsonUsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(logoutFilter, RememberMeAuthenticationFilter.class)
            .addFilterAfter(userActivityFilter, RememberMeAuthenticationFilter.class);

        return http.build();
    }

    // csrf 토큰
    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        CookieCsrfTokenRepository repository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        repository.setCookiePath("/");
        repository.setCookieName("XSRF-TOKEN");
        repository.setHeaderName("X-XSRF-Token");
        return repository;
    }

    // 비밀번호 인코더
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 인증 매니저
    @Bean
    public AuthenticationManager authenticationManager(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }


    @Bean
    public CustomCsrfDebugFilter customCsrfDebugFilter(CsrfTokenRepository csrfTokenRepository) {
        return new CustomCsrfDebugFilter(csrfTokenRepository);
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(DataSource dataSource) {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        // 테이블 자동 생성(첫 실행 시 활성화) -> 실제 배포에서는 테이블 수동 설정
//        tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }

    // 커스텀 필터 내부에서 PersistentTokenBasedRememberMeServices를 사용하기 위한 Bean 등록
    @Bean
    public PersistentTokenBasedRememberMeServices rememberMeServices(
        PersistentTokenRepository tokenRepository,
        UserDetailsService userDetailsService
    ) {
        PersistentTokenBasedRememberMeServices services =
            new PersistentTokenBasedRememberMeServices(
                "remember-me", // key
                userDetailsService,
                tokenRepository
            );
        services.setCookieName("remember-me-cookie");
        services.setParameter("remember-me");
        services.setTokenValiditySeconds(60 * 60 * 24 * 21); // 21일
        return services;
    }


    @Bean
    public RememberMeAuthenticationFilter rememberMeAuthenticationFilter(
        AuthenticationManager authenticationManager,
        RememberMeServices rememberMeServices
    ) {
        return new RememberMeAuthenticationFilter(authenticationManager, rememberMeServices);
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler(
            UserMapper userMapper,
            CsrfTokenRepository csrfTokenRepository,
            JwtService jwtService,
            ObjectMapper objectMapper,
            @Value("${jwt.refresh-token-expiry}") long refreshTokenExpiryMillis
    ) {
        return new LoginSuccessHandler(userMapper, csrfTokenRepository, jwtService, objectMapper, refreshTokenExpiryMillis);
    }

    @Bean
    public UserActivityFilter userActivityFilter(UserActivityService userActivityService) {
        return new UserActivityFilter(userActivityService);
    }
}


