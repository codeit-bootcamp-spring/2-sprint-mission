package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.auth.AuthLoginRequest;
import com.sprint.mission.discodeit.dto.auth.AuthLoginResponse;
import com.sprint.mission.discodeit.dto.common.ApiResponse;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthLoginResponse>> login(@Valid @RequestBody AuthLoginRequest request) {
        AuthLoginResponse response = authService.login(request);
        ApiResponse<AuthLoginResponse> apiResponse = new ApiResponse<>("로그인 성공", response);
        return ResponseEntity.ok().body(apiResponse);
    }
}
