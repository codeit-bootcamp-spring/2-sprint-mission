package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.dto.CreateUserRequest;
import com.sprint.mission.discodeit.dto.RegisterResponse;
import com.sprint.mission.discodeit.dto.UpdateUserRequest;
import com.sprint.mission.discodeit.jwt.JwtUtil;
import com.sprint.mission.discodeit.jwt.RequiresAuth;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
  public ResponseEntity<?> register(@RequestBody CreateUserRequest request) {

    RegisterResponse response = authService.register(request);
    return ResponseEntity.ok(response);

  }

  @RequiresAuth
  @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
  public ResponseEntity<?> updateUser(
      @RequestBody UpdateUserRequest request,
      @PathVariable UUID userId
  ) {
    userService.updateUser(userId, request);
    return ResponseEntity.ok("회원정보 수정 완료");
  }

  @RequiresAuth
  @RequestMapping(value = "/{userId}", method = RequestMethod.PATCH)
  public ResponseEntity<?> updateUserProfile(
      @RequestParam("file") MultipartFile profile,
      @PathVariable UUID userId
  ) throws IOException {
    CreateBinaryContentRequest request = CreateBinaryContentRequest.builder()
        .fileName(profile.getOriginalFilename())
        .size(profile.getSize())
        .contentType(profile.getContentType())
        .bytes(profile.getBytes())
        .build();

    UUID binaryId = binaryContentService.createBinaryContent(request);

    userService.updateProfile(userId, binaryId);

    return ResponseEntity.ok("프로필사진 변경 완료");
  }


  @RequiresAuth
  @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
  public ResponseEntity<?> deleteUser(@PathVariable UUID userId) {
    userService.deleteUser(userId);
    return ResponseEntity.ok("회원 탈퇴 완료");
  }

  @RequestMapping(value = "", method = RequestMethod.GET)
  public ResponseEntity<?> findAll() {
    return ResponseEntity.ok(userService.getAllUsers());
  }

  @RequiresAuth
  @RequestMapping(value = "/{userId}/userStatus", method = RequestMethod.PATCH)
  public ResponseEntity<?> updateStatus(@PathVariable UUID userId) {
    userStatusService.update(userId);
    return ResponseEntity.ok("온라인 상태 갱신 완료");
  }
}
