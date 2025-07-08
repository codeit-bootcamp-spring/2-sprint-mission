package com.sprint.mission.discodeit.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserLoginCommand;
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
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@RequiredArgsConstructor
public class JsonUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final ObjectMapper objectMapper;

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {

    if (!request.getMethod().equals("POST")) {
      throw new AuthenticationServiceException(
          "Authentication method not supported: " + request.getMethod());
    }

    try {
      UserLoginCommand loginRequest = objectMapper.readValue(request.getInputStream(),
          UserLoginCommand.class);

      UsernamePasswordAuthenticationToken authRequest =
          new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());

      setDetails(request, authRequest);
      return this.getAuthenticationManager().authenticate(authRequest);

    } catch (IOException e) {
      throw new AuthenticationServiceException("Request parsing failed", e);
    }
  }

  public static JsonUsernamePasswordAuthenticationFilter createDefault(
      ObjectMapper objectMapper,
      AuthenticationManager authenticationManager,
      SessionAuthenticationStrategy sessionAuthenticationStrategy,
      RememberMeServices rememberMeServices
  ) {
    JsonUsernamePasswordAuthenticationFilter filter = new JsonUsernamePasswordAuthenticationFilter(
        objectMapper);
    filter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(
        "/api/auth/login", HttpMethod.POST.name()));
    filter.setAuthenticationManager(authenticationManager);
    filter.setAuthenticationSuccessHandler(new CustomLoginSuccessHandler(objectMapper));
    filter.setAuthenticationFailureHandler(new CustomLoginFailureHandler(objectMapper));
    filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());
    filter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy);
    filter.setRememberMeServices(rememberMeServices);
    return filter;
  }

  public static class Configurer extends
      AbstractAuthenticationFilterConfigurer<HttpSecurity, Configurer, JsonUsernamePasswordAuthenticationFilter> {

    private final ObjectMapper objectMapper;

    public Configurer(ObjectMapper objectMapper) {
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
      successHandler(new CustomLoginSuccessHandler(objectMapper));
      failureHandler(new CustomLoginFailureHandler(objectMapper));
    }
  }
}
