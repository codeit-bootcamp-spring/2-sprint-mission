package com.sprint.mission.discodeit.security.jwt;

import static com.sprint.mission.discodeit.security.config.SecurityMatchers.DOWNLOAD;
import static com.sprint.mission.discodeit.security.config.SecurityMatchers.GET_CSRF_TOKEN;
import static com.sprint.mission.discodeit.security.config.SecurityMatchers.LOGIN;
import static com.sprint.mission.discodeit.security.config.SecurityMatchers.LOGOUT;
import static com.sprint.mission.discodeit.security.config.SecurityMatchers.ME;
import static com.sprint.mission.discodeit.security.config.SecurityMatchers.NON_API;
import static com.sprint.mission.discodeit.security.config.SecurityMatchers.PUBLIC_MATCHERS;
import static com.sprint.mission.discodeit.security.config.SecurityMatchers.REFRESH;
import static com.sprint.mission.discodeit.security.config.SecurityMatchers.SIGN_UP;

import com.sprint.mission.discodeit.domain.user.dto.UserResult;
import com.sprint.mission.discodeit.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return Arrays.stream(PUBLIC_MATCHERS)
        .anyMatch(matcher -> matcher.matches(request));
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain

  ) throws ServletException, IOException {
    String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      AuthenticationException ex = new InsufficientAuthenticationException("엑세스 토큰이 없습니다.");
      jwtAuthenticationEntryPoint.commence(request, response, ex);
      return;
    }

    String token = authHeader.substring(7);
    if (isInvalidToken(token)) {
      AuthenticationException ex = new BadCredentialsException("유효하지 않은 토큰입니다.");
      jwtAuthenticationEntryPoint.commence(request, response, ex);
      return;
    }

    UserResult userResult = jwtService.parseUser(token);
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
        userResult, null, List.of(new SimpleGrantedAuthority("ROLE_" + userResult.role())));
    SecurityContextHolder.getContext().setAuthentication(authentication);

    filterChain.doFilter(request, response);
  }

  private boolean isInvalidToken(String accessToken) {
    return jwtService.isInvalidAccessToken(accessToken);
  }

}
