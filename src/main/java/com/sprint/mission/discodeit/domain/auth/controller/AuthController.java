package com.sprint.mission.discodeit.domain.auth.controller;

import com.sprint.mission.discodeit.domain.auth.dto.RoleUpdateRequest;
import com.sprint.mission.discodeit.domain.auth.service.AuthService;
import com.sprint.mission.discodeit.domain.user.dto.UserResult;
import com.sprint.mission.discodeit.security.jwt.JwtSession;
import com.sprint.mission.discodeit.security.jwt.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;
  private final JwtService jwtService;

  @GetMapping("/csrf-token")
  public ResponseEntity<CsrfToken> getCsrfToken(CsrfToken csrfToken) {
    return ResponseEntity.ok(csrfToken);
  }

  @GetMapping("/me")
  public ResponseEntity<String> getCurrentUser(HttpServletRequest request) {
    String refreshToken = extractRefreshTokenFromCookie(request);
    String accessToken = jwtService.getAccessTokenByRefreshToken(refreshToken);

    return ResponseEntity.ok(accessToken);
  }

  @PutMapping("/role")
  public ResponseEntity<UserResult> updateRole(
      @RequestBody RoleUpdateRequest roleUpdateRequest,
      HttpServletRequest request
  ) {
    UserResult userResult = authService.updateRole(roleUpdateRequest);
    String refreshToken = extractRefreshTokenFromCookie(request);
    jwtService.invalidateSession(refreshToken);

    return ResponseEntity.ok(userResult);
  }

  @PostMapping("/refresh")
  public ResponseEntity<String> revokeAccessToken(
      HttpServletRequest request,
      HttpServletResponse response
  ) {
    String refreshToken = extractRefreshTokenFromCookie(request);
    JwtSession jwtSession = jwtService.refreshSession(refreshToken);

    Cookie newRefreshCookie = new Cookie("refresh_token", jwtSession.getRefreshToken());
    response.addCookie(newRefreshCookie);

    return ResponseEntity.ok(jwtSession.getAccessToken());
  }

  private String extractRefreshTokenFromCookie(HttpServletRequest request) {
    if (request.getCookies() == null) {
      throw new IllegalArgumentException("리프레시 토큰이 없습니다.");
    }

    for (Cookie cookie : request.getCookies()) {
      if ("refresh_token".equals(cookie.getName())) {
        return cookie.getValue();
      }
    }

    throw new IllegalArgumentException("리프레시 토큰이 없습니다.");
  }

}
