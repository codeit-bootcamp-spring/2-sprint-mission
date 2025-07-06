package com.sprint.mission.discodeit.domain.auth.controller;

import com.sprint.mission.discodeit.domain.user.dto.UserResult;
import com.sprint.mission.discodeit.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  @GetMapping("/csrf-token")
  public ResponseEntity<CsrfToken> getCsrfToken(CsrfToken csrfToken) {
    return ResponseEntity.ok(csrfToken);
  }

  @GetMapping("/me")
  public ResponseEntity<UserResult> getCurrentUser(
      @AuthenticationPrincipal CustomUserDetails userDetails
  ) {
    if (userDetails == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    return ResponseEntity.ok(userDetails.getUserResult());
  }

}
