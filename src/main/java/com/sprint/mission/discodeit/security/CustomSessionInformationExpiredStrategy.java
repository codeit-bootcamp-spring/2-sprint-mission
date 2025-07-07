package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.api.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

@RequiredArgsConstructor
public class CustomSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {

    private final ObjectMapper objectMapper;

    @Override
    public void onExpiredSessionDetected(
        SessionInformationExpiredEvent event
    ) throws IOException, ServletException {
        int status = HttpServletResponse.SC_UNAUTHORIZED;
        HttpServletResponse response = event.getResponse();
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ErrorResponse errorResponse = new ErrorResponse(
            Instant.now(),
            HttpStatus.UNAUTHORIZED.name(),
            "SESSION_EXPIRED",
            null,
            "AUTH_EXCEPTION",
            status
        );

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
