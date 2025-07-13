package com.sprint.mission.discodeit.security.jwt;

import static com.sprint.mission.discodeit.security.config.SecurityMatchers.GET_CSRF_TOKEN;
import static com.sprint.mission.discodeit.security.config.SecurityMatchers.LOGIN;
import static com.sprint.mission.discodeit.security.config.SecurityMatchers.LOGOUT;
import static com.sprint.mission.discodeit.security.config.SecurityMatchers.NON_API;
import static com.sprint.mission.discodeit.security.config.SecurityMatchers.SIGN_UP;

import com.sprint.mission.discodeit.domain.user.dto.UserResult;
import com.sprint.mission.discodeit.security.jwt.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;

  private static final List<RequestMatcher> EXCLUDE_MATCHERS = Arrays.asList(
      NON_API, GET_CSRF_TOKEN, SIGN_UP, LOGOUT, LOGIN
  );

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    return EXCLUDE_MATCHERS.stream()
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
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "엑세스 토큰이 없습니다.");
      return;
    }

    String token = authHeader.substring(7);
    if (isInvalidToken(token)) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.");
      return;
    }

    UserResult userResult = jwtService.parseUser(token);
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
        userResult, null, List.of());
    SecurityContextHolder.getContext().setAuthentication(authentication);

    filterChain.doFilter(request, response);
  }

  private boolean isInvalidToken(String accessToken) {
    return !jwtService.validateAccessToken(accessToken);
  }

}
