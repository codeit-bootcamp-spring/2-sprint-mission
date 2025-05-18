package com.sprint.mission.discodeit.auth.controller;

import com.sprint.mission.discodeit.auth.dto.LoginRequest;
import com.sprint.mission.discodeit.auth.service.AuthService;
import com.sprint.mission.discodeit.user.dto.UserResult;
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
    public ResponseEntity<UserResult> login(@RequestBody @Valid LoginRequest loginRequest) {
        UserResult userResult = authService.login(loginRequest);

        return ResponseEntity.ok(userResult);
    }

}
