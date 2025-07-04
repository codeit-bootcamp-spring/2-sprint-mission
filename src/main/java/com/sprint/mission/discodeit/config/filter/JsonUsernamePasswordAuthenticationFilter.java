package com.sprint.mission.discodeit.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class JsonUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  // 이 필터는 "/api/auth/login" 경로의 POST 요청만 처리하도록 설정
  private static final AntPathRequestMatcher DEFAULT_LOGIN_REQUEST_MATCHER =
      new AntPathRequestMatcher("/api/auth/login", "POST");

  private final ObjectMapper objectMapper;

  public JsonUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper) {
    super(DEFAULT_LOGIN_REQUEST_MATCHER);
    this.objectMapper = objectMapper;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException, IOException {

    // 요청 본문(JSON)을 LoginRequest 자바 객체로 변환
    LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);

    String username = loginRequest.username();
    String password = loginRequest.password();

    // 아이디와 비밀번호가 비어있는지 확인
    if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
      throw new AuthenticationServiceException("Username or Password not provided");
    }

    // 아직 인증되지 않은 AuthenticationToken 객체 생성
    UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);

    // AuthenticationManager에게 인증 처리를 위임
    return this.getAuthenticationManager().authenticate(authRequest);
  }
}