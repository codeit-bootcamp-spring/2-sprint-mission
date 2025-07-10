package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@RequiredArgsConstructor
public class DiscodeitUsernamePasswordAuthenticationFilter extends
    UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;

    @Override
    public Authentication attemptAuthentication(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        try {
            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);

            UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());

            setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);

        } catch (Exception e) {
            throw new AuthenticationServiceException("login request failed", e);
        }

    }

    public static DiscodeitUsernamePasswordAuthenticationFilter createDefault(
        ObjectMapper objectMapper,
        AuthenticationManager authenticationManager,
        SessionAuthenticationStrategy sessionAuthenticationStrategy,
        RememberMeServices rememberMeServices
    ) {
        DiscodeitUsernamePasswordAuthenticationFilter filter =
            new DiscodeitUsernamePasswordAuthenticationFilter(objectMapper);
        filter.setFilterProcessesUrl("/api/auth/login");
        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationSuccessHandler(new CustomLoginSuccessHandler(objectMapper));
        filter.setAuthenticationFailureHandler(new CustomLoginFailureHandler(objectMapper));
        filter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy);
        filter.setRememberMeServices(rememberMeServices);
        return filter;
    }

    public static class Configurer extends
        AbstractAuthenticationFilterConfigurer<HttpSecurity, Configurer, DiscodeitUsernamePasswordAuthenticationFilter> {

        private final ObjectMapper objectMapper;

        public Configurer(ObjectMapper objectMapper) {
            super(new DiscodeitUsernamePasswordAuthenticationFilter(objectMapper), "/api/auth/login");
            this.objectMapper = objectMapper;
        }

        @Override
        protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
            return new AntPathRequestMatcher(loginProcessingUrl, HttpMethod.POST.name());
        }

        @Override
        public void init(HttpSecurity http) throws Exception {
            loginProcessingUrl("/api/auth/login");
        }
    }
}
