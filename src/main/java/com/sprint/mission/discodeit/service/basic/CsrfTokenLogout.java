package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.repository.CsrfTokenJpaRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CsrfTokenLogout implements LogoutHandler {

    private final CsrfTokenJpaRepository csrfTokenJpaRepository;

    @Override
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) {
        String sessionId = null;
        if (request.getSession(false) != null) {
            sessionId = request.getSession(false).getId();
        }

        if (sessionId != null) {
            csrfTokenJpaRepository.deleteBySessionId(sessionId);
            log.debug("토큰 삭제 완료");
        }
    }
} 