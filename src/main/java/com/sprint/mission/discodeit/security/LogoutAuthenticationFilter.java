package com.sprint.mission.discodeit.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class LogoutAuthenticationFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    if (!shouldFilter(request)) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      // 세션 무효화
      HttpSession session = request.getSession(false);
      if (session != null) {
        log.info("세션 무효화: sessionId={}", session.getId());
        session.invalidate();
      }

      // SecurityContext 초기화
      SecurityContextHolder.clearContext();

      response.setStatus(HttpStatus.OK.value());
      log.info("로그아웃 성공");

    } catch (Exception e) {
      log.error("로그아웃 처리 중 오류 발생", e);
      response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
  }

  private boolean shouldFilter(HttpServletRequest request) {
    return "POST".equals(request.getMethod()) &&
        "/api/auth/logout".equals(request.getRequestURI());
  }
}
