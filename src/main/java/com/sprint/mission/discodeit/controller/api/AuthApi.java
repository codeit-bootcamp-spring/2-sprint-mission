package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserRoleUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Auth", description = "인증 API")
public interface AuthApi {

  @Operation(summary = "현재 사용자 Access Token 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "Access Token 조회 성공",
          content = @Content(schema = @Schema(implementation = String.class))
      ),
      @ApiResponse(
          responseCode = "401", description = "인증되지 않은 사용자",
          content = @Content(examples = @ExampleObject(value = "Unauthorized"))
      )
  })

  ResponseEntity<String> getCurrentUser(@CookieValue(value = "refresh_token", required = false) String refreshToken);

  @Operation(summary = "사용자 권한 수정", description = "관리자만 사용 가능")
  ResponseEntity<UserDto> updateUserRole(@RequestBody @Valid UserRoleUpdateRequest request);
}
