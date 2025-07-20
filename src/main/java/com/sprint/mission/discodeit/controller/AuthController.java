package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.security.jwt.JwtToken;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final UserMapper userMapper;

    @GetMapping("/csrf-token")
    public Object csrf(CsrfToken csrfToken) {
        return csrfToken;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userMapper.toDto(user, true));
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

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(
            @CookieValue(name = JwtService.REFRESH_TOKEN_COOKIE_NAME, required = false) String refreshToken,
            HttpServletResponse response
    ) {
        var tokens = jwtService.refreshAccessToken(refreshToken);
        response.addCookie(createRefreshTokenCookie(tokens.refreshToken(), (int) (JwtService.REFRESH_TOKEN_EXPIRATION / 1000)));
        return ResponseEntity.ok(tokens.accessToken());
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

