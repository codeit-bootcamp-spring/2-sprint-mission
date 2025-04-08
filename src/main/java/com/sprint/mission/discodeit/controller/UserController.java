
package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "사용자 API", description = "사용자 관련 기능 제공")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  @Operation(summary = "사용자 생성", description = "새로운 사용자를 생성합니다.")
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserDto> createUser(
      @RequestPart("userCreateRequest") UserCreateRequest request,
      @RequestPart(value = "profile", required = false) MultipartFile profileImage
  ) {
    Optional<BinaryContentCreateRequest> binaryContent = Optional.empty();
    if (profileImage != null && !profileImage.isEmpty()) {
      try {
        binaryContent = Optional.of(new BinaryContentCreateRequest(
            UUID.randomUUID().toString(),
            profileImage.getContentType(),
            profileImage.getBytes()
        ));
      } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
      }
    }
    return ResponseEntity.ok(
        userService.find(userService.create(request, binaryContent).getId()));
  }

  @Operation(summary = "사용자 정보 수정", description = "특정 사용자의 정보를 수정합니다.")
  @PatchMapping("/{userId}")
  public ResponseEntity<UserDto> updateUser(@PathVariable UUID userId,
      @RequestBody UserUpdateRequest request) {
    return ResponseEntity.ok(
        userService.find(userService.update(userId, request, Optional.empty()).getId()));
  }

  @Operation(summary = "사용자 삭제", description = "특정 사용자를 삭제합니다.")
  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
    userService.delete(userId);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "전체 사용자 목록 조회", description = "모든 사용자의 목록을 조회합니다.")
  @GetMapping
  public ResponseEntity<List<UserDto>> findAllUsers() {
    return ResponseEntity.ok(userService.findAll());
  }

  @Operation(summary = "사용자 조회", description = "특정 사용자의 정보를 조회합니다.")
  @GetMapping("/{userId}")
  public ResponseEntity<UserDto> getUser(@PathVariable UUID userId) {
    return ResponseEntity.ok(userService.find(userId));
  }

  @Operation(summary = "사용자 상태 변경", description = "사용자의 온라인 상태를 변경합니다.")
  @PatchMapping("/{userId}/userStatus")
  public ResponseEntity<Void> updateUserStatus(
      @PathVariable UUID userId,
      @RequestParam(required = false) Boolean online) {

    if (online == null) {
      online = false;
    }

    userService.updateUserStatus(userId, online);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "사용자 로그인", description = "사용자가 로그인하고 JWT 토큰을 발급받습니다.")
  @PostMapping("/auth/login")
  public ResponseEntity<String> login(@RequestBody LoginRequest request) {
    try {
      String token = userService.login(request.username(), request.password());
      return ResponseEntity.ok(token);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
  }
}
