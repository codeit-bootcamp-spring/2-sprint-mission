package com.sprint.mission.discodeit.auth;

import com.sprint.mission.discodeit.core.user.usecase.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class JwtAuthInterceptor implements HandlerInterceptor {

  private final JwtUtil jwtUtil;
  private final UserService userService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    final String authorizationHeader = request.getHeader("Authorization");

    UUID userId = null;
    String jwt = null;

    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      jwt = authorizationHeader.substring(7);
      try {
        userId = UUID.fromString(jwtUtil.extractUserId(jwt));
      } catch (Exception e) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write("{\"error\":\"Invalid token\"}");
        return false;
      }
    } else {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.getWriter().write("{\"error\":\"Missing or invalid Authorization header\"}");
      return false;
    }

    if (jwtUtil.validateToken(jwt)) {
      // 사용자 존재 여부 확인
      if (!userService.existsById(userId)) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write("{\"error\":\"User not found\"}");
        return false;
      }

      // 요청 속성에 사용자 ID 저장 (컨트롤러에서 접근 가능)
      request.setAttribute("userId", userId);
      return true;
    }

    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.getWriter().write("{\"error\":\"Invalid token\"}");
    return false;
  }
}
