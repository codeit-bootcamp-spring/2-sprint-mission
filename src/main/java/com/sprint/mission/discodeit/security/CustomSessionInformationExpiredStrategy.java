package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

// 세션이 만료되었을 때 실행되는 커스텀 핸들러
@RequiredArgsConstructor
public class CustomSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {

    private final ObjectMapper objectMapper;

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event)
        throws IOException, ServletException {

        HttpServletResponse response = event.getResponse();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> simpleError = Map.of(
            "code", "SESSION_EXPIRED",
            "message", "Your session has expired.",
            "status", HttpServletResponse.SC_UNAUTHORIZED
        );

        objectMapper.writeValue(response.getWriter(), simpleError);
    }
}
