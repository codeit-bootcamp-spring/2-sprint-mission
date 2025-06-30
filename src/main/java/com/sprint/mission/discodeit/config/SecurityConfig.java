package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.security.CustomLoginFailureHandler;
import com.sprint.mission.discodeit.security.CustomLoginSuccessHandler;
import com.sprint.mission.discodeit.security.CustomLogoutFilter;
import com.sprint.mission.discodeit.security.JsonUsernamePasswordAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
        HttpSecurity http,
        JsonUsernamePasswordAuthenticationFilter jsonLoginFilter,
        CustomLogoutFilter customLogoutFilter
    ) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                    "/swagger-ui/**", "/v3/api-docs/**",
                    "/actuator/**",
                    "/", "/favicon.ico", "/index.html",
                    "/static/**", "/assets/**", "/profile-images/**",
                    "/api/auth/csrf-token", "/api/auth/login",
                    "/api/users"
                ).permitAll()
                .requestMatchers("/api/auth/role").hasRole("ADMIN") // 권한 수정은 관리자만 가능
                .requestMatchers(
                    "/api/channels/public",
                    "/api/channels/public/**"
                ).hasRole("CHANNEL_MANAGER") // 퍼블릭 채널 생성/수정/삭제는 채널 매니저 이상만 가능
                .requestMatchers("/api/**").hasRole("USER") // 그 외 API는 최소 ROLE_USER 이상
                .anyRequest().permitAll()
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(
                    "/api/auth/login",
                    "/api/auth/logout",
                    "/api/auth/role"
                ) // CSRF 무시
            )
            .addFilterBefore(customLogoutFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jsonLoginFilter, UsernamePasswordAuthenticationFilter.class)
            .formLogin(form -> form.disable())
            .logout(logout -> logout.disable()); // LogoutFilter 제외

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
        PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        // DaoAuthenticationProvider가 사용할 UserDetailsService를 지정
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
        DaoAuthenticationProvider provider) throws Exception {

        return http.getSharedObject(AuthenticationManagerBuilder.class)
            .authenticationProvider(provider) // DaoAuthenticationProvider 등록
            .build(); // AuthenticationManager 생성
    }

    @Bean
    public JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordAuthenticationFilter(
        AuthenticationManager authenticationManager,
        ObjectMapper objectMapper,
        UserMapper userMapper
    ) {
        JsonUsernamePasswordAuthenticationFilter filter =
            new JsonUsernamePasswordAuthenticationFilter(objectMapper);

        filter.setAuthenticationManager(authenticationManager);
        filter.setFilterProcessesUrl("/api/auth/login");
        filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());

        filter.setAuthenticationSuccessHandler(
            new CustomLoginSuccessHandler(objectMapper, userMapper));
        filter.setAuthenticationFailureHandler(new CustomLoginFailureHandler(objectMapper));

        return filter;
    }

    @Bean
    public CustomLogoutFilter customLogoutFilter() {
        return new CustomLogoutFilter();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("""
                ROLE_ADMIN > ROLE_CHANNEL_MANAGER
                ROLE_CHANNEL_MANAGER > ROLE_USER
            """);
        return hierarchy;
    }
}
