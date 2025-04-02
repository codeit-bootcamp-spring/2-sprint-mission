package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@ResponseBody
@RequestMapping("/api/user")
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @PostMapping(
      path = "/api/users",
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
  )
  public ResponseEntity<UserDto> createUser(
      @RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
        .flatMap(this::resolveProfileRequest);
    User createdUser = userService.create(userCreateRequest, profileRequest);
    UserDto userDto = UserDto.from(createdUser);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(userDto);
  }

  @PostMapping(
      path = "/api/users/{userId}",
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
  )
  public ResponseEntity<UserDto> updateUser(
      @PathVariable UUID userId,
      @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
        .flatMap(this::resolveProfileRequest);

    User updatedUser = userService.update(userId, userUpdateRequest, profileRequest);
    UserDto userDto = UserDto.from(updatedUser);

    return ResponseEntity.ok(userDto);
  }

  @DeleteMapping("/api/users/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
    userService.delete(userId);
    return ResponseEntity.noContent().build(); // 204 No Content
  }

  @GetMapping("/api/users")
  public ResponseEntity<List<UserDto>> getAllUsers() {
    List<UserDto> users = userService.findAll();
    return ResponseEntity.ok(users);
  }

  @PatchMapping("/api/users/{userId}/userStatus")
  public ResponseEntity<UserStatus> updateUserStatus(
      @PathVariable UUID userId,
      @RequestBody UserStatusUpdateRequest request
  ) {
    UserStatus updatedUserStatus = userStatusService.updateByUserId(userId, request);
    return ResponseEntity.ok(updatedUserStatus);
  }

  private Optional<BinaryContentCreateRequest> resolveProfileRequest(MultipartFile profileFile) {
    if (profileFile.isEmpty()) {
      return Optional.empty();
    } else {
      try {
        BinaryContentCreateRequest binaryContentCreateRequest = new BinaryContentCreateRequest(
            profileFile.getOriginalFilename(),
            profileFile.getContentType(),
            profileFile.getBytes()
        );
        return Optional.of(binaryContentCreateRequest);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
