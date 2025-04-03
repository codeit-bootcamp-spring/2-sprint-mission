package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.dto.authdto.AuthServiceLoginResponse;
import com.sprint.mission.discodeit.service.dto.authdto.AuthServiceLoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "인증 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class LoginController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "로그인")
    @ApiResponse(responseCode = "200", description = "로그인 성공")
    @ApiResponse(responseCode = "401", description = "사용자를 찾을 수 없음", content = @Content(examples = @ExampleObject(value = "User not found")))
    @ApiResponse(responseCode = "401", description = "비밀번호가 일치하지 않음", content = @Content(examples = @ExampleObject(value = "Password does not match")))
    public ResponseEntity<AuthServiceLoginResponse> postLogin(
            @RequestBody AuthServiceLoginRequest authServiceLoginRequest
    ) {
        AuthServiceLoginResponse loginResponse = authService.login(authServiceLoginRequest);
        return ResponseEntity.ok(loginResponse);
    }
}
