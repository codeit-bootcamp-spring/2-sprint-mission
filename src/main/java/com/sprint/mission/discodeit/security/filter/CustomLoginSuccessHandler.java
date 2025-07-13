package com.sprint.mission.discodeit.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.security.jwt.JwtSession;
import com.sprint.mission.discodeit.security.jwt.service.JwtService;
import com.sprint.mission.discodeit.security.userDetails.CustomUserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
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
      Authentication authentication
  ) throws IOException, ServletException {
    CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter()
        .write(objectMapper.writeValueAsString(principal.getUserResult()));

    JwtSession jwtSession = jwtService.generateSession(principal.getUserResult());
    Cookie cookie = new Cookie("refresh_token", jwtSession.getRefreshToken());
    response.addCookie(cookie);
    response.getWriter()
        .write(objectMapper.writeValueAsString(jwtSession.getAccessToken()));
  }

}
