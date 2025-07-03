package com.sprint.mission.discodeit.controller.swagger;

import com.sprint.mission.discodeit.dto.auth.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Auth", description = "인증 API")
public interface AuthApi {

    @Operation(summary = "Csrf-Token 조회", operationId = "find_token")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "토큰 조회 성공",
            content = @Content(
                mediaType = "*/*",
                schema = @Schema(implementation = CsrfToken.class)
            )
        )
    })
    ResponseEntity<CsrfToken> getCsrfToken(
        CsrfToken csrfToken
    );

    @Operation(summary = "자동 로그인", operationId = "find_me")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "자동 로그인 성공",
            content = @Content(
                mediaType = "*/*",
                schema = @Schema(implementation = UserDto.class)
            )
        )
    })
    ResponseEntity<UserDto> getCurrentUser(
        @AuthenticationPrincipal DiscodeitUserDetails userDetails
    );

    @Operation(summary = "사용자 권한 수정", operationId = "update_role")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "자사용자 권한 변경 성공",
            content = @Content(
                mediaType = "*/*",
                schema = @Schema(implementation = UserDto.class)
            )

        )
    })
    ResponseEntity<UserDto> updateRole(
        @RequestBody RoleUpdateRequest roleUpdateRequest
    );
}
