package com.sprint.mission.discodeit.jwt;

import com.sprint.mission.discodeit.service.UserService;
import io.jsonwebtoken.ExpiredJwtException; // jwt 라이브러리에 따른 예외 예시
import io.jsonwebtoken.MalformedJwtException; // jwt 라이브러리에 따른 예외 예시
import io.jsonwebtoken.SignatureException; // jwt 라이브러리에 따른 예외 예시
import io.jsonwebtoken.UnsupportedJwtException; // jwt 라이브러리에 따른 예외 예시
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthInterceptor implements HandlerInterceptor {

  private static final Logger log = LoggerFactory.getLogger(JwtAuthInterceptor.class);
  private static final String UNAUTHORIZED_ERROR_MESSAGE = "{\"error\":\"Authentication failed\"}"; // 통일된 에러 메시지

  private final JwtUtil jwtUtil;
  private final UserService userService;

  @Override
  public boolean preHandle(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull Object handler
  ) throws Exception {
    if (!(handler instanceof HandlerMethod handlerMethod)) {
      return true;
    }

    // 메소드나 클래스에 @RequiresAuth 어노테이션이 있는지 확인
    RequiresAuth methodAnnotation = handlerMethod.getMethodAnnotation(RequiresAuth.class);
    RequiresAuth classAnnotation = handlerMethod.getBeanType().getAnnotation(RequiresAuth.class);

    if (methodAnnotation == null && classAnnotation == null) {
      return true;
    }

    // --- 인증 처리 시작 ---
    final String authorizationHeader = request.getHeader("Authorization");
    String jwt = null;

    // 1. 헤더 및 Bearer 토큰 형식 확인
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      jwt = authorizationHeader.substring(7);
      log.debug("Extracted JWT: {}", jwt);
    } else {
      log.warn("Authorization header is missing or does not start with Bearer");
      sendUnauthorizedResponse(response); // 통일된 응답 전송
      return false;
    }

    try {
      // 2. JWT 유효성 검증 (서명, 만료 시간 등)
      jwtUtil.validateToken(jwt); // 실패 시 예외 발생을 가정
      log.debug("JWT validation successful (signature, expiration)");

      // 3. 유효한 토큰에서 사용자 ID 추출
      String userIdStr = jwtUtil.extractUserId(jwt); // 유효성 검증 후 추출 시도
      log.debug("Extracted userId: {}", userIdStr);

      // userId가 null이거나 비어있는 경우 처리
      if (userIdStr == null || userIdStr.trim().isEmpty()) {
        log.warn("User ID extracted from token is null or empty");
        sendUnauthorizedResponse(response); // 통일된 응답 전송
        return false;
      }
      UUID userId;
      try {
        userId = UUID.fromString(userIdStr);
        log.debug("Converted userId String to UUID: {}", userId);
      } catch (IllegalArgumentException e) {
        // UUID 형식 오류 처리
        log.warn("User ID string '{}' from token is not a valid UUID format", userIdStr);
        sendUnauthorizedResponse(response); // 형식이 잘못되어도 인증 실패
        return false;
      }

      // 4. 사용자 존재 여부 확인
      boolean userExists = userService.existsById(userId);
      log.debug("Does user {} exist? {}", userId, userExists);

      if (userExists) {
        // 5. 인증 성공: 요청 속성에 userId 저장하고 계속 진행
        request.setAttribute("userId", userId);
        log.debug("Set request attribute 'userId': {}", userId);
        return true;
      } else {
        // 4-1. 사용자가 존재하지 않음
        log.warn("User ID {} from token does not exist", userId);
        sendUnauthorizedResponse(response);
        return false;
      }

    } catch (ExpiredJwtException e) {
      log.warn("JWT validation failed: Token expired - {}", e.getMessage());
      sendUnauthorizedResponse(response); // 통일된 응답 전송
      return false;
    } catch (SignatureException e) {
      log.warn("JWT validation failed: Invalid signature - {}", e.getMessage());
      sendUnauthorizedResponse(response); // 통일된 응답 전송
      return false;
    } catch (MalformedJwtException e) {
      log.warn("JWT validation failed: Malformed token - {}", e.getMessage());
      sendUnauthorizedResponse(response); // 통일된 응답 전송
      return false;
    } catch (UnsupportedJwtException e) {
      log.warn("JWT validation failed: Unsupported token - {}", e.getMessage());
      sendUnauthorizedResponse(response); // 통일된 응답 전송
      return false;
    } catch (IllegalArgumentException e) {
      log.warn("JWT validation failed: Invalid claim or argument - {}", e.getMessage());
      sendUnauthorizedResponse(response); // 통일된 응답 전송
      return false;
    } catch (Exception e) { // 그 외 예상치 못한 예외
      log.error("Unexpected error during JWT authentication", e);
      sendUnauthorizedResponse(response); // 통일된 응답 전송
      return false;
    }
  }

  private void sendUnauthorizedResponse(HttpServletResponse response) throws IOException {
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(UNAUTHORIZED_ERROR_MESSAGE); // 미리 정의된 통일된 메시지 사용
  }
}