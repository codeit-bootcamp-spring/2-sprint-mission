package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final ObjectMapper objectMapper = new ObjectMapper();

  public LoginAuthenticationFilter(AuthenticationManager authenticationManager) {
    super.setAuthenticationManager(authenticationManager);
    setFilterProcessesUrl("/api/auth/login");
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)throws AuthenticationException {

    String username;
    String password;

    try {
      if (request.getContentType() != null && request.getContentType().startsWith("application/json")) {
        LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
        username = loginRequest.username();
        password = loginRequest.password();
      } else {
        username = obtainUsername(request);
        password = obtainPassword(request);
      }

      if (username == null) username = "";
      if (password == null) password = "";
      username = username.trim();

      UsernamePasswordAuthenticationToken authRequest =
          new UsernamePasswordAuthenticationToken(username, password);

      setDetails(request, authRequest);
      return this.getAuthenticationManager().authenticate(authRequest);
    } catch (IOException e) {
      throw new RuntimeException("로그인 요청 파싱 실패", e);
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authResult)
      throws IOException, ServletException {

    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authResult);
    SecurityContextHolder.setContext(context);

    request.getSession(true).setAttribute(
        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
        context
    );

    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("application/json");
    response.getWriter().write("{\"message\": \"로그인 성공\"}");
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException failed)
      throws IOException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json");
    response.getWriter().write("{\"message\": \"로그인 실패\"}");
  }
}
