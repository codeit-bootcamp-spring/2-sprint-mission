package com.sprint.mission.discodeit.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class LogoutFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    if (request.getRequestURI().equals("/api/auth/logout")
        && request.getMethod().equalsIgnoreCase("POST")) {

      // 세션 무효화
      HttpSession session = request.getSession(false);
      if (session != null) {
        session.invalidate();
      }

      // SecurityContext 초기화
      SecurityContextHolder.clearContext();

      // 응답 반환
      response.setStatus(HttpServletResponse.SC_OK);
      return; // 더 이상 필터 체인을 진행하지 않음
    }

    // 다음 필터로 진행
    filterChain.doFilter(request, response);
  }
}
