package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.*;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateResponse;
import com.sprint.mission.discodeit.entity.user.UserStatus;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;
  private final ChannelService channelService;
  private final ReadStatusService readStatusService;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserCreateResponse> create(
      @RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profileRequest) {

    BinaryContentCreateRequest profileCreateRequest = (profileRequest == null)
        ? null
        : BinaryContentCreateRequest.fromMultipartFile(profileRequest);

    UserCreateResponse response = userService.create(userCreateRequest, profileCreateRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserUpdateResponse> update(
      @PathVariable UUID userId,
      @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profileRequest
  ) {
    BinaryContentCreateRequest profileCreateRequest = (profileRequest == null)
        ? null
        : BinaryContentCreateRequest.fromMultipartFile(profileRequest);

    UserUpdateResponse response = userService.update(userId, userUpdateRequest,
        profileCreateRequest);
    return ResponseEntity.ok(response);
  }

  @PatchMapping("/{userId}/userStatus")
  public ResponseEntity<UserStatusUpdateResponse> updateStatus(@PathVariable UUID userId,
      @RequestBody UserStatusUpdateRequest userStatusUpdateRequest) {
    UserStatus updatedStatus = userStatusService.update(userId,
        userStatusUpdateRequest.newLastActiveAt());

    UserStatusUpdateResponse response = UserStatusUpdateResponse.fromEntity(updatedStatus);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{userId}")
  public ResponseEntity<UserResponse> find(@PathVariable UUID userId) {
    UserResponse response = userService.find(userId);
    return ResponseEntity.ok(response);
  }

  @GetMapping({"", "/"})
  public ResponseEntity<List<UserResponse>> findAll() {
    List<UserResponse> response = userService.findAll();
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> delete(@PathVariable UUID userId) {
    userService.delete(userId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
