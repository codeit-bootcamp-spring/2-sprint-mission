package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.AuthApi;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.security.JwtService;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController implements AuthApi {

  private final AuthService authService;
  private final JwtService jwtService;

  @GetMapping("csrf-token")
  public ResponseEntity<CsrfToken> getCsrfToken(CsrfToken csrfToken) {
    log.debug("CSRF 토큰 요청");
    return ResponseEntity.status(HttpStatus.OK).body(csrfToken);
  }

  @GetMapping("me")
  public ResponseEntity<String> me(@CookieValue(name = "refresh_token", required = false) String refreshToken) {
    log.info("Access Token 재조회 요청 (refresh 기반)");
    Optional<String> token = jwtService.getSessionByRefreshToken(refreshToken)
        .map(session -> session.getAccessToken());
    return token.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(401).build());
  }

  @PostMapping("logout")
  public ResponseEntity<Void> logout(@CookieValue(name = "refresh_token", required = false) String refreshToken,
      HttpServletResponse response) {
    log.info("로그아웃 요청");
    if (refreshToken != null) {
      jwtService.invalidateRefreshToken(refreshToken);
      Cookie cookie = new Cookie("refresh_token", null);
      cookie.setHttpOnly(true);
      cookie.setPath("/");
      cookie.setMaxAge(0);
      response.addCookie(cookie);
    }
    return ResponseEntity.ok().build();
  }

  @PostMapping("refresh")
  public ResponseEntity<String> refresh(@CookieValue(name = "refresh_token", required = false) String refreshToken,
      HttpServletResponse response) {
    log.info("액세스 토큰 재발급 요청");
    if (refreshToken == null || jwtService.getSessionByRefreshToken(refreshToken).isEmpty()) {
      return ResponseEntity.status(401).build();
    }
    String newAccessToken = jwtService.reissueAccessToken(refreshToken);
    return ResponseEntity.ok(newAccessToken);
  }

  @PutMapping("role")
  public ResponseEntity<UserDto> role(@RequestBody RoleUpdateRequest request) {
    log.info("권한 수정 요청");
    UserDto userDto = authService.updateRole(request);
    jwtService.invalidateUserSessions(userDto.id());
    return ResponseEntity.status(HttpStatus.OK).body(userDto);
  }
}
