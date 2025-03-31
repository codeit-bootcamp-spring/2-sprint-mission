package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.dto.user.UserRequest;
import com.sprint.mission.discodeit.application.dto.user.UserResponse;
import com.sprint.mission.discodeit.application.dto.user.UserResult;
import com.sprint.mission.discodeit.application.dto.userstatus.UserStatusResult;
import com.sprint.mission.discodeit.service.BinaryContentService;
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
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final BinaryContentService binaryContentService;
    private final UserStatusService userStatusService;

    @PostMapping("/register")
    public ResponseEntity<UserResult> register(@Valid @RequestPart UserRequest userRequest,
                                               @RequestPart(required = false) MultipartFile multipartFile) {
        UUID profileId = binaryContentService.createProfileImage(multipartFile);

        return ResponseEntity.ok(userService.register(userRequest, profileId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getById(@PathVariable UUID userId) {
        UserResult userResult = userService.getById(userId);
        UserStatusResult userStatusDto = userStatusService.getByUserId(userResult.id());

        return ResponseEntity.ok(UserResponse.of(userResult, userStatusDto.isLogin()));
    }

    @GetMapping
    public ResponseEntity<List<UserResult>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResult> updateUser(@PathVariable UUID userId,
                                                 @RequestBody UserRequest userRequest) {
        UserResult updatedUser = userService.updateName(userId, userRequest.name());
        return ResponseEntity.ok(updatedUser);
    }


    @PutMapping("/{userId}/profile-image")
    public ResponseEntity<UserResult> updateProfileImage(@PathVariable UUID userId,
                                                         @RequestPart MultipartFile profileImage) {
        UserResult beforeUpdatedUser = userService.getById(userId);
        UUID profileId = binaryContentService.createProfileImage(profileImage);
        UserResult user = userService.updateProfileImage(userId, profileId);
        binaryContentService.delete(beforeUpdatedUser.profileId());
        if (beforeUpdatedUser.profileId() != null) {
            binaryContentService.delete(beforeUpdatedUser.profileId());
        }
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}/status")
    public ResponseEntity<UserStatusResult> updateOnlineStatus(@PathVariable UUID userId) {
        UserStatusResult status = userStatusService.updateByUserId(userId);

        return ResponseEntity.ok(status);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable UUID userId) {
        UserResult beforeUpdatedUser = userService.getById(userId);
        userService.delete(userId);
        binaryContentService.delete(beforeUpdatedUser.profileId());

        return ResponseEntity.ok().build();
    }
}
