package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
public class JsonUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final ObjectMapper objectMapper = new ObjectMapper();

  public JsonUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
    super.setAuthenticationManager(authenticationManager);
    // 필터 처리 경로 설정
    setFilterProcessesUrl("/api/auth/login");
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      LoginPayload login = objectMapper.readValue(request.getInputStream(), LoginPayload.class);
      UsernamePasswordAuthenticationToken authRequest =
          new UsernamePasswordAuthenticationToken(login.username(), login.password());
      setDetails(request, authRequest);
      return this.getAuthenticationManager().authenticate(authRequest);
    } catch (IOException e) {
      throw new RuntimeException("로그인 요청 파싱 실패", e);
    }
  }

  public record LoginPayload(String username, String password) {

  }
}
