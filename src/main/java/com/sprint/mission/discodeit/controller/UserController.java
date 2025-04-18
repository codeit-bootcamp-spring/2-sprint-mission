package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.*;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.user.UserStatus;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
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
  private final UserStatusMapper userStatusMapper;

  @GetMapping("/{userId}")
  public ResponseEntity<UserDto> find(@PathVariable UUID userId) {
    UserDto response = userService.find(userId);
    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<List<UserDto>> findAll() {
    List<UserDto> response = userService.findAll();
    return ResponseEntity.ok(response);
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserDto> create(
      @RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profileRequest) {

    BinaryContentCreateRequest profileCreateRequest =
        (profileRequest == null || profileRequest.isEmpty())
            ? null
            : BinaryContentCreateRequest.fromMultipartFile(profileRequest);

    UserDto response = userService.create(userCreateRequest, profileCreateRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserDto> update(
      @PathVariable UUID userId,
      @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profileRequest
  ) {
    BinaryContentCreateRequest profileCreateRequest =
        (profileRequest == null || profileRequest.isEmpty())
            ? null
            : BinaryContentCreateRequest.fromMultipartFile(profileRequest);

    UserDto response = userService.update(userId, userUpdateRequest,
        profileCreateRequest);
    return ResponseEntity.ok(response);
  }

  @PatchMapping("/{userId}/userStatus")
  public ResponseEntity<UserStatusDto> updateStatus(@PathVariable UUID userId,
      @RequestBody UserStatusUpdateRequest userStatusUpdateRequest) {
    UserStatus updatedStatus = userStatusService.updateByUserId(userId,
        userStatusUpdateRequest.newLastActiveAt());

    UserStatusDto response = userStatusMapper.toResponse(updatedStatus);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> delete(@PathVariable UUID userId) {
    userService.delete(userId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
