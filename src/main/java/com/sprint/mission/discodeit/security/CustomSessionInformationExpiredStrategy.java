package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.exception.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

@RequiredArgsConstructor
public class CustomSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {

  private final ObjectMapper objectMapper;

  @Override
  public void onExpiredSessionDetected(SessionInformationExpiredEvent event)
      throws IOException, ServletException {
    HttpServletResponse response = event.getResponse();
    int status = HttpServletResponse.SC_UNAUTHORIZED;
    response.setStatus(status);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    ErrorResponse errorResponse = new ErrorResponse(
        Instant.now(),
        "SESSION_EXPIRED",
        "다른 장치에서 로그인하여 세션이 만료되었습니다.",
        Map.of("reason", "Concurrent login detected"),
        "SessionInformationExpiredException",
        status
    );
    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
  }
}

