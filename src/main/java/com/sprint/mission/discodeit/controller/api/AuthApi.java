package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;

@Tag(name = "Auth", description = "인증 API")
public interface AuthApi {

  @Operation(summary = "사용자 권한 수정")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "권한 수정 성공",
          content = @Content(schema = @Schema(implementation = UserDto.class))
      ),
      @ApiResponse(
          responseCode = "400", description = "잘못된 요청 형식 또는 권한 없음",
          content = @Content(examples = @ExampleObject(value = "Invalid role or insufficient permission"))
      )
  })
  ResponseEntity<UserDto> updateUserRole(
      @Parameter(description = "수정할 권한 정보") @Valid RoleUpdateRequest request
  );

  @Operation(summary = "CSRF 토큰 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "CSRF 토큰 조회 성공",
          content = @Content(schema = @Schema(implementation = CsrfToken.class))
      ),
      @ApiResponse(
          responseCode = "500", description = "CSRF 토큰 생성 실패",
          content = @Content(examples = @ExampleObject(value = "Could not generate CSRF token"))
      )
  })
  ResponseEntity<CsrfToken> getCsrfToken(HttpServletRequest request);

  @Operation(summary = "세션ID로 사용자 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "현재 로그인한 사용자 정보 반환",
          content = @Content(schema = @Schema(implementation = UserDto.class))
      ),
      @ApiResponse(
          responseCode = "401", description = "인증되지 않음",
          content = @Content(examples = @ExampleObject(value = "Unauthorized"))
      )
  })
  UserDto me(
      @Parameter(hidden = true) CustomUserDetails principal
  );

}