package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.AuthApi;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.dto.service.user.LoginRequest;
import com.sprint.mission.discodeit.dto.service.user.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController implements AuthApi {

  private final AuthService authService;

  // 로그인
  @Override
  @PostMapping("/login")
  public ResponseEntity<UserDto> login(@RequestBody @Valid LoginRequest request) {
    UserDto response = authService.login(request);
    return ResponseEntity.ok(response);
  }
}