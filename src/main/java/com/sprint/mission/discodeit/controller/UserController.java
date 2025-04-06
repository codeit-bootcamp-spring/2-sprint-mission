package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserInfoDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.jwt.JwtUtil;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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
  private final JwtUtil jwtUtil;
  private final BinaryContentService binaryContentService;

  @RequestMapping(value = "", method = RequestMethod.POST)
  public ResponseEntity<User> register(@RequestPart("userCreateRequest") CreateUserRequest request,
      @RequestPart(value = "profile", required = false) MultipartFile profile) {

    User user = authService.register(request);
    if (!profile.isEmpty()) {
      UUID binaryId = binaryContentService.createBinaryContent(profile);
      user = userService.updateProfile(user.getId(), binaryId);
    }
    return ResponseEntity.status(201).body(user);
  }

  @RequestMapping(value = "/{userId}", method = RequestMethod.PATCH)
  public ResponseEntity<User> updateUser(
      @RequestPart("userUpdateRequest") UpdateUserRequest request,
      @RequestPart(value = "profile", required = false) MultipartFile profile,
      @PathVariable("userId") UUID userId
  ) {
    User user = userService.updateUser(userId, request);

    if (!profile.isEmpty()) {
      UUID binaryId = binaryContentService.createBinaryContent(profile);
      userService.updateProfile(userId, binaryId);
    }
    return ResponseEntity.ok(user);
  }

//  @RequestMapping(value = "/{userId}", method = RequestMethod.PATCH)
//  public ResponseEntity<?> updateUserProfile(
//      @RequestParam("profile") MultipartFile profile,
//      @PathVariable("userId") UUID userId
//  ) {
//    UUID binaryId = binaryContentService.createBinaryContent(profile);
//
//    userService.updateProfile(userId, binaryId);
//
//    return ResponseEntity.ok("프로필사진 변경 완료");
//  }


  @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
  public ResponseEntity<?> deleteUser(@PathVariable("userId") UUID userId) {
    userService.deleteUser(userId);
    return ResponseEntity.status(204).body("회원 탈퇴 완료");
  }

  @RequestMapping(value = "", method = RequestMethod.GET)
  public ResponseEntity<List<UserInfoDto>> findAll() {
    return ResponseEntity.ok(userService.getAllUsers());
  }

  @RequestMapping(value = "/{userId}/userStatus", method = RequestMethod.PATCH)
  public ResponseEntity<?> updateStatus(
      @PathVariable("userId") UUID userId,
      @RequestBody UserStatusUpdateRequest request) {
    UserStatus userStatus = userStatusService.update(userId, request);
    return ResponseEntity.ok(userStatus);
  }
}
