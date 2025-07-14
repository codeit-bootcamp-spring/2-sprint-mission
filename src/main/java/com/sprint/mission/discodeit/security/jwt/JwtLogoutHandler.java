package com.sprint.mission.discodeit.security.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

  private final JwtService jwtService;

  @Override
  public void logout(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    String refreshToken = extractRefreshTokenFromCookie(request);

    if (refreshToken != null) {
      JwtSession jwtSession = jwtService.findJwtSessionByRefreshToken(refreshToken);
      jwtService.deleteExpiredToken(jwtSession);

      ResponseCookie expiredCookie = ResponseCookie.from("refresh_token", "")
          .path("/api/auth/refresh")
          .httpOnly(true)
          .maxAge(0)
          .build();

      response.addHeader(HttpHeaders.SET_COOKIE, expiredCookie.toString());
    }

    SecurityContextHolder.clearContext();
  }

  private String extractRefreshTokenFromCookie(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies == null) {
      return null;
    }

    for (Cookie cookie : cookies) {
      if (cookie.getName().equals("refresh_token")) {
        return cookie.getValue();
      }
    }

    return null;
  }

}
