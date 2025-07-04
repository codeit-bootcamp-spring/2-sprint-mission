package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.AuthApi;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

  @GetMapping(path = "/csrf-token")
  public ResponseEntity<CsrfToken> csrf(CsrfToken csrfToken) {
    return ResponseEntity.status(HttpStatus.OK).body(csrfToken);
  }

  @GetMapping("/me")
  public ResponseEntity<UserDto> getCurrentUser(HttpSession session) {
    UserDto userDto = (UserDto) session.getAttribute("user");
    if (userDto == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    return ResponseEntity.ok(userDto);
  }
}
