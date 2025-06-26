package com.sprint.mission.discodeit.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class CustomLogoutFilter extends OncePerRequestFilter {

    private static final String LOGOUT_URI = "/api/auth/logout";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        if (LOGOUT_URI.equals(request.getRequestURI())
                && "POST".equalsIgnoreCase(request.getMethod())) {

            log.info("로그아웃 처리 시작");

            if (request.getSession(false) != null) {
                request.getSession(false).invalidate();
                log.debug("세션 무효화 완료");
            }

            SecurityContextHolder.clearContext();
            log.debug("SecurityContext 초기화 완료");

            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
