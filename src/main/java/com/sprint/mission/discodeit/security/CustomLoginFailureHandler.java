package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.exception.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@RequiredArgsConstructor
public class CustomLoginFailureHandler implements AuthenticationFailureHandler {

  private final ObjectMapper objectMapper;

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {
    int status = HttpServletResponse.SC_UNAUTHORIZED;

    response.setStatus(status);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    ErrorResponse errorResponse = new ErrorResponse(
        Instant.now(),
        "AUTHENTICATION_FAILED",
        "아이디 또는 비밀번호가 올바르지 않습니다.",
        Map.of("error", exception.getMessage()),
        exception.getClass().getSimpleName(),
        status
    );

    response.setCharacterEncoding("UTF-8");
    objectMapper.writeValue(response.getWriter(), errorResponse);
  }
}