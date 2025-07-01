package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.AuthApi;
import com.sprint.mission.discodeit.dto.data.CsrfTokenDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.security.UserPrincipal;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController implements AuthApi {

  private final AuthService authService;

  @GetMapping("/me")
  public ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {
    log.info("현재 사용자 정보 조회 요청");

    if (authentication == null || !authentication.isAuthenticated()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
    UserDto userDto = userPrincipal.toUserDto();

    log.debug("현재 사용자 정보 조회 응답: {}", userDto);
    return ResponseEntity.ok(userDto);
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
