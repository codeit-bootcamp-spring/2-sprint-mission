package com.sprint.mission.discodeit.security.filter;

import static com.sprint.mission.discodeit.security.config.SecurityConstant.REFRESH_TOKEN;

import com.sprint.mission.discodeit.security.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@RequiredArgsConstructor
public class JwtTokenLogoutHandler implements LogoutHandler {

  private final JwtService jwtService;

  @Override
  public void logout(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication
  ) {
    String refreshToken = extractRefreshTokenFromCookie(request);
    jwtService.invalidateSession(refreshToken);
  }

  private String extractRefreshTokenFromCookie(HttpServletRequest request) {
    if (request.getCookies() == null) {
      return null;
    }

    for (Cookie cookie : request.getCookies()) {
      if (REFRESH_TOKEN.equals(cookie.getName())) {
        return cookie.getValue();
      }
    }
    return null;
  }

}
