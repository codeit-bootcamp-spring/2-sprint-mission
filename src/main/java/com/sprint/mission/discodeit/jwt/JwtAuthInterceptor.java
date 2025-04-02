package com.sprint.mission.discodeit.jwt;

import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final UserRepository userRepository;

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
            try {
                userId = jwtUtil.extractUserId(jwt);
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

        if (userId != null && jwtUtil.validateToken(jwt)) {
            // 사용자 존재 여부 확인
            if (!userRepository.existsById(UUID.fromString(userId))) {
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
