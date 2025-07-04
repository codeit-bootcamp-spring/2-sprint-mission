package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.controller.user.UserDto;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.security.jwt.JwtSession;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@RequiredArgsConstructor
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

  private final ObjectMapper objectMapper;
  private final JwtService jwtService;

  // JsonUsernamePasswordAuthenticationFilter를 통해 인증이 성공했을 시, 핸들러를 통해 response가 응답됨
  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    UserDto userDto = ((CustomUserDetails) authentication.getPrincipal()).getUserDto();

    // 인증 성공 시 토큰 발급 (jwtSession 생성)
    JwtSession jwtSession = jwtService.generateJwtSession(userDto);
    String accessToken = jwtSession.getAccessToken();
    String refreshToken = jwtSession.getRefreshToken();

    // RefreshToken을 HttpOnly 쿠키로 저장
    Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
    // XSS 공격 방지, HTTP 요청/응답에서만 쿠키 전송됨
    refreshCookie.setHttpOnly(true);
    refreshCookie.setPath("/");
    refreshCookie.setMaxAge(14 * 24 * 60 * 60);
    response.addCookie(refreshCookie);

    // AccessToken을 응답 Body에 문자열로 반환
    response.setContentType("text/plain;charset=UTF-8");
    response.getWriter().write(accessToken);
    response.getWriter().flush();
  }
}
