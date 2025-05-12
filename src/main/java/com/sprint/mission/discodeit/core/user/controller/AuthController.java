package com.sprint.mission.discodeit.core.user.controller;


import static com.sprint.mission.discodeit.core.user.controller.UserDtoMapper.toLoginUserCommand;

import com.sprint.mission.discodeit.core.user.controller.dto.UserLoginRequest;
import com.sprint.mission.discodeit.core.user.usecase.UserLoginUseCase;
import com.sprint.mission.discodeit.core.user.usecase.dto.LoginUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "로그인 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

  private final UserLoginUseCase loginUseCase;

  @PostMapping("/login")
  public ResponseEntity<UserDto> login(@RequestBody UserLoginRequest requestBody) {
    LoginUserCommand command = toLoginUserCommand(requestBody);
    UserDto result = loginUseCase.login(command);
    return ResponseEntity.status(HttpStatus.OK).body(result);
  }
}
