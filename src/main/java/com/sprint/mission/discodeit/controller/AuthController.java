package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final JwtService jwtService;

  /**
   * CSRF 토큰을 발급받기 위한 API입니다.
   */
  @GetMapping("/csrf-token")
  public CsrfToken getCsrfToken(CsrfToken csrfToken) {
    return csrfToken;
  }

  /**
   * 현재 로그인된 사용자의 정보를 조회하는 API입니다.
   * JwtAuthenticationFilter에서 SecurityContext에 저장한 UserDto를 바로 받아옵니다.
   */
  @GetMapping("/me")
  public ResponseEntity<UserDto> getMyInfo(@AuthenticationPrincipal UserDto userDto) {
    return ResponseEntity.ok(userDto);
  }

  /**
   * 로그아웃을 처리하는 API입니다.
   * 액세스 토큰은 블랙리스트에, 리프레시 토큰은 DB에서 삭제합니다.
   */
  @PostMapping("/logout")
  public ResponseEntity<Void> logout(
      @RequestHeader("Authorization") String authorizationHeader,
      @CookieValue("refresh_token") String refreshToken) {

    // "Bearer " 접두사 제거
    String accessToken = authorizationHeader.substring(7);

    jwtService.logout(accessToken, refreshToken);

    return ResponseEntity.ok().build();
  }

  @PostMapping("/refresh")
  public ResponseEntity<?> refreshTokens(@CookieValue("refresh_token") String refreshToken) {
    return jwtService.refreshAccessToken(refreshToken)
        .map(tokenPair -> {
          ResponseCookie newRefreshTokenCookie = ResponseCookie.from("refresh_token", tokenPair.refreshToken())
              .path("/")
              .httpOnly(true)
              .secure(true)
              .maxAge(60 * 60 * 24 * 7) // 7일
              .build();

          return ResponseEntity.ok()
              .header("Set-Cookie", newRefreshTokenCookie.toString())
              .body(Map.of("accessToken", tokenPair.accessToken()));
        })
        .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(Map.of("error", "리프레시 토큰이 유효하지 않습니다.")));
  }
}