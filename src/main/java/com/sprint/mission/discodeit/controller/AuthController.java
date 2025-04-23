package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.Api.AuthApi;
import com.sprint.mission.discodeit.service.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;

@RequiredArgsConstructor
@Controller
@ResponseBody
//RequestMapping("/api/auth")
public class AuthController implements AuthApi {

  private final AuthService authService;
  private final UserMapper userMapper;

  @Override
  public Optional<NativeWebRequest> getRequest() {
    return AuthApi.super.getRequest();
  }

  @Override
  public ResponseEntity<UserDto> login(LoginRequest loginRequest) {
    User user = authService.login(loginRequest);

    UserDto dto = userMapper.toDto(user);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(dto);
  }

  /*@RequestMapping(path = "login")
  public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest) {
    User user = authService.login(loginRequest);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(user);
  }*/

}
