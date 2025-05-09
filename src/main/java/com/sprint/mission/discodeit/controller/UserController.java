package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.UserApi;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.validation.Valid;
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

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController implements UserApi {

  private final UserService userService;
  private final UserStatusService userStatusService;
  private final UserMapper userMapper;

  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  @Override
  public ResponseEntity<UserDto> create(
          @Valid @RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
          @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    log.info("▶▶ [API] Received user creation request - username: {}, email: {}", userCreateRequest.username(), userCreateRequest.email());
    Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
            .flatMap(this::resolveProfileRequest);
    UserDto createdUser = userService.create(userCreateRequest, profileRequest);
    log.info("◀◀ [API] User created successfully - username: {}, email: {}", userCreateRequest.username(), userCreateRequest.email());
    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(createdUser);
  }

  @PatchMapping(
          path = "{userId}",
          consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
  )
  @Override
  public ResponseEntity<UserDto> update(
          @PathVariable("userId") UUID userId,
          @Valid @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,
          @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    log.info("▶▶ [API] Received user update request - id: {}", userId);
    Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
            .flatMap(this::resolveProfileRequest);
    UserDto updatedUser = userService.update(userId, userUpdateRequest, profileRequest);
    log.info("◀◀ [API] User updated successfully - id: {}", userId);
    return ResponseEntity
            .status(HttpStatus.OK)
            .body(updatedUser);
  }

  @DeleteMapping(path = "{userId}")
  @Override
  public ResponseEntity<Void> delete(@PathVariable("userId") UUID userId) {
    log.info("▶▶ [API] Received user deletion request - id: {}", userId);
    userService.delete(userId);
    log.info("◀◀ [API] User deleted successfully - id: {}", userId);
    return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
  }

  @GetMapping
  @Override
  public ResponseEntity<List<UserDto>> findAll() {
    log.info("▶▶ [API] Received request to find all users");
    List<UserDto> users = userService.findAll();
    log.info("◀◀ [API] Returning all users - count: {}", users.size());
    return ResponseEntity
            .status(HttpStatus.OK)
            .body(users);
  }

  @PatchMapping(path = "{userId}/userStatus")
  @Override
  public ResponseEntity<UserStatusDto> updateUserStatusByUserId(@PathVariable("userId") UUID userId,
                                                                @Valid @RequestBody UserStatusUpdateRequest request) {
    log.info("▶▶ [API] Received user status update request - userId: {}", userId);
    UserStatusDto updatedUserStatus = userStatusService.updateByUserId(userId, request);
    log.info("◀◀ [API] User status updated successfully - userId: {}", userId);
    return ResponseEntity
            .status(HttpStatus.OK)
            .body(updatedUserStatus);
  }

  private Optional<BinaryContentCreateRequest> resolveProfileRequest(MultipartFile profileFile) {
    if (profileFile.isEmpty()) {
      log.debug("▶▶ [API] Empty profile file received, skipping profile creation");
      return Optional.empty();
    } else {
      try {
        BinaryContentCreateRequest binaryContentCreateRequest = new BinaryContentCreateRequest(
                profileFile.getOriginalFilename(),
                profileFile.getContentType(),
                profileFile.getBytes()
        );
        log.debug("▶▶ [API] Profile file resolved for binary content creation - fileName: {}", profileFile.getOriginalFilename());
        return Optional.of(binaryContentCreateRequest);
      } catch (IOException e) {
        log.error("◀◀ [API] Failed to resolve profile file for binary content creation", e);
        throw new RuntimeException(e);
      }
    }
  }
}