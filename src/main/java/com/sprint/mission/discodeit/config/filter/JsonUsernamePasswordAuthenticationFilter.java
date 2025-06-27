package com.sprint.mission.discodeit.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

public class JsonUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
      new AntPathRequestMatcher("/api/auth/login", "POST");
  private final ObjectMapper objectMapper;

  public JsonUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper) {
    super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
    this.objectMapper = objectMapper;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException, IOException {

    if (!request.getMethod().equals("POST")) {
      throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
    }

    LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);

    String username = loginRequest.username();
    String password = loginRequest.password();

    if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
      throw new AuthenticationServiceException("Username or Password not provided");
    }

    UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);

    return this.getAuthenticationManager().authenticate(authRequest);
  }
}