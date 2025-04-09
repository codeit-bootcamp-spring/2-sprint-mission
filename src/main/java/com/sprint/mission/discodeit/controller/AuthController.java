package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.controller.auth.LoginRequestDTO;
import com.sprint.mission.discodeit.dto.controller.auth.LoginResponseDTO;
import com.sprint.mission.discodeit.dto.service.auth.LoginDTO;
import com.sprint.mission.discodeit.dto.service.auth.LoginParam;
import com.sprint.mission.discodeit.dto.service.user.UserDTO;
import com.sprint.mission.discodeit.mapper.AuthMapper;
import com.sprint.mission.discodeit.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth-Controller", description = "Authorization 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;
  private final AuthMapper authMapper;

  @Operation(summary = "로그인 기능",
      description = "사용자의 username과 password를 기반으로 로그인을 시도합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "로그인 성공"),
          @ApiResponse(responseCode = "400", description = "username 또는 password가 입력되지 않음 (DTO 유효성 검증 실패)"),
          @ApiResponse(responseCode = "401", description = "틀린 password를 입력함"),
          @ApiResponse(responseCode = "404", description = "username에 해당하는 유저가 존재하지 않음")
      })
  @PostMapping("/login")
  public ResponseEntity<LoginResponseDTO> login(
      @RequestBody @Valid LoginRequestDTO loginRequestDTO) {
    LoginParam loginParam = authMapper.toLoginParam(loginRequestDTO);
    LoginDTO loginDTO = authService.login(loginParam);
    LoginResponseDTO user = authMapper.toLoginResponseDTO(loginDTO);

    return ResponseEntity.ok(user);
  }

}
