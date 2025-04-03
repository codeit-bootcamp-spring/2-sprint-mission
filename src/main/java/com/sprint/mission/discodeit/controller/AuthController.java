package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ApiDataResponse;
import com.sprint.mission.discodeit.dto.LoginRequestDto;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<ApiDataResponse<Void>> login(
      @RequestBody LoginRequestDto loginRequestDto
  ) {
    authService.login(loginRequestDto);
    return ResponseEntity.ok(ApiDataResponse.success());
  }

}
