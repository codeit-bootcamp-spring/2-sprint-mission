package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.swagger.AuthApi;
import com.sprint.mission.discodeit.dto.auth.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.security.jwt.JwtSession;
import com.sprint.mission.discodeit.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final UserService userService;
    private final JwtService jwtService;

    @GetMapping("/csrf-token")
    public ResponseEntity<CsrfToken> getCsrfToken(
      CsrfToken csrfToken
    ) {
        return ResponseEntity.status(HttpStatus.OK)
            .header("X-CSRF-TOKEN", csrfToken.getToken())
            .body(csrfToken);
    }

    @GetMapping("/me")
    public ResponseEntity<String> getCurrentUser(
        @CookieValue(value = "refresh_token") String refreshToken
    ) {
        JwtSession jwtSession = jwtService.getJwtSession(refreshToken);

        return ResponseEntity.status(HttpStatus.OK)
            .body(jwtSession.getAccessToken());
    }

    @PutMapping("/role")
    public ResponseEntity<UserDto> updateRole(
        @RequestBody RoleUpdateRequest roleUpdateRequest
    ) {
        UserDto userDto = userService.updateRole(roleUpdateRequest);

        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshToken(
        @CookieValue(value = "refresh_token") String refreshToken,
        HttpServletResponse response
    ) {
        JwtSession jwtSession = jwtService.refreshJwtSession(refreshToken);
        Cookie refreshTokenCookie = new Cookie("refresh_token", jwtSession.getRefreshToken());
        response.addCookie(refreshTokenCookie);

        return ResponseEntity.
            status(HttpStatus.OK)
            .body(jwtSession.getAccessToken());
    }
}