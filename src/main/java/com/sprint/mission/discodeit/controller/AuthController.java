package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.AuthApi;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.security.jwt.JwtSession;
import com.sprint.mission.discodeit.security.jwt.JwtSessionRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController implements AuthApi {

  private final AuthService authService;
  private final UserService userService;
  private final JwtService jwtService;

  @GetMapping("csrf-token")
  public ResponseEntity<CsrfToken> getCsrfToken(CsrfToken csrfToken) {
    log.debug("CSRF 토큰 요청");
    return ResponseEntity.status(HttpStatus.OK).body(csrfToken);
  }

  @GetMapping("me")
  public ResponseEntity<String> me(
      @CookieValue(value = "refresh_token", required = false) String refreshToken) {
    if (refreshToken == null || refreshToken.isBlank()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing refresh token");
    }

    // JwtSession 조회
    JwtSession session = jwtService.findSessionByRefreshToken(refreshToken);
    if (session == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
    }

    // 기존 accessToken 그대로 반환
    return ResponseEntity.ok(session.getAccessToken());
  }


  @PutMapping("role")
  public ResponseEntity<UserDto> role(@RequestBody RoleUpdateRequest request) {
    log.info("권한 수정 요청");
    UserDto userDto = authService.updateRole(request);

    jwtService.forceLogoutUser(request.userId());

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userDto);
  }

  @PostMapping("refresh")
  public ResponseEntity<String> refreshToken(@CookieValue("refresh_token") String refreshToken,
      HttpServletResponse response) {
    JwtSession session = jwtService.findSessionByRefreshToken(refreshToken);
    if (session == null || !jwtService.isValidToken(refreshToken)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    String newAccessToken = jwtService.reissueAccessToken(session.getRefreshToken()).toString();
    // 리프레시 토큰 재발급이 필요하면 여기서 새 쿠키에 담아서 보내기

    return ResponseEntity.ok(newAccessToken);
  }


}
