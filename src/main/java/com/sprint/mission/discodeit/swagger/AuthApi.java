package com.sprint.mission.discodeit.swagger;

import com.sprint.mission.discodeit.dto.controller.auth.LoginRequestDTO;
import com.sprint.mission.discodeit.dto.controller.auth.LoginResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Auth-Controller", description = "Authorization 관련 API")
public interface AuthApi {

  @Operation(summary = "로그인 기능",
      description = "사용자의 username과 password를 기반으로 로그인을 시도합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "로그인 성공"),
          @ApiResponse(responseCode = "400", description = "username 또는 password가 입력되지 않음 (DTO 유효성 검증 실패)"),
          @ApiResponse(responseCode = "401", description = "틀린 password를 입력함"),
          @ApiResponse(responseCode = "404", description = "username에 해당하는 유저가 존재하지 않음")
      })
  ResponseEntity<LoginResponseDTO> login(LoginRequestDTO loginRequestDTO);

}
