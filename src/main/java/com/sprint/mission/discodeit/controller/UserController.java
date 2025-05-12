package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.*;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusResponse;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.user.UserStatus;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  private static final Logger log = LoggerFactory.getLogger(UserController.class);

  private final UserService userService;
  private final UserStatusService userStatusService;
  private final ChannelService channelService;
  private final ReadStatusService readStatusService;
  private final UserStatusMapper userStatusMapper;

  @GetMapping("/{userId}")
  public ResponseEntity<UserResponse> find(
      @PathVariable @NotNull(message = "사용자 ID는 필수입니다.") UUID userId) {
    log.debug("GET /api/users/{} - 사용자 조회 요청", userId);
    UserResponse response = userService.find(userId);
    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<List<UserResponse>> findAll() {
    log.debug("GET /api/users - 모든 사용자 조회 요청");
    List<UserResponse> response = userService.findAll();
    return ResponseEntity.ok(response);
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserResponse> create(
      @RequestPart("userCreateRequest") @Valid UserCreateRequest userCreateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profileRequest) {

    log.info("POST /api/users - 사용자 생성 요청: username={}, email={}", userCreateRequest.username(),
        userCreateRequest.email());
    if (profileRequest != null && !profileRequest.isEmpty()) {
      log.info("프로필 이미지 업로드 요청: filename={}, size={} bytes", profileRequest.getOriginalFilename(),
          profileRequest.getSize());
    }

    BinaryContentCreateRequest profileCreateRequest =
        (profileRequest == null || profileRequest.isEmpty()) ? null
            : BinaryContentCreateRequest.fromMultipartFile(profileRequest);

    UserResponse response = userService.create(userCreateRequest, profileCreateRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserResponse> update(
      @PathVariable @NotNull(message = "사용자 ID는 필수입니다.") UUID userId,
      @RequestPart("userUpdateRequest") @Valid UserUpdateRequest userUpdateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profileRequest) {
    log.info("PATCH /api/users/{} - 사용자 수정 요청", userId);

    if (profileRequest != null && !profileRequest.isEmpty()) {
      log.info("프로필 이미지 수정 업로드 요청: filename={}, size={} bytes",
          profileRequest.getOriginalFilename(), profileRequest.getSize());
    }

    BinaryContentCreateRequest profileCreateRequest =
        (profileRequest == null || profileRequest.isEmpty()) ? null
            : BinaryContentCreateRequest.fromMultipartFile(profileRequest);

    UserResponse response = userService.update(userId, userUpdateRequest, profileCreateRequest);
    return ResponseEntity.ok(response);
  }

  @PatchMapping("/{userId}/userStatus")
  public ResponseEntity<UserStatusResponse> updateStatus(
      @PathVariable @NotNull(message = "사용자 ID는 필수입니다.") UUID userId,
      @Valid @RequestBody UserStatusUpdateRequest userStatusUpdateRequest) {
    log.debug("PATCH /api/users/{}/userStatus - 상태 업데이트 요청", userId);

    UserStatus updatedStatus = userStatusService.updateByUserId(userId,
        userStatusUpdateRequest.newLastActiveAt());

    UserStatusResponse response = userStatusMapper.toResponse(updatedStatus);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> delete(
      @PathVariable @NotNull(message = "사용자 ID는 필수입니다.") UUID userId) {
    log.info("DELETE /api/users/{} - 유저 삭제 요청", userId);
    userService.delete(userId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
