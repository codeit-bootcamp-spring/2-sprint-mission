package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.user.LoginRequest;
import com.sprint.mission.discodeit.dto.request.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.user.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.response.user.UserCreateResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    // 사용자 등록 (회원가입)
    @PostMapping
    public ResponseEntity<UserCreateResponse> createUser(@RequestBody UserCreateRequest request) {
        System.out.println("회원가입 API 요청 들어옴.");
        return ResponseEntity.ok(userService.register(request, Optional.empty()));
    }

    // 사용자 정보 수정, @PathVariable - URL경로 일부 {}
    // Post(생성), Get(조회), Put(수정), Delete(삭제)
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable UUID userId, @RequestBody UserUpdateRequest request) {
        System.out.println("사용자 정보 수정 API 요청 들어옴.");
        User updatedUser = userService.update(userId, request, Optional.empty());
        return ResponseEntity.ok(updatedUser);
    }

    // 사용자 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID userId) {
        System.out.println("사용자 삭제 API 요청 들어옴.");
        userService.delete(userId);
        return ResponseEntity.ok("delete success");
    }

    // 모든 사용자 조회
    @GetMapping("/findAll")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        System.out.println("모든 사용자 조회 API 요청 들어옴.");
        List<UserDto> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    // 사용자 온라인 상태 업데이트, Patch로 리소스의 일부분만 업데이트
    // 프론트가 있어야 UserStatusUpate가 가능하지 않나? API 도구로 현재 시간 전달 불가능/불편
    @PatchMapping("/{userId}/status")
    public ResponseEntity<String> updateUserStatus(
            @PathVariable UUID userId,
            @RequestBody boolean isOnline
    ) {
        System.out.println("사용자 온라인 상태 업데이트 API 요청 들어옴.");
        Instant newLastActiveAt = isOnline ? Instant.now() : null;

        UserStatusUpdateRequest request = new UserStatusUpdateRequest(newLastActiveAt);
        userStatusService.updateByUserId(userId, request);

        return ResponseEntity.ok("온라인 상태 업데이트 성공");
    }
}
