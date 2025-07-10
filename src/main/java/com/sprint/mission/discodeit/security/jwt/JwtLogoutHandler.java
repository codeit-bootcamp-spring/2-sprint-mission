package com.sprint.mission.discodeit.security.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

    private final JwtService jwtService;
    private static final String REFRESH_TOKEN_NAME = "refresh_token";

    @Override
    public void logout(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) {
        String refreshToken = extractRefreshTokenFromCookie(request);

        if (refreshToken != null) {
            jwtService.invalidateJwtSession(refreshToken);
            expireRefreshTokenCookie(response);
        }
    }

    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        return request.getCookies() == null ? null :
            Arrays.stream(request.getCookies())
                .filter(cookie -> REFRESH_TOKEN_NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    private void expireRefreshTokenCookie(HttpServletResponse response) {
        Cookie expiredCookie = new Cookie("refresh_token", "");
        expiredCookie.setHttpOnly(true);
        expiredCookie.setMaxAge(0);
        response.addCookie(expiredCookie);
    }
}
