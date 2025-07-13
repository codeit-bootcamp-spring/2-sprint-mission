package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.UserDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  private final JwtService jwtService;
  private final ObjectMapper objectMapper;

  public JwtAuthenticationSuccessHandler(JwtService jwtService, ObjectMapper objectMapper) {
    this.jwtService = jwtService;
    this.objectMapper = objectMapper;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
    UserDto user = (UserDto) authentication.getPrincipal();

    String accessToken = jwtService.generateAccessToken(user);
    String refreshToken = jwtService.generateRefreshToken(user);
    jwtService.saveSession(user, accessToken, refreshToken);

    Cookie cookie = new Cookie("refresh_token", refreshToken);
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    cookie.setMaxAge((int) jwtService.getRefreshTokenExpiry());
    response.addCookie(cookie);

    response.setContentType("application/json");
    PrintWriter writer = response.getWriter();
    writer.write(objectMapper.writeValueAsString(accessToken));
    writer.flush();
  }
}