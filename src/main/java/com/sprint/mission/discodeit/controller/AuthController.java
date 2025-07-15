package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.security.jwt.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;

    @GetMapping("/me")
    public ResponseEntity<String> me(
            @CookieValue(name = JwtService.REFRESH_TOKEN_COOKIE_NAME, required = false) String refreshToken) {
        
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("리프레시 토큰이 필요합니다.");
        }

        return jwtService.getAccessTokenByRefreshToken(refreshToken)
                .map(accessToken -> ResponseEntity.ok("\"" + accessToken + "\""))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 리프레시 토큰입니다."));
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(
            @CookieValue(name = JwtService.REFRESH_TOKEN_COOKIE_NAME, required = false) String refreshToken,
            HttpServletResponse response) {
        
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("리프레시 토큰이 필요합니다.");
        }

        return jwtService.refreshAccessToken(refreshToken)
                .map(tokens -> {
                    Cookie newRefreshCookie = createRefreshTokenCookie(
                            tokens.refreshToken(), 
                            (int) (JwtService.REFRESH_TOKEN_EXPIRATION / 1000)
                    );
                    response.addCookie(newRefreshCookie);
                    return ResponseEntity.ok("\"" + tokens.accessToken() + "\"");
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 리프레시 토큰입니다."));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(cookie -> JwtService.REFRESH_TOKEN_COOKIE_NAME.equals(cookie.getName()))
                .findFirst()
                .ifPresent(cookie -> {
                    jwtService.invalidateRefreshToken(cookie.getValue());
                    response.addCookie(createRefreshTokenCookie(null, 0));
                });

        return ResponseEntity.ok().build();
    }

    private Cookie createRefreshTokenCookie(String token, int maxAgeInSeconds) {
        Cookie cookie = new Cookie(JwtService.REFRESH_TOKEN_COOKIE_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setMaxAge(maxAgeInSeconds);
        return cookie;
    }
}