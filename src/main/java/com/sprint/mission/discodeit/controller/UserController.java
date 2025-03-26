package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.jwt.JwtUtil;
import com.sprint.mission.discodeit.jwt.RequiresAuth;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;
    private final UserStatusService userStatusService;
    private final JwtUtil jwtUtil;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody CreateUserRequest request) {

        RegisterResponse response = authService.register(request);
        return ResponseEntity.ok(response);

    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @RequiresAuth
    @RequestMapping(value = "/me", method = RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequest request, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        UUID userId = UUID.fromString(jwtUtil.extractUserId(token));

        userService.updateUser(userId, request);

        return ResponseEntity.ok("회원정보 수정 완료");
    }

    @RequiresAuth
    @RequestMapping(value = "/me", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        UUID userId = UUID.fromString(token);

        userService.deleteUser(userId);
        return ResponseEntity.ok("회원 탈퇴 완료");
    }

    @RequiresAuth
    @RequestMapping(value = "/me/status", method = RequestMethod.PUT)
    public ResponseEntity<?> updateStatus(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        UUID userId = UUID.fromString(token);

        userStatusService.update(userId);

        return ResponseEntity.ok("온라인 상태 갱신 완료");
    }
}
