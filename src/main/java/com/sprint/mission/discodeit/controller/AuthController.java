package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.dto.UserResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.dto.user.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "인증 API")
public class AuthController {

  private final AuthService authService;

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
  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public ResponseEntity<UserResponse> login(@RequestBody LoginRequest request) {
    User user = authService.login(request);
    return ResponseEntity.ok(UserResponse.of(user));
  }
}
