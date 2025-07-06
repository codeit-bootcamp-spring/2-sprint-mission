package com.sprint.mission.discodeit.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.domain.auth.dto.LogInRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@RequiredArgsConstructor
public class JsonUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private static final String LOGIN_URL = "/api/auth/login";

  public static final RequestMatcher LOGIN = new AntPathRequestMatcher(
      "/api/auth/login", HttpMethod.POST.name());

  private final ObjectMapper objectMapper;

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {

    if (!request.getMethod().equals("POST")) {
      throw new AuthenticationServiceException(
          "Authentication method not supported: " + request.getMethod());
    }

    try {
      LogInRequest loginRequest = objectMapper.readValue(request.getInputStream(),
          LogInRequest.class);
      UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
          loginRequest.username(), loginRequest.password());

      setDetails(request, authRequest);
      return this.getAuthenticationManager().authenticate(authRequest);
    } catch (IOException e) {
      throw new AuthenticationServiceException("Request parsing failed", e);
    }
  }

  public static class Configurer extends
      AbstractAuthenticationFilterConfigurer<HttpSecurity, Configurer, JsonUsernamePasswordAuthenticationFilter> {

    private final ObjectMapper objectMapper;

    public Configurer(ObjectMapper objectMapper) {
      super(new JsonUsernamePasswordAuthenticationFilter(objectMapper), LOGIN_URL);
      this.objectMapper = objectMapper;
    }

    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
      return new AntPathRequestMatcher(loginProcessingUrl, HttpMethod.POST.name());
    }

    @Override
    public void init(HttpSecurity http) {
      loginProcessingUrl(LOGIN_URL);
      successHandler(new CustomLoginSuccessHandler(objectMapper));
      failureHandler(new CustomLoginFailureHandler(objectMapper));
    }
  }

  public static JsonUsernamePasswordAuthenticationFilter createDefault(
      ObjectMapper objectMapper,
      AuthenticationManager authenticationManager,
      SessionAuthenticationStrategy sessionAuthenticationStrategy
  ) {
    JsonUsernamePasswordAuthenticationFilter filter = new JsonUsernamePasswordAuthenticationFilter(
        objectMapper);
    filter.setRequiresAuthenticationRequestMatcher(LOGIN);
    filter.setAuthenticationManager(authenticationManager);
    filter.setAuthenticationSuccessHandler(new CustomLoginSuccessHandler(objectMapper));
    filter.setAuthenticationFailureHandler(new CustomLoginFailureHandler(objectMapper));
    filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());
    filter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy);
    return filter;
  }

}