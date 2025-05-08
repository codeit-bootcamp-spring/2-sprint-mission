package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final AuthService authService;
  private final UserStatusService userStatusService;
  private final BinaryContentService binaryContentService;

  @RequestMapping(value = "", method = RequestMethod.GET)
  public ResponseEntity<List<UserDto>> findAll() {
    return ResponseEntity.ok(userService.getAllUsers());
  }

  @RequestMapping(value = "", method = RequestMethod.POST)
  public ResponseEntity<UserDto> createUser(
      @Valid @RequestPart("userCreateRequest") CreateUserRequest request,
      @RequestPart(value = "profile", required = false) MultipartFile profile) {

    UserDto user = authService.register(request);
    if (!profile.isEmpty()) {
      BinaryContent binaryContent = binaryContentService.createBinaryContent(profile);
      user = userService.updateProfile(user.id(), binaryContent);
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(user);
  }

  @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
  public ResponseEntity<?> delete(@PathVariable("userId") UUID userId) {
    userService.deleteUser(userId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body("회원 탈퇴 완료");
  }

  @RequestMapping(value = "/{userId}", method = RequestMethod.PATCH)
  public ResponseEntity<UserDto> update(
      @Valid @RequestPart("userUpdateRequest") UpdateUserRequest request,
      @RequestPart(value = "profile", required = false) MultipartFile profile,
      @PathVariable("userId") UUID userId
  ) {
    UserDto userDto = userService.updateUser(userId, request);

    if (!profile.isEmpty()) {
      BinaryContent binaryContent = binaryContentService.createBinaryContent(profile);
      userService.updateProfile(userId, binaryContent);
    }
    return ResponseEntity.ok(userDto);
  }


  @RequestMapping(value = "/{userId}/userStatus", method = RequestMethod.PATCH)
  public ResponseEntity<UserStatusDto> updateStatus(
      @PathVariable("userId") UUID userId,
      @RequestBody UserStatusUpdateRequest request) {
    return ResponseEntity.ok(userStatusService.update(userId, request));
  }
}
