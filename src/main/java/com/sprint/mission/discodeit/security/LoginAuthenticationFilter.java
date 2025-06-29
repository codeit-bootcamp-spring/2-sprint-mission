package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final ObjectMapper objectMapper = new ObjectMapper();

  public LoginAuthenticationFilter(AuthenticationManager authenticationManager) {
    super.setAuthenticationManager(authenticationManager);
    setFilterProcessesUrl("/api/auth/login");
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)throws AuthenticationException {

    try {
      LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);

      UsernamePasswordAuthenticationToken authRequest =
          new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());

      return this.getAuthenticationManager().authenticate(authRequest);
    } catch (IOException e) {
      throw new RuntimeException("로그인 요청 파싱 실패", e);
    }
  }

}
