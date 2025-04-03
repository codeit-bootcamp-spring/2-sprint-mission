package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.UserApi;
import com.sprint.mission.discodeit.controller.dto.UserResponse;
import com.sprint.mission.discodeit.controller.dto.UserStatusUpdateResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.service.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.service.dto.user.UserDto;
import com.sprint.mission.discodeit.service.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.service.dto.user.userstatus.UserStatusUpdateRequest;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController implements UserApi {

  private final UserService userService;
  private final UserStatusService userStatusService;

  // 사용자 등록
  @Override
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserResponse> create(
      @RequestPart("userCreateRequest") UserCreateRequest userRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    BinaryContentCreateRequest fileData = BinaryContentCreateRequest.of(profile);
    User user = userService.create(userRequest, fileData);
    return ResponseEntity.ok(UserResponse.of(user));
  }

  // 사용자 정보 수정
  @Override
  @PatchMapping(path = "/{userId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<UserResponse> update(
      @PathVariable("userId") UUID userId,
      @RequestPart("userUpdateRequest") UserUpdateRequest request,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    BinaryContentCreateRequest fileData = BinaryContentCreateRequest.of(profile);
    User user = userService.update(userId, request, fileData);
    return ResponseEntity.ok(UserResponse.of(user));
  }

  // 사용자 삭제
  @Override
  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> delete(@PathVariable("userId") UUID userId) {
    userService.delete(userId);
    return ResponseEntity.noContent().build();
  }

  // 모든 사용자 조회
  @Override
  @GetMapping
  public ResponseEntity<List<UserDto>> findAll() {
    List<UserDto> response = userService.findAll();
    return ResponseEntity.ok(response);
  }

  // 사용자 온라인 상태 변경
  @Override
  @PatchMapping("/{userId}/userStatus")
  public ResponseEntity<UserStatusUpdateResponse> updateStatus(
      @PathVariable("userId") UUID userId,
      @RequestBody UserStatusUpdateRequest request
  ) {
    UserStatus userStatus = userStatusService.updateByUserId(userId, request);
    UserStatusUpdateResponse response = UserStatusUpdateResponse.of(userStatus);
    return ResponseEntity.ok(response);
  }
}
