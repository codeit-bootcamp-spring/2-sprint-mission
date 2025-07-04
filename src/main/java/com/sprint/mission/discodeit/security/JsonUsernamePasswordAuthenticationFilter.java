package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.controller.auth.LoginRequest;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

// 스프린트 미션에서는 과거 사용했던 Security Filter를 이용하기 위해 JsonUsername~Filter를 통해 로그인 -> 성공했을 시, SuccessHandler에서 토큰 발급으로 하고 있지만,
// (Session = stateless)로 해주면 결국 JsonUsername~Filter -> manager -> provider를 타도 세션이 생성이 안됨 (JWT에선 stateless이므로 Session 사용 X)
// SessionCreationPolicy.ALWAYS -> Provider에서 인증이 성공하면 Session을 만들고, SecurityContext를 저장함 -> 인증 상태 유지
// 처음부터 JWT를 사용해라! 하면 Login API 만들고, 성공했을 때 토큰 발급하는 식으로 만드는 것이 훨씬 편할 듯 하다.
@RequiredArgsConstructor
public class JsonUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final ObjectMapper objectMapper;

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {

    if (!request.getMethod().equals("POST")) {
      throw new AuthenticationServiceException(
          "Authentication method not supported: " + request.getMethod());
    }

    try {
      // JSON 바디(바이트 스트림) -> 객체 역직렬화
      LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(),
          LoginRequest.class);

      UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
          loginRequest.username(), loginRequest.password());
      this.setDetails(request, authToken);

      return this.getAuthenticationManager().authenticate(authToken);
    } catch (IOException e) {
      throw new AuthenticationServiceException("Request parsing failed", e);
    }
  }

  public static class Configurer extends
      AbstractAuthenticationFilterConfigurer<HttpSecurity, Configurer, JsonUsernamePasswordAuthenticationFilter> {

    private final ObjectMapper objectMapper;
    private final JwtService jwtService;

    public Configurer(ObjectMapper objectMapper, JwtService jwtService) {
      super(new JsonUsernamePasswordAuthenticationFilter(objectMapper), "/api/auth/login");
      this.objectMapper = objectMapper;
      this.jwtService = jwtService;
    }

    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
      return new AntPathRequestMatcher(loginProcessingUrl, HttpMethod.POST.name());
    }

    @Override
    public void init(HttpSecurity http) throws Exception {
      // 내부적으로 createLoginProcessingUrlMatcher("/api/auth/login")가 호출
      // -> AntPathRequestMatcher("/api/auth/login", "POST") 가 생성
      loginProcessingUrl("/api/auth/login");
      successHandler(new CustomLoginSuccessHandler(objectMapper, jwtService));
      failureHandler(new CustomLoginFailureHandler(objectMapper));
    }
  }
}
