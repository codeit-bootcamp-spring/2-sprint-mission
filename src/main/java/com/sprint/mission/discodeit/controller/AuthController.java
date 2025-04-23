package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.login.LoginForm;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/api/auth/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginForm form) {
        UserDto response = authService.login(form);

        return ResponseEntity.ok(response);
    }
}
