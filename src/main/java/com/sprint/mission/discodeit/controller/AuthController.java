package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.application.dto.user.UserResponse;
import com.sprint.mission.discodeit.application.dto.user.UserResult;
import com.sprint.mission.discodeit.application.dto.userstatus.UserStatusResult;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserStatusService userStatusService;

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        UserResult userResult = authService.login(loginRequest);
        UserStatusResult userStatus = userStatusService.getByUserId(userResult.id());
        UserResponse response = UserResponse.of(userResult, userStatus.isLogin());
        return ResponseEntity.ok(response);
    }
}
