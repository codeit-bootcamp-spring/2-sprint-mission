package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.AuthApi;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.security.CustomUserDetails;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController implements AuthApi {

  private final UserService userService;
  private final AuthService authService;

  @GetMapping("/csrf-token")
  public ResponseEntity<CsrfToken> csrfToken(CsrfToken csrfToken) {
    log.info("csrf-token - CSRF 토큰 요청");
    return ResponseEntity.ok(csrfToken);
  }

  @GetMapping("/me")
  public ResponseEntity<UserDto> me(@AuthenticationPrincipal CustomUserDetails userDetails) {
    log.info("me - 정보 조회 요청");
    UUID userId = userDetails.getUserDto().id();
    UserDto res = userService.find(userId);
    return ResponseEntity.ok(res);
  }

  @PutMapping("/role")
  public ResponseEntity<UserDto> updateRole(@RequestBody RoleUpdateRequest request) {
    log.info("role - 권한 수정 요청");
    UserDto res = authService.updateRole(request);
    return ResponseEntity.ok(res);
  }
}
