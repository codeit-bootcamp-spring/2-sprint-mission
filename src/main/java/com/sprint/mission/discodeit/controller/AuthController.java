package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.dto.user.UserIdResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.dto.user.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login")
public class AuthController {
    private final AuthService authService;

    // 로그인
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<UserIdResponse> login(@RequestBody LoginRequest request) {
        User user = authService.login(request);
        UserIdResponse response = new UserIdResponse(true, user.getId());
        return ResponseEntity.ok(response);
    }
}
