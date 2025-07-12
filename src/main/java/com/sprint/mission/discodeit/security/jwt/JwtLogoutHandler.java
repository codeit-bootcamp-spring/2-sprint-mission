package com.sprint.mission.discodeit.security.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Slf4j
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

    private final JwtService jwtService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) {

        String refreshToken = Arrays.stream(request.getCookies())
            .filter(cookie -> "refresh_token".equals(cookie.getName()))
            .map(Cookie::getValue)
            .findFirst()
            .orElse(null);

        if (refreshToken != null) {
            log.debug("리프레쉬 토큰 무효화: {}", refreshToken);
            jwtService.invalidateJwtSession(refreshToken);

            Cookie expiredCookie = new Cookie("refresh_token", "");
            expiredCookie.setMaxAge(0); // 즉시 만료
            expiredCookie.setHttpOnly(true);
            response.addCookie(expiredCookie); // 빈 문자열 쿠키를 응답
        }
    }
}
