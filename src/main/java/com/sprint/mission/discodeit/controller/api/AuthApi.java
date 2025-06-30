package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;

@Tag(name = "Auth", description = "인증 API")
public interface AuthApi {

    @Operation(summary = "CSRF 토큰 발급")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "CSRF 토큰 발급 성공")
    })
    ResponseEntity<CsrfToken> getCsrfToken(CsrfToken csrfToken);

    @Operation(summary = "현재 로그인한 사용자 정보 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "현재 사용자 정보 조회 성공"),
            @ApiResponse(responseCode = "401", description = "비로그인 상태 또는 인증 실패")
    })
    ResponseEntity<UserDto> me(@Parameter(description = "현재 인증 정보") Authentication authentication);

    @Operation(summary = "사용자 권한 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 권한 수정 성공")
    })
    ResponseEntity<UserDto> updateUserRole(
            @Parameter(description = "수정할 사용자 권한 정보") RoleUpdateRequest request);
}
