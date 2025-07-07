package com.sprint.mission.discodeit.core.auth.controller;


import com.sprint.mission.discodeit.core.auth.dto.LoginRequest;
import com.sprint.mission.discodeit.core.auth.entity.CustomUserDetails;
import com.sprint.mission.discodeit.core.auth.service.CustomUserDetailsService;
import com.sprint.mission.discodeit.core.user.dto.UserDto;
import com.sprint.mission.discodeit.core.user.dto.request.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.security.jwt.JwtSession;
import com.sprint.mission.discodeit.security.jwt.JwtSessionRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.jaas.AbstractJaasAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "로그인 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

  private final CustomUserDetailsService userDetailsService;
  private final JwtSessionRepository jwtSessionRepository;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  @GetMapping("/csrf-token")
  public ResponseEntity<CsrfToken> getCsrfToken(CsrfToken csrfToken) {
    return ResponseEntity.ok(csrfToken);
  }

  @PutMapping("/role")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<UserDto> updateRole(@RequestBody UserRoleUpdateRequest request) {
    return ResponseEntity.ok(userDetailsService.updateRole(request));
  }

  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest,
      HttpServletResponse response) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
    );

    UserDto userDto = (UserDto) authentication.getPrincipal();
    JwtSession session = jwtService.generateTokens(userDto);

    Cookie refreshTokenCookie = new Cookie("refreshToken", session.getRefreshToken());
    refreshTokenCookie.setHttpOnly(true);
    refreshTokenCookie.setPath("/");
    response.addCookie(refreshTokenCookie);

    return ResponseEntity.ok(session.getAccessToken());
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(
      @CookieValue(name = "refreshToken", required = false) String refreshToken,
      HttpServletResponse response) {
    if (refreshToken != null) {
      jwtService.invalidateRefreshToken(refreshToken);
    }

    Cookie cookie = new Cookie("refreshToken", null);
    cookie.setMaxAge(0);
    cookie.setPath("/");
    response.addCookie(cookie);

    return ResponseEntity.ok().build();
  }

  @PostMapping("/refresh")
  public ResponseEntity<String> refresh(
      @CookieValue(name = "refreshToken", required = false) String refreshToken,
      HttpServletResponse response) {
    if (refreshToken == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token not found.");
    }
    Optional<JwtSession> sessionOptional = jwtService.refreshAccessToken(refreshToken);

    if (sessionOptional.isPresent()) {
      JwtSession newSession = sessionOptional.get();

      Cookie newRefreshTokenCookie = new Cookie("refreshToken", newSession.getRefreshToken());
      newRefreshTokenCookie.setHttpOnly(true);
      newRefreshTokenCookie.setPath("/");
      response.addCookie(newRefreshTokenCookie);

      return ResponseEntity.ok(newSession.getAccessToken());
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token.");
    }
  }

  @GetMapping("/me")
  public ResponseEntity<String> me(
      @CookieValue(name = "refreshToken", required = false) String refreshToken) {
    if (refreshToken == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token not found.");
    }

    Optional<JwtSession> sessionOptional = jwtSessionRepository.findByRefreshToken(refreshToken);

    if (sessionOptional.isPresent()) {
      JwtSession session = sessionOptional.get();
      return ResponseEntity.ok(session.getAccessToken());
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token.");
    }
  }
}
