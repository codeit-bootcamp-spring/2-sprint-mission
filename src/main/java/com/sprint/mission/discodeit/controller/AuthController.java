package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.dto.LoginDto;
import com.sprint.mission.discodeit.service.dto.UserResponseDto;
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
    public ResponseEntity<UserResponseDto> login(@RequestBody LoginDto loginDto) {
        UserResponseDto response = authService.login(loginDto);
        return ResponseEntity.ok(response);
    }
}
