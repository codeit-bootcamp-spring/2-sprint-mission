package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.dto.LoginRequest;
import com.sprint.mission.discodeit.controller.dto.ApiUser;
import com.sprint.mission.discodeit.service.AuthService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;

@RequiredArgsConstructor
@Controller
@ResponseBody
@RequestMapping("/api/auth")
public class AuthController implements AuthApi {

  private final AuthService authService;

  @Override
  public Optional<NativeWebRequest> getRequest() {
    return AuthApi.super.getRequest();
  }

  @Override
  public ResponseEntity<ApiUser> login(LoginRequest loginRequest) {
    authService.login(loginRequest);
    return AuthApi.super.login(loginRequest);
  }

  /*@RequestMapping(path = "login")
  public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest) {
    User user = authService.login(loginRequest);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(user);
  }*/

}
