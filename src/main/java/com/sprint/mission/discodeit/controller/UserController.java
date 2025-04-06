package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.application.dto.user.UserResult;
import com.sprint.mission.discodeit.application.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.application.dto.userstatus.UserStatusResult;
import com.sprint.mission.discodeit.application.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    @PostMapping
    public ResponseEntity<UserResult> register(@Valid @RequestPart UserCreateRequest userCreateRequest,
                                               @RequestPart(required = false) MultipartFile profileImage) {
        UserResult user = userService.register(userCreateRequest, profileImage);

        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResult> getById(@PathVariable UUID userId) {
        UserResult user = userService.getById(userId);

        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserResult>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @PatchMapping(value = "/{userId}")
    public ResponseEntity<UserResult> updateUser(@PathVariable UUID userId,
                                                 @RequestPart UserUpdateRequest userUpdateRequest,
                                                 @RequestPart(required = false) MultipartFile profileImage) {
        UserResult updatedUser = userService.update(userId, userUpdateRequest, profileImage);

        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/{userId}/userStatus")
    public ResponseEntity<UserStatusResult> updateOnlineStatus(@PathVariable UUID userId, @RequestBody UserStatusUpdateRequest userStatusUpdateRequest) {
        UserStatusResult status = userStatusService.updateByUserId(userId, userStatusUpdateRequest);

        return ResponseEntity.ok(status);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable UUID userId) {
        userService.delete(userId);

        return ResponseEntity.noContent()
                .build();
    }
}
