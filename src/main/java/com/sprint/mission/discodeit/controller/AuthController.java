package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.auth.request.UserLoginDto;
import com.sprint.mission.discodeit.dto.auth.reslonse.UserLoginResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<UserLoginResponse> login(@RequestBody @Validated UserLoginDto userLoginDto){
        authService.login(userLoginDto);
        return ResponseEntity.ok(new UserLoginResponse(true, "로그인 성공"));
    }

}
