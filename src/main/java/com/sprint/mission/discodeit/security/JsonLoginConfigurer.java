package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
public class JsonLoginConfigurer extends
    AbstractAuthenticationFilterConfigurer<HttpSecurity, JsonLoginConfigurer, JsonUsernamePasswordAuthenticationFilter> {

    private final ObjectMapper objectMapper;

    public JsonLoginConfigurer(ObjectMapper objectMapper) {
        super(new JsonUsernamePasswordAuthenticationFilter(objectMapper), "/api/auth/login");
        this.objectMapper = objectMapper;
    }

    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl, HttpMethod.POST.name());
    }

    @Override
    public void init(HttpSecurity http) throws Exception {
        loginProcessingUrl("/api/auth/login");
        successHandler(new JsonLoginSuccessHandler(objectMapper));
        failureHandler(new JsonLoginFailureHandler(objectMapper));
    }
}
