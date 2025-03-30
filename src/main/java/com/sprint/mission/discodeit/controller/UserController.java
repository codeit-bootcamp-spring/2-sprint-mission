package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.common.ApiResponse;
import com.sprint.mission.discodeit.dto.user.*;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateByUserIdRequest;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserCreateResponse>> register(@Valid @RequestBody UserCreateRequest request) {
        UUID userId = userService.createUser(request);
        ApiResponse<UserCreateResponse> response = new ApiResponse<>(true, "유저 생성 성공", new UserCreateResponse(userId));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<ApiResponse<UserUpdateResponse>> updateProfile(@PathVariable UUID id, @Valid @RequestBody UserUpdateRequest request) {
        userService.updateUser(id, request);
        ApiResponse<UserUpdateResponse> response = new ApiResponse<>(true, "유저 업데이트 성공", new UserUpdateResponse(id));
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDeleteResponse>> delete(@PathVariable UUID id) {
        userService.deleteUser(id);
        ApiResponse<UserDeleteResponse> response = new ApiResponse<>(true, "유저 삭제 성공", new UserDeleteResponse(id, Instant.now()));
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserReadResponse>> read(@PathVariable UUID id) {
        UserReadResponse readResponse = userService.readUser(id);
        ApiResponse<UserReadResponse> response = new ApiResponse<>(true, "유저 읽기 성공", readResponse);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/finaAll")
    public ResponseEntity<List<UserReadResponse>> readAll() {       // 미션 요구사항때문에 이 api의 반환값만 ApiResponse를 사용안함.
        List<UserReadResponse> response = userService.readAllUsers();
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Void>> updateStatus(@PathVariable UUID id, @Valid @RequestBody UserStatusUpdateByUserIdRequest request) {
        userStatusService.updateUserStatusByUserId(id, request);
        ApiResponse<Void> response = new ApiResponse<>(true, "유저 상태 업데이트 성공", null);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}
