package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.UserApi;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController implements UserApi {

    private final UserService userService;
    private final UserStatusService userStatusService;

    @Override
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> create(
            @RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
            @RequestPart(value = "profile", required = false) MultipartFile profile) {

        log.info("Request to create user: username = {}", userCreateRequest.username());

        if (profile != null && !profile.isEmpty()) {
            log.info("Uploading profile image: filename = {}", profile.getOriginalFilename());
            log.debug("Profile image size = {} bytes", profile.getSize());
        }

        Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
                .flatMap(this::resolveProfileRequest);

        UserDto createdUser = userService.create(userCreateRequest, profileRequest);

        log.info("User created successfully: id = {}", createdUser.id());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        List<UserDto> users = userService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @Override
    @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> update(
            @PathVariable UUID userId,
            @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,
            @RequestPart(value = "profile", required = false) MultipartFile profile) {

        log.info("Updating user: id = {}", userId);

        if (profile != null && !profile.isEmpty()) {
            log.info("Uploading profile image: filename = {}", profile.getOriginalFilename());
            log.debug("Profile image size = {} bytes", profile.getSize());
        }

        Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
                .flatMap(this::resolveProfileRequest);

        UserDto updatedUser = userService.update(userId, userUpdateRequest, profileRequest);

        log.info("User updated successfully: id = {}", userId);

        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    @Override
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable("userId") UUID userId) {
        log.info("Deleting user: id = {}", userId);

        userService.delete(userId);

        log.info("User deleted successfully: id = {}", userId);

        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/{userId}/userStatus")
    public ResponseEntity<UserStatusDto> updateUserStatusByUserId(
            @PathVariable("userId") UUID userId,
            @RequestBody UserStatusUpdateRequest request) {

        log.info("Updating user status: userId = {}", userId);

        UserStatusDto userStatus = userStatusService.updateByUserId(userId, request);

        log.info("User status updated: userId = {}", userId);

        return ResponseEntity.status(HttpStatus.OK).body(userStatus);

    }

    private Optional<BinaryContentCreateRequest> resolveProfileRequest(MultipartFile profileFile) {
        if (profileFile == null || profileFile.isEmpty()) {
            return Optional.empty();
        } else {
            try {
                BinaryContentCreateRequest request = new BinaryContentCreateRequest(
                        profileFile.getOriginalFilename(),
                        profileFile.getContentType(),
                        profileFile.getBytes()
                );
                return Optional.of(request);
            } catch (IOException e) {
                log.error("Failed to parse profile image", e);
                throw new RuntimeException(e);
            }
        }
    }
}
