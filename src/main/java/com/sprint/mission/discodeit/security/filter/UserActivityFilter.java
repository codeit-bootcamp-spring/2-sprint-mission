package com.sprint.mission.discodeit.security.filter;

import com.sprint.mission.discodeit.service.UserActivityService;
import com.sprint.mission.discodeit.util.AuthUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class UserActivityFilter extends OncePerRequestFilter {


    private final UserActivityService userActivityService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        // 로그인이 되어 있으면 활동시간 갱신
        if (AuthUtils.isLoggedIn()) {
            UUID userId = AuthUtils.getCurrentUser().getId();
            userActivityService.updateLastActiveAt(userId, Instant.now());
        }

        filterChain.doFilter(request, response);
    }
}
