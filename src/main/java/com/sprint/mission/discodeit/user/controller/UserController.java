package com.sprint.mission.discodeit.user.controller;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.user.dto.UserResult;
import com.sprint.mission.discodeit.user.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.user.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.user.service.UserService;
import com.sprint.mission.discodeit.userstatus.dto.UserStatusResult;
import com.sprint.mission.discodeit.userstatus.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.userstatus.service.UserStatusService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResult> register(@Valid @RequestPart UserCreateRequest userCreateRequest, @RequestPart(required = false) MultipartFile profileImage) {

        BinaryContentRequest binaryContentRequest = getBinaryContentRequest(profileImage);
        UserResult user = userService.register(userCreateRequest, binaryContentRequest);

        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResult> getById(@PathVariable UUID userId) {
        UserResult user = userService.getById(userId);

        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserResult>> getAll() {
        return ResponseEntity.ok(userService.getAllIn());
    }

    @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResult> updateUser(@PathVariable UUID userId, @RequestPart UserUpdateRequest userUpdateRequest, @RequestPart(required = false) MultipartFile profileImage) {

        BinaryContentRequest binaryContentRequest = getBinaryContentRequest(profileImage);
        UserResult updatedUser = userService.update(userId, userUpdateRequest, binaryContentRequest);

        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/{userId}/userStatus")
    public ResponseEntity<UserStatusResult> updateOnlineStatus(@PathVariable UUID userId) {
        UserStatusResult status = userStatusService.updateByUserId(userId, Instant.now());

        return ResponseEntity.ok(status);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable UUID userId) {
        userService.delete(userId);

        return ResponseEntity.noContent()
                .build();
    }

    private BinaryContentRequest getBinaryContentRequest(MultipartFile profileImage) {
        if (profileImage == null) {
            return null;
        }
        return BinaryContentRequest.fromMultipartFile(profileImage);
    }

}
