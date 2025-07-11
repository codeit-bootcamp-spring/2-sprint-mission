package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.AuthApi;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.security.jwt.JwtDto;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
  private final UserService userService;

  @GetMapping("/csrf-token")
  public ResponseEntity<CsrfToken> csrfToken(CsrfToken csrfToken) {
    log.info("csrf-token - CSRF 토큰 요청");
    return ResponseEntity.ok(csrfToken);
  }

  @GetMapping("/me")
  public ResponseEntity<String> me(
      @CookieValue(value = "refresh_token", required = false) String refreshToken) {
    log.info("me - 정보 조회 요청");
    JwtDto jwtDto = jwtService.getJwtSession(refreshToken);

    return ResponseEntity.ok(jwtDto.accessToken());
  }

  @PutMapping("/role")
  public ResponseEntity<UserDto> updateRole(@RequestBody RoleUpdateRequest request) {
    log.info("role - 권한 수정 요청");
    UserDto res = authService.updateRole(request);
    return ResponseEntity.ok(res);
  }

  @PostMapping("/refresh")
  public ResponseEntity<?> refreshToken(@CookieValue("refresh_token") String refreshToken,
      HttpServletResponse response) {
    if (refreshToken == null || refreshToken.isBlank() || !jwtService.validateToken(refreshToken)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new DiscodeitException(ErrorCode.INVALID_USER_CREDENTIALS));
    }

    JwtDto newToken = jwtService.refreshToken(refreshToken);

    Cookie cookie = new Cookie(JwtService.REFRESH_TOKEN, newToken.refreshToken());
    cookie.setHttpOnly(true);
    response.addCookie(cookie);

    return ResponseEntity.ok(newToken.accessToken());
  }
}
