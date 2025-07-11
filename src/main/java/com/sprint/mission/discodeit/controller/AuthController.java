package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.AuthApi;
import com.sprint.mission.discodeit.dto.data.CsrfTokenDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.dto.request.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.security.UserPrincipal;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.security.jwt.JwtSessionRepository;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final JwtSessionRepository jwtSessionRepository;
  private final UserMapper userMapper;
  private final AuthService authService;

  @Value("${security.jwt.refresh-token-validity-seconds}")
  private long refreshTokenValiditySeconds;

  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
    );
    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
    UserDto userDto = userMapper.toDto(userPrincipal.getUser());

    JwtService.TokenPair tokenPair = jwtService.generateTokens(userPrincipal.getUser(), userDto);

    ResponseCookie cookie = ResponseCookie.from("refresh_token", tokenPair.refreshToken())
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(refreshTokenValiditySeconds)
        .sameSite("Lax")
        .build();

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(tokenPair.accessToken());
  }

  @PostMapping("/refresh")
  public ResponseEntity<String> refreshAccessToken(@CookieValue("refresh_token") String refreshToken) {
    return jwtService.refreshTokens(refreshToken)
        .map(tokenPair -> {
          ResponseCookie cookie = ResponseCookie.from("refresh_token", tokenPair.refreshToken())
              .httpOnly(true)
              .secure(true)
              .path("/")
              .maxAge(refreshTokenValiditySeconds)
              .sameSite("Lax")
              .build();

          return ResponseEntity.ok()
              .header(HttpHeaders.SET_COOKIE, cookie.toString())
              .body(tokenPair.accessToken());
        })
        .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(@CookieValue(name = "refresh_token", required = false) String refreshToken) {
    if (refreshToken != null) {
      jwtService.invalidateRefreshToken(refreshToken);
    }

    ResponseCookie cookie = ResponseCookie.from("refresh_token", "")
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(0)
        .sameSite("Lax")
        .build();

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .build();
  }

  @GetMapping("/me")
  public ResponseEntity<String> getCurrentUser(
      @CookieValue(value = "refresh_token", required = false) String refreshToken) {

    log.info("현재 사용자 정보 조회 요청 - 리프레시 토큰 기반");

    if (refreshToken == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    return jwtSessionRepository.findByRefreshToken(refreshToken)
        .filter(session -> !session.isExpired())
        .map(session -> {
          log.debug("리프레시 토큰으로 액세스 토큰 반환");
          return ResponseEntity.ok(session.getAccessToken());
        })
        .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
  }


  @PutMapping("/role")
  @Override
  public ResponseEntity<UserDto> updateUserRole(@RequestBody @Valid UserRoleUpdateRequest request) {
    UserDto updateUser = authService.updateRole(request);
    jwtService.invalidateAllUserSessions(updateUser.id());
    return ResponseEntity.ok(updateUser);
  }

  @GetMapping("/csrf-token")
  public ResponseEntity<CsrfTokenDto> getCsrfToken(CsrfToken csrfToken) {
    log.info("CSRF 토큰 발급 요청");

    CsrfTokenDto response = new CsrfTokenDto(
        csrfToken.getToken(),
        csrfToken.getHeaderName(),
        csrfToken.getParameterName()
    );

    log.debug("CSRF 토큰 발급 응답: {}", response);
    return ResponseEntity.ok(response);
  }
}
