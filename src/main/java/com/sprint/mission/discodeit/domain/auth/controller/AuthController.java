package com.sprint.mission.discodeit.domain.auth.controller;

import com.sprint.mission.discodeit.domain.auth.dto.LoginRequest;
import com.sprint.mission.discodeit.domain.auth.service.AuthService;
import com.sprint.mission.discodeit.domain.user.dto.UserResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/csrf-token")
    public ResponseEntity<CsrfToken> getCsrfToken(CsrfToken csrfToken) {
        return ResponseEntity.ok(csrfToken);
    }

}
