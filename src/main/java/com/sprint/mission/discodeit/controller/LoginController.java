package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.dto.authdto.AuthServiceLoginResponse;
import com.sprint.mission.discodeit.service.dto.authdto.AuthServiceLoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class LoginController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthServiceLoginResponse> postLogin(
            @RequestBody AuthServiceLoginRequest authServiceLoginRequest
    ) {
        AuthServiceLoginResponse loginResponse = authService.login(authServiceLoginRequest);
        return ResponseEntity.ok(loginResponse);
    }
}
