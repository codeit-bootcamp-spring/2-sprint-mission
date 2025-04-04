package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ApiDataResponse;
import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 API")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  @Operation(
      summary = "로그인"
  )
  @io.swagger.v3.oas.annotations.parameters.RequestBody(
      content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = LoginRequest.class)
      ),
      required = true
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "로그인 성공",
          content = @Content(
              schema = @Schema(implementation = User.class)
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "사용자를 찾을 수 없음",
          content = @Content(
              examples = @ExampleObject("User with username {username} not found")
          )
      ),
      @ApiResponse(
          responseCode = "400",
          description = "비밀번호가 일치하지 않음",
          content = @Content(
              examples = @ExampleObject("Wrong password")
          )
      )
  })
  public ResponseEntity<ApiDataResponse<Void>> login(
      @RequestBody() LoginRequest loginRequest
  ) {
    authService.login(loginRequest);
    return ResponseEntity.status(HttpStatus.OK).body(ApiDataResponse.success());
  }
}
