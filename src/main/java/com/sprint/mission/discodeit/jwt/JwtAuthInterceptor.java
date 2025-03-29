package com.sprint.mission.discodeit.jwt;

import com.sprint.mission.discodeit.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Component
@RequiredArgsConstructor
public class JwtAuthInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(JwtAuthInterceptor.class);
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler
    ) throws Exception {
        // 핸들러가 컨트롤러 메소드가 아닌 경우 (정적 리소스 등) 통과
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // 메소드나 클래스에 @RequiresAuth 어노테이션이 있는지 확인
        RequiresAuth methodAnnotation = handlerMethod.getMethodAnnotation(RequiresAuth.class);
        RequiresAuth classAnnotation = handlerMethod.getBeanType().getAnnotation(RequiresAuth.class);

        // 어노테이션이 없으면 인증 불필요 (통과)
        if (methodAnnotation == null && classAnnotation == null) {
            return true;
        }

        // 이 아래는 인증이 필요한 경우의 처리
        final String authorizationHeader = request.getHeader("Authorization");

        String userId;
        String jwt;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            log.debug("Extracted JWT: {}", jwt); // 로그 추가

            try {
                userId = jwtUtil.extractUserId(jwt);
                log.debug("Extracted userId: {}", userId); // 로그 추가
            } catch (Exception e) {
                log.error("Failed to extract userId from JWT", e); // 로그 추가
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("{\"error\":\"Missing or invalid Authorization header\"}");
                return false;
            }
        } else {
            log.warn("Authorization header is missing or does not start with Bearer"); // 로그 추가
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("{\"error\":\"Missing or invalid Authorization header\"}");
            return false;
        }

        if (userId != null) {
            boolean isTokenValid = jwtUtil.validateToken(jwt);
            log.debug("Is token valid? {}", isTokenValid); // 로그 추가
            if(isTokenValid) {
                boolean userExists = userService.existsById(userId);
                log.debug("Does user exist? {}", userExists); // 로그 추가

                if (userExists) {
                    request.setAttribute("userId", userId);
                    log.debug("Set request attribute 'userId': {}", userId); // 로그 추가
                    return true;
                } else {
                    log.warn("User ID {} from token does not exist", userId); // 로그 추가
                }
            } else {
                log.warn("Token validation failed for userId {}", userId); // 로그 추가
            }
        } else {
            log.warn("userId is null after extraction attempt"); // 로그 추가
        }


        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write("{\"error\":\"Missing or invalid Authorization header\"}");
        return false;
    }
}
