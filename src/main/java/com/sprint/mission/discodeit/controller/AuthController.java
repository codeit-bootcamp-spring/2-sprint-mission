package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.DTO.User.LoginRequest;
import com.sprint.mission.discodeit.DTO.User.UserDto;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public UserDto login(@RequestBody LoginRequest request) {
        return authService.login(request.email(), request.password());
    }
}
