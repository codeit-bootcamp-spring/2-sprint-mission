package com.sprint.mission.discodeit.adapter.inbound.user;


import static com.sprint.mission.discodeit.adapter.inbound.user.UserDtoMapper.toLoginUserCommand;

import com.sprint.mission.discodeit.adapter.inbound.user.request.UserLoginRequest;
import com.sprint.mission.discodeit.adapter.inbound.user.response.UserResponse;
import com.sprint.mission.discodeit.core.user.usecase.UserLoginUseCase;
import com.sprint.mission.discodeit.core.user.usecase.dto.LoginUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
  public ResponseEntity<UserResponse> login(@RequestBody UserLoginRequest requestBody) {
    LoginUserCommand command = toLoginUserCommand(requestBody);
    UserResult result = loginUseCase.login(command);
    //TODO. service 반환값 수정해야함
    return ResponseEntity.ok((result));
  }

}
