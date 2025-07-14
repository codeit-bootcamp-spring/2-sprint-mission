package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.security.jwt.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@RequiredArgsConstructor
public class SessionRegistryLogoutHandler implements LogoutHandler {

  private final JwtService jwtService;

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {
    // 1. 쿠키에서 refresh_token 추출
    String refreshToken = extractRefreshTokenFromCookie(request);
    if (refreshToken == null || refreshToken.isBlank()) {
      return; // 토큰 없으면 아무 작업도 하지 않음 (옵션: 로그 남기기)
    }

    // 2. JwtService로 무효화
    jwtService.revokeRefreshToken(refreshToken);

    // 3. 클라이언트 쿠키 제거 (브라우저 삭제 유도)
    Cookie deleteCookie = new Cookie("refresh_token", null);
    deleteCookie.setHttpOnly(true);
    deleteCookie.setPath("/");
    deleteCookie.setMaxAge(0); // 즉시 삭제
    response.addCookie(deleteCookie);
  }

  private String extractRefreshTokenFromCookie(HttpServletRequest request) {
    if (request.getCookies() == null) {
      return null;
    }

    for (Cookie cookie : request.getCookies()) {
      if ("refresh_token".equals(cookie.getName())) {
        return cookie.getValue();
      }
    }
    return null;
  }
}
