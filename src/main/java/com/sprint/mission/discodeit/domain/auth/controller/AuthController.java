package com.sprint.mission.discodeit.domain.auth.controller;

import com.sprint.mission.discodeit.domain.auth.dto.RoleUpdateRequest;
import com.sprint.mission.discodeit.domain.auth.service.AuthService;
import com.sprint.mission.discodeit.domain.user.dto.UserResult;
import com.sprint.mission.discodeit.security.jwt.service.JwtService;
import com.sprint.mission.discodeit.security.userDetails.CustomUserDetails;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

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
    if (refreshToken == null) {
      return ResponseEntity.status(401)
          .body("리프레시 토큰이 없습니다.");
    }
    String accessToken = jwtService.getAccessTokenByRefreshToken(refreshToken);

    return ResponseEntity.ok(accessToken);
  }

  @PutMapping("/role")
  public ResponseEntity<UserResult> updateRole(
      RoleUpdateRequest roleUpdateRequest,
      @AuthenticationPrincipal CustomUserDetails userDetails
  ) {
    UserResult userResult = authService.updateRole(roleUpdateRequest);

    return ResponseEntity.ok(userResult);
  }

  private String extractRefreshTokenFromCookie(HttpServletRequest request) {
    if (request.getCookies() == null) {
      return null;
    }

    for (Cookie cookie : request.getCookies()) {
      if ("refresh_token".equals(cookie.getName())) {
        return cookie.getValue();
      }
    }
    return null;
  }

}
