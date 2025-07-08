package com.sprint.mission.discodeit.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class CustomLogoutFilter extends OncePerRequestFilter {

    private final PersistentTokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        if ("/api/auth/logout".equals(request.getRequestURI())
            && HttpMethod.POST.matches(request.getMethod())) {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getName() != null) {
                String username = authentication.getName();
                tokenRepository.removeUserTokens(username); // Remember-Me 토큰 삭제
            }

            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate(); // 세션 무효화
            }

            SecurityContextHolder.clearContext(); // SecurityContext 초기화 (인증 정보 제거)

            ResponseCookie deleteCookie = ResponseCookie.from("remember-me", "")
                .path("/")
                .maxAge(0)
                .httpOnly(true)
                .build();
            response.setHeader("Set-Cookie", deleteCookie.toString());

            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        filterChain.doFilter(request, response);

    }
}
