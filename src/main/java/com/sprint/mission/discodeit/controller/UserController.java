package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.application.dto.user.UserResult;
import com.sprint.mission.discodeit.application.dto.userstatus.UserStatusResult;
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
    public ResponseEntity<UserResult> register(@Valid @RequestPart UserCreateRequest userRequest,
                                               @RequestPart(required = false) MultipartFile profileImage) {
        UserResult user = userService.register(userRequest, profileImage);

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

    @PutMapping(value = "/{userId}")
    public ResponseEntity<UserResult> updateUserName(@PathVariable UUID userId,
                                                     @RequestBody UserCreateRequest userRequest) {
        UserResult updatedUser = userService.updateName(userId, userRequest.name());

        return ResponseEntity.ok(updatedUser);
    }


    @PutMapping("/{userId}/profile-image")
    public ResponseEntity<UserResult> updateProfileImage(@PathVariable UUID userId,
                                                         @RequestPart MultipartFile profileImage) {
        UserResult user = userService.updateProfileImage(userId, profileImage);

        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}/status")
    public ResponseEntity<UserStatusResult> updateOnlineStatus(@PathVariable UUID userId) {
        UserStatusResult status = userStatusService.updateByUserId(userId);

        return ResponseEntity.ok(status);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable UUID userId) {
        userService.delete(userId);

        return ResponseEntity.noContent()
                .build();
    }
}
