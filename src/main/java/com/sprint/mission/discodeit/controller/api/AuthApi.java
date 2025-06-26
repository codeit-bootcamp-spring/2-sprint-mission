package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.data.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

@Tag(name = "Auth", description = "인증 API")
public interface AuthApi {

    @Operation(summary = "로그인")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation = UserDto.class))
            ),
            @ApiResponse(
                    responseCode = "401", description = "인증 실패 (아이디 또는 비밀번호 오류)",
                    content = @Content(
                            examples = @ExampleObject(value = "Authentication failed: Invalid credentials")
                    )
            )
    })
    ResponseEntity<UserDto> login(
            @Parameter(description = "인증 정보") Authentication authentication
    );
}