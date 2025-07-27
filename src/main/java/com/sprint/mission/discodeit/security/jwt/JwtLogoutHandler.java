package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.event.sse.SseUserRefreshEvent;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Slf4j
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

  private final JwtService jwtService;
  private final ApplicationEventPublisher eventPublisher;

  @SneakyThrows
  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {
    resolveRefreshToken(request)
        .ifPresent(refreshToken -> {
          JwtSession jwtSession = jwtService.getJwtSession(refreshToken);
          UUID userId = jwtSession.getUserId();

          jwtService.invalidateJwtSession(refreshToken);
          invalidateRefreshTokenCookie(response);

          eventPublisher.publishEvent(new SseUserRefreshEvent(
              null,
              userId,
              Instant.now()
          ));
          log.info("사용자 로그아웃 및 오프라인 상태 변경 이벤트 발행: userId={}", userId);
        });
  }

  private Optional<String> resolveRefreshToken(HttpServletRequest request) {
    return Arrays.stream(request.getCookies())
        .filter(cookie -> cookie.getName().equals(JwtService.REFRESH_TOKEN_COOKIE_NAME))
        .findFirst()
        .map(Cookie::getValue);
  }

  private void invalidateRefreshTokenCookie(HttpServletResponse response) {
    Cookie refreshTokenCookie = new Cookie(JwtService.REFRESH_TOKEN_COOKIE_NAME, "");
    refreshTokenCookie.setMaxAge(0);
    refreshTokenCookie.setHttpOnly(true);
    response.addCookie(refreshTokenCookie);
  }
}