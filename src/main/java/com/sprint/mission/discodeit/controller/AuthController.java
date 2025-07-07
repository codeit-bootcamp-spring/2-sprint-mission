package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.controller.auth.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.controller.user.UserDto;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.security.jwt.JwtSession;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.swagger.AuthApi;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class AuthController implements AuthApi {

  private final AuthService authService;
  private final JwtService jwtService;

  @GetMapping("/csrf-token")
  public ResponseEntity<CsrfToken> getCsrfToken(CsrfToken csrfToken) {
    log.debug("CSRF 토큰 요청");
    return ResponseEntity.ok(csrfToken);
  }

  @GetMapping("/me")
  public ResponseEntity<String> me(@CookieValue("refreshToken") String token) {
    JwtSession jwtSession = jwtService.findByRefreshToken(token);
    return ResponseEntity.ok(jwtSession.getAccessToken());
  }

  @PutMapping("/role")
  public ResponseEntity<UserDto> updateRole(@RequestBody RoleUpdateRequest roleUpdateRequest) {
    return ResponseEntity.ok(authService.updateRole(roleUpdateRequest));
  }

  @PostMapping("/refresh")
  public ResponseEntity<String> refresh(@CookieValue("refreshToken") String token) {
    String accessToken = jwtService.refreshAccessToken(token);
    return ResponseEntity.ok(accessToken);
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletResponse response,
      @CookieValue("refreshToken") String token) {
    jwtService.invalidateRefreshToken(token);

    ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
        .path("/")
        .maxAge(0)
        .httpOnly(true)
        .build();

    // 브라우저는 Set-Cookie 헤더에 Max-Age=0 (혹은 Expires가 과거)인 쿠키가 내려오면, 그 쿠키를 삭제
    response.addHeader("Set-Cookie", deleteCookie.toString());

    return ResponseEntity.noContent().build();
  }
}
