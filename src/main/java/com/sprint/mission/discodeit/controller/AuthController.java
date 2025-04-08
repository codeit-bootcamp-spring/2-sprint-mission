package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.LoginResponse;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

  private final UserService userService;
  private final AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<UserDto.Response> registerUser(@RequestBody UserDto.Create userDto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.createdUser(userDto));
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody UserDto.Login loginDto) {
    String token = authService.login(loginDto);
    return ResponseEntity.ok(new LoginResponse(loginDto.getEmail(), "true", token));
  }
}
