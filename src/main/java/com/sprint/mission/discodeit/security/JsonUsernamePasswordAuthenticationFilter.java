package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Slf4j
@RequiredArgsConstructor
public class JsonUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final ObjectMapper objectMapper;

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    log.debug("[JSON LOGIN FILTER] 진입: method={}, path={}",
        request.getMethod(), request.getRequestURI());
    if (!request.getMethod().equals("POST")) {
      throw new AuthenticationServiceException(
          "Authentication method not supported: " + request.getMethod());
    }

    try {
      LoginRequest loginRequest =
          objectMapper.readValue(request.getInputStream(), LoginRequest.class);

      UsernamePasswordAuthenticationToken authRequest =
          new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());

      setDetails(request, authRequest);

      return this.getAuthenticationManager().authenticate(authRequest);
    } catch (Exception e) {
      throw new AuthenticationServiceException("Authentication failed: " + e.getMessage());
    }
  }

  public static JsonUsernamePasswordAuthenticationFilter createDefault(
      ObjectMapper objectMapper,
      AuthenticationManager authenticationManager,
      SessionAuthenticationStrategy sessionAuthenticationStrategy,
      JwtService jwtService
  ) {
    JsonUsernamePasswordAuthenticationFilter filter = new JsonUsernamePasswordAuthenticationFilter(
        objectMapper);
    filter.setRequiresAuthenticationRequestMatcher(SecurityMatchers.LOGIN);
    filter.setAuthenticationManager(authenticationManager);
    filter.setAuthenticationSuccessHandler(new CustomLoginSuccessHandler(objectMapper, jwtService));
    filter.setAuthenticationFailureHandler(new CustomLoginFailureHandler(objectMapper));
    filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());
    filter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy);

    return filter;
  }

  public static class Configurer extends
      AbstractAuthenticationFilterConfigurer<HttpSecurity, Configurer, JsonUsernamePasswordAuthenticationFilter> {

    private final ObjectMapper objectMapper;
    private final JwtService jwtService;

    public Configurer(ObjectMapper objectMapper, JwtService jwtService) {
      super(new JsonUsernamePasswordAuthenticationFilter(objectMapper), SecurityMatchers.LOGIN_URL);
      this.objectMapper = objectMapper;
      this.jwtService = jwtService;
    }

    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
      return new AntPathRequestMatcher(loginProcessingUrl, HttpMethod.POST.name());
    }

    @Override
    public void init(HttpSecurity http) throws Exception {
      loginProcessingUrl(SecurityMatchers.LOGIN_URL);
      successHandler(new CustomLoginSuccessHandler(objectMapper, jwtService));
      failureHandler(new CustomLoginFailureHandler(objectMapper));
    }
  }
}
