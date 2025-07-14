package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@RequiredArgsConstructor
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

  private final ObjectMapper objectMapper;
  private final JwtService jwtService;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    DiscodeitUserDetails principal = (DiscodeitUserDetails) authentication.getPrincipal();

    // 2. 토큰 생성
    String accessToken = jwtService.generateTokens(principal.getUserDto()).getAccessToken();
    String refreshToken = jwtService.generateTokens(principal.getUserDto()).getRefreshToken();

    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType(MediaType.TEXT_PLAIN_VALUE);
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(accessToken);

    Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
    refreshCookie.setHttpOnly(true);
    refreshCookie.setPath("/"); // 또는 "/api/auth/refresh"
    refreshCookie.setMaxAge(60 * 60 * 24 * 7); // 7일
    response.addCookie(refreshCookie);
  }
}
