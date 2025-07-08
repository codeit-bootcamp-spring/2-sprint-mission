package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.CsrfToken;
import com.sprint.mission.discodeit.repository.CsrfTokenJpaRepository;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CsrfTokenDbService implements CsrfTokenRepository {

    private static final String DEFAULT_CSRF_PARAMETER_NAME = "_csrf";
    private static final String DEFAULT_CSRF_HEADER_NAME = "X-XSRF-TOKEN";
    private static final int TOKEN_VALIDITY_HOURS = 24;
    private final CsrfTokenJpaRepository csrfTokenJpaRepository;

    @PostConstruct
    @Transactional
    public void clearAllTokensOnStartup() {
        long count = csrfTokenJpaRepository.count();
        if (count > 0) {
            csrfTokenJpaRepository.deleteAll();
        }
    }

    @Override
    public org.springframework.security.web.csrf.CsrfToken generateToken(
        HttpServletRequest request) {
        String tokenValue = UUID.randomUUID().toString();
        return new DefaultCsrfToken(DEFAULT_CSRF_HEADER_NAME, DEFAULT_CSRF_PARAMETER_NAME,
            tokenValue);
    }

    @Override
    @Transactional
    public void saveToken(org.springframework.security.web.csrf.CsrfToken token,
        HttpServletRequest request, HttpServletResponse response) {
        String sessionId = getSessionId(request);

        if (token == null) {
            csrfTokenJpaRepository.deleteBySessionId(sessionId);
            return;
        }

        try {
            LocalDateTime expiresAt = LocalDateTime.now().plusHours(TOKEN_VALIDITY_HOURS);

            // 기존 토큰 삭제
            csrfTokenJpaRepository.deleteBySessionId(sessionId);

            // 새 토큰 저장
            CsrfToken tokenEntity = CsrfToken.builder().sessionId(sessionId).token(token.getToken())
                .headerName(token.getHeaderName()).parameterName(token.getParameterName())
                .expiresAt(expiresAt).build();

            csrfTokenJpaRepository.save(tokenEntity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public org.springframework.security.web.csrf.CsrfToken loadToken(HttpServletRequest request) {
        String sessionId = getSessionId(request);

        Optional<CsrfToken> tokenEntityOpt = csrfTokenJpaRepository.findBySessionId(sessionId);

        if (tokenEntityOpt.isEmpty()) {
            return null;
        }

        CsrfToken tokenEntity = tokenEntityOpt.get();

        if (tokenEntity.isExpired()) {
            return null;
        }

        return new DefaultCsrfToken(tokenEntity.getHeaderName(), tokenEntity.getParameterName(),
            tokenEntity.getToken());
    }


    private String getSessionId(HttpServletRequest request) {
        String sessionId = request.getSession(true).getId();
        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
        }
        return sessionId;
    }
}
