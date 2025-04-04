package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.CreateUserRequest;
import com.sprint.mission.discodeit.dto.RegisterResponse;
import com.sprint.mission.discodeit.dto.UpdateUserRequest;
import com.sprint.mission.discodeit.jwt.JwtUtil;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
  public ResponseEntity<?> register(@RequestPart("userCreateRequest") CreateUserRequest request,
      @RequestPart(value = "profile", required = false) MultipartFile profileImage) {

    RegisterResponse response = authService.register(request);
    if (!profileImage.isEmpty()) {
      UUID binaryId = binaryContentService.createBinaryContent(profileImage);
      userService.updateProfile(response.getUserId(), binaryId);
    }
    return ResponseEntity.ok(response);
  }

  @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
  public ResponseEntity<?> updateUser(
      @RequestBody UpdateUserRequest request,
      @PathVariable("userId") UUID userId
  ) {
    userService.updateUser(userId, request);
    return ResponseEntity.ok("회원정보 수정 완료");
  }

  @RequestMapping(value = "/{userId}", method = RequestMethod.PATCH)
  public ResponseEntity<?> updateUserProfile(
      @RequestParam("profile") MultipartFile profile,
      @PathVariable("userId") UUID userId
  ) {
    UUID binaryId = binaryContentService.createBinaryContent(profile);

    userService.updateProfile(userId, binaryId);

    return ResponseEntity.ok("프로필사진 변경 완료");
  }


  @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
  public ResponseEntity<?> deleteUser(@PathVariable("userId") UUID userId) {
    userService.deleteUser(userId);
    return ResponseEntity.ok("회원 탈퇴 완료");
  }

  @RequestMapping(value = "", method = RequestMethod.GET)
  public ResponseEntity<?> findAll() {
    return ResponseEntity.ok(userService.getAllUsers());
  }

  @RequestMapping(value = "/{userId}/userStatus", method = RequestMethod.PATCH)
  public ResponseEntity<?> updateStatus(@PathVariable("userId") UUID userId) {
    userStatusService.update(userId);
    return ResponseEntity.ok("온라인 상태 갱신 완료");
  }


}
