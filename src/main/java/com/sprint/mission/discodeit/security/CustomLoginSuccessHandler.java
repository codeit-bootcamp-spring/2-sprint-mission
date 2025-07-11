package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.security.jwt.JwtSession;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Slf4j
@RequiredArgsConstructor
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

  private final ObjectMapper objectMapper;
  private final JwtService jwtService;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    log.debug("[LOGIN SUCCESS] 진입: contentType={}, Accept={}",
        request.getContentType(), request.getHeader("Accept"));

    CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();

    JwtSession jwtSession = jwtService.generationToken(principal.getUserDto());
    String accessToken = jwtSession.getAccessToken();
    String refreshToken = jwtSession.getRefreshToken();

    Cookie cookie = new Cookie(JwtService.REFRESH_TOKEN, refreshToken);
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    response.addCookie(cookie);

    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");

    String payload = objectMapper.writeValueAsString(accessToken);
    response.getWriter().write(payload);
    response.getWriter().flush();

    log.info("[성공 응답] {}", objectMapper.writeValueAsString(principal.getUserDto()));
  }
}
