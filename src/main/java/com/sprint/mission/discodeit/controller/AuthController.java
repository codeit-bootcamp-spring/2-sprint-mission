package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.controller.auth.LoginRequestDTO;
import com.sprint.mission.discodeit.dto.controller.auth.LoginResponseDTO;
import com.sprint.mission.discodeit.dto.service.auth.LoginDTO;
import com.sprint.mission.discodeit.dto.service.auth.LoginParam;
import com.sprint.mission.discodeit.mapper.AuthMapper;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.swagger.AuthApi;
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
  private final AuthMapper authMapper;


  @Override
  @PostMapping("/login")
  public ResponseEntity<LoginResponseDTO> login(
      @RequestBody @Valid LoginRequestDTO loginRequestDTO) {
    LoginParam loginParam = authMapper.toLoginParam(loginRequestDTO);
    LoginDTO loginDTO = authService.login(loginParam);
    LoginResponseDTO user = authMapper.toLoginResponseDTO(loginDTO);

    return ResponseEntity.ok(user);
  }

}
