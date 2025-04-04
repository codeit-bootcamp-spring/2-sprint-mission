package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.*;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.util.LogMapUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "User Api")
public class UserController {

  private static final Logger log = LoggerFactory.getLogger(UserController.class);
  private final UserService userService;
  private final UserStatusService userStatusService;

  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<UserDto> create(
      @RequestPart("userCreateRequest") UserCreateRequest userRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile) {
    BinaryContentCreateRequest profileRequest = resolveProfileRequest(profile);
    UserDto userDto = userService.create(userRequest, profileRequest);
    log.info("{}", LogMapUtil.of("action", "createUser")
        .add("userDto", userDto));

    return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
  }

  @PutMapping(path = "/{userKey}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<User> update(
      @PathVariable UUID userKey,
      @RequestPart("userUpdateRequest") UserUpdateRequest userRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    BinaryContentCreateRequest profileRequest = resolveProfileRequest(profile);
    User updatedUser = userService.update(userKey, userRequest, profileRequest);
    log.info("{}", LogMapUtil.of("action", "updateUser")
        .add("updatedUser", updatedUser));
    return ResponseEntity.ok(updatedUser);
  }

  @GetMapping
  public ResponseEntity<List<UserDto>> readAll() {
    List<UserDto> userDtos = userService.readAll();
    log.info("{}", LogMapUtil.of("action", "readAllUsers")
        .add("userDtos", userDtos));

    return ResponseEntity.ok(userDtos);
  }

  @DeleteMapping("/{userKey}")
  public ResponseEntity<Void> deleteUser(@PathVariable UUID userKey) {
    userService.delete(userKey);
    log.info("{}", LogMapUtil.of("action", "deleteUser")
        .add("userKey", userKey));

    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{userKey}/status")
  public ResponseEntity<UserStatus> updateUserStatusByUserKey(@PathVariable UUID userKey,
      @RequestBody UserStatusUpdateRequest request) {
    UserStatus updatedUserStatus = userStatusService.updateByUserKey(userKey, request);
    log.info("{}", LogMapUtil.of("action", "updateStatus")
        .add("updatedUserStatus", updatedUserStatus));

    return ResponseEntity.ok(updatedUserStatus);
  }

  private BinaryContentCreateRequest resolveProfileRequest(MultipartFile profileFile) {
    if (profileFile == null || profileFile.isEmpty()) {
      return null;
    }

    try {
      return new BinaryContentCreateRequest(
          profileFile.getOriginalFilename(),
          profileFile.getContentType(),
          profileFile.getBytes()
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
