package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.common.ApiResponse;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateResponse;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateResponse;
import com.sprint.mission.discodeit.entity.user.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserCreateResponse>> create(@Valid @RequestBody UserCreateRequest request) {
        UserCreateResponse response = userService.create(request);
        ApiResponse<UserCreateResponse> apiResponse = new ApiResponse<>("유저 생성 성공", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> find(@PathVariable UUID userId) {
        UserResponse response = userService.find(userId);
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>("유저 조회 성공", response);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping({"", "/"})
    public ResponseEntity<ApiResponse<List<UserResponse>>> findAll() {
        List<UserResponse> response = userService.findAll();
        ApiResponse<List<UserResponse>> apiResponse = new ApiResponse<>("유저 목록 조회 성공", response);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID userId) {
        userService.delete(userId);
        ApiResponse<Void> apiResponse = new ApiResponse<>("유저 삭제 성공", null);
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{userId}/status")
    public ResponseEntity<ApiResponse<UserStatusUpdateResponse>> updateStatus(@PathVariable UUID userId) {
        UserStatus updatedStatus = userStatusService.updateByUserId(userId);
        UserResponse userResponse = userService.find(userId);

        UserStatusUpdateResponse response = new UserStatusUpdateResponse(
                userId,
                userResponse.username(),
                userResponse.isOnline(),
                updatedStatus.getLastLoginAt()
        );

        ApiResponse<UserStatusUpdateResponse> apiResponse = new ApiResponse<>("사용자 온라인 상태 업데이트 성공", response);
        return ResponseEntity.ok(apiResponse);
    }
}
