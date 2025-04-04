package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.application.dto.user.UserResult;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping
  public ResponseEntity<UserResult> login(@Valid @RequestBody LoginRequest loginRequest) {
    UserResult userResult = authService.login(loginRequest);

    return ResponseEntity.ok(userResult);
  }
}
