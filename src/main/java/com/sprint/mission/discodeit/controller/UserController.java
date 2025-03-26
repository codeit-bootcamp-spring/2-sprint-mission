package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.common.ApiResponse;
import com.sprint.mission.discodeit.dto.user.*;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserCreateResponse>> register(@Valid @RequestBody UserCreateRequest request) {
        UUID userId = userService.createUser(request);
        ApiResponse<UserCreateResponse> response = new ApiResponse<>(true, "유저 생성 성공", new UserCreateResponse(userId));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<UserUpdateResponse>> update(@Valid @RequestBody UserUpdateRequest request) {
        userService.updateUser(request);
        ApiResponse<UserUpdateResponse> response = new ApiResponse<>(true, "유저 업데이트 성공", new UserUpdateResponse(request.userId()));
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/delete")
    public ResponseEntity<ApiResponse<UserDeleteResponse>> delete(@Valid @RequestBody UserDeleteRequest request) {
        userService.deleteUser(request.userId());
        ApiResponse<UserDeleteResponse> response = new ApiResponse<>(true, "유저 삭제 성공", new UserDeleteResponse(request.userId(), Instant.now()));
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

}
