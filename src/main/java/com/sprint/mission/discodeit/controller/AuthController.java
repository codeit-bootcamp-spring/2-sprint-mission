package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.AuthApi;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.security.CustomUserDetails;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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

  private final AuthService authService;

  @PutMapping(value = "/role")
  public ResponseEntity<UserDto> updateUserRole(
      @RequestBody @Valid RoleUpdateRequest request
  ) {
    log.info("사용자 권한 수정 요청: request={}", request);
    UserDto updatedUser = authService.updateUserRole(request);
    log.debug("사용자 수정 응답: {}", updatedUser);
    return ResponseEntity.ok(updatedUser);
  }

  @GetMapping("/csrf-token")
  public ResponseEntity<CsrfToken> getCsrfToken(HttpServletRequest request) {
    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    if (csrfToken == null) {
      return ResponseEntity.internalServerError().build();
    }
    return ResponseEntity.ok(csrfToken);
  }

  @GetMapping("/me")
  public UserDto me(@AuthenticationPrincipal CustomUserDetails principal) {
    return ResponseEntity.ok(principal.getUserDto()).getBody();
  }

}
