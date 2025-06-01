package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.aop.LogMasking;
import com.sprint.mission.discodeit.dto.controller.auth.LoginRequestDTO;
import com.sprint.mission.discodeit.dto.controller.auth.LoginResponseDTO;
import com.sprint.mission.discodeit.dto.service.auth.LoginCommand;
import com.sprint.mission.discodeit.dto.service.auth.LoginResult;
import com.sprint.mission.discodeit.mapper.AuthMapper;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.swagger.AuthApi;
import com.sprint.mission.discodeit.util.MaskingUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class AuthController implements AuthApi {

  private final AuthService authService;
  private final AuthMapper authMapper;


  @Override
  @LogMasking
  @PostMapping("/login")
  public ResponseEntity<LoginResponseDTO> login(
      @RequestBody @Valid LoginRequestDTO loginRequest) {
    LoginCommand loginCommand = authMapper.toLoginCommand(loginRequest);
    LoginResult loginResult = authService.login(loginCommand);
    LoginResponseDTO user = authMapper.toLoginResponseDTO(loginResult);

    return ResponseEntity.ok(user);
  }
}
