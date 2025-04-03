package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelFindResponse;
import com.sprint.mission.discodeit.dto.common.ApiResponse;
import com.sprint.mission.discodeit.dto.user.*;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateResponse;
import com.sprint.mission.discodeit.entity.common.ReadStatus;
import com.sprint.mission.discodeit.entity.user.UserStatus;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
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
    private final ChannelService channelService;
    private final ReadStatusService readStatusService;

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

    @GetMapping({"", "/","/findAll"})
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

    @PutMapping
    public ResponseEntity<ApiResponse<UserUpdateResponse>> update(@Valid @RequestBody UserUpdateRequest request) {
        UserUpdateResponse response = userService.update(request);
        ApiResponse<UserUpdateResponse> apiResponse = new ApiResponse<>("유저 수정 성공", response);
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

    @GetMapping("/{userId}/channels")
    public ResponseEntity<ApiResponse<List<ChannelFindResponse>>> findAllChannelByUser(@PathVariable UUID userId) {
        List<ChannelFindResponse> response = channelService.findAllByUserId(userId);
        ApiResponse<List<ChannelFindResponse>> apiResponse = new ApiResponse<>("유저 채널 목록 조회 성공", response);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{userId}/readStatuses")
    public ResponseEntity<ApiResponse<List<ReadStatus>>> findAllByUser(@PathVariable UUID userId) {
        List<ReadStatus> statuses = readStatusService.findAllByUserId(userId);
        ApiResponse<List<ReadStatus>> response = new ApiResponse<>("사용자 메시지 읽음 상태 리스트 조회 성공", statuses);
        return ResponseEntity.ok(response);
    }
}
