package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;

@RequiredArgsConstructor
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

  private final ObjectMapper objectMapper;
  private final PersistentTokenBasedRememberMeServices rememberMeServices;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    Boolean rememberMe = (Boolean) request.getAttribute("rememberMe");
    if (Boolean.TRUE.equals(rememberMe)) {
      rememberMeServices.loginSuccess(request, response, authentication);
    }

    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

    response.setStatus(HttpServletResponse.SC_OK);
    response.setCharacterEncoding("UTF-8"); // 한글 문자 encoding
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    objectMapper.writeValue(response.getWriter(), userDetails.getUserDto());
  }
}