package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.login.LoginForm;
import com.sprint.mission.discodeit.entity.User;
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
    public ResponseEntity<User> login(@RequestBody LoginForm form) {
        User user = authService.login(form);

        return ResponseEntity.ok(user);
    }
}
