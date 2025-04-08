package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.user.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "인증 API")
public class AuthController {

  private final AuthService authService;
  private final UserService userService;

  @Operation(summary = "로그인")
  @PostMapping("/login")
  public ResponseEntity<UserDto> userLogin(@RequestBody LoginRequest request) {
    System.out.println("로그인 Api가 요청 됐습니다.");
    User loginUser = authService.login(request);
    UserDto userDto = userService.find(loginUser.getId());

    return ResponseEntity.ok(userDto);
  }
}


