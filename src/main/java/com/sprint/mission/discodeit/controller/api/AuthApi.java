package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.controller.dto.UserResponse;
import com.sprint.mission.discodeit.service.dto.user.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Auth", description = "인증 API")
public interface AuthApi {

  // 로그인
  @Operation(summary = "로그인")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "로그인 성공",
          content = @Content(mediaType = "*/*", schema = @Schema(implementation = UserResponse.class))),
      @ApiResponse(responseCode = "400", description = "비밀번호가 일치하지 않음",
          content = @Content(mediaType = "*/*", examples = {
              @ExampleObject(value = "비밀번호가 일치하지 않음")
          })),
      @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
          content = @Content(mediaType = "*/*", examples = {
              @ExampleObject(value = "{username}에 해당하는 User가 없음")
          }))
  })
  ResponseEntity<UserResponse> login(LoginRequest request);
}
