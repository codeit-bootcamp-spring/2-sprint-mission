package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.LoginRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;
  private final UserService userService;

  @PostMapping("/login")
  public ResponseEntity<UserResponse> login(@RequestBody LoginRequest request) {
    try {
      UserResponse userResponse = authService.login(request);
      return ResponseEntity.ok(userResponse);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }
}
