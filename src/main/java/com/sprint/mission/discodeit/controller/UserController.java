package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.user.LoginRequest;
import com.sprint.mission.discodeit.dto.request.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.user.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.response.user.UserCreateResponse;
import com.sprint.mission.discodeit.dto.response.user.UserStatusResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User", description = "User API")
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;

  // 모든 사용자 조회
  @Operation(summary = "전체 User 목록 조회")
  @GetMapping
  public ResponseEntity<List<UserDto>> getAllUsers() {
    System.out.println("모든 사용자 조회 API 요청 들어옴.");
    List<UserDto> users = userService.findAll();
    return ResponseEntity.ok(users);
  }

  // 사용자 등록 (회원가입)
  @Operation(summary = "User 등록")
  @PostMapping
  public ResponseEntity<UserCreateResponse> createUser(
      @RequestPart("user") UserCreateRequest request,
      @RequestPart(value = "profileImg", required = false) MultipartFile profileFile
  ) {
    System.out.println("회원가입 API 요청 들어옴.");

    // 파일이 null이면 Optional.empty()
    Optional<BinaryContentCreateRequest> optionalProfile = Optional.ofNullable(profileFile)
        .map(file -> {
          try {
            return new BinaryContentCreateRequest(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getBytes()
            );
          } catch (Exception e) {
            throw new RuntimeException("프로필 파일 처리 실패", e);
          }
        });

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(userService.register(request, optionalProfile));
  }

  // 사용자 정보 수정, @PathVariable - URL경로 일부 {}
  // Post(생성), Get(조회), Put(수정), Delete(삭제)
  @Operation(summary = "User 정보 수정", description = "수정할 User ID")
  @PatchMapping(value = "/{userId}", consumes = "multipart/form-data")
  public ResponseEntity<User> updateUser(
      @PathVariable UUID userId,
      @RequestPart("user") UserUpdateRequest request,
      @RequestPart(value = "profileImg", required = false) MultipartFile profileImage
  ) {
    System.out.println("사용자 정보 수정 API 요청 들어옴.");

    Optional<BinaryContentCreateRequest> optionalProfileCreateRequest = Optional.empty();

    if (profileImage != null && !profileImage.isEmpty()) {
      try {
        BinaryContentCreateRequest profileRequest = new BinaryContentCreateRequest(
            profileImage.getOriginalFilename(),
            profileImage.getContentType(),
            profileImage.getBytes()
        );
        optionalProfileCreateRequest = Optional.of(profileRequest);
      } catch (IOException e) {
        throw new RuntimeException("프로필 이미지 처리 실패", e);
      }
    }

    User updatedUser = userService.update(userId, request, optionalProfileCreateRequest);
    return ResponseEntity.ok(updatedUser);
  }

  // 사용자 삭제
  @Operation(summary = "User 삭제", description = "삭제할 User ID")
  @DeleteMapping("/{userId}")
  public ResponseEntity<String> deleteUser(@PathVariable UUID userId) {
    System.out.println("사용자 삭제 API 요청 들어옴.");
    userService.delete(userId);
    return ResponseEntity.ok("delete success");
  }

  // 사용자 온라인 상태 업데이트, Patch로 리소스의 일부분만 업데이트
  @Operation(summary = "User 온라인 상태 업데이트", description = "상태를 변경할 User ID")
  @PatchMapping("/{userId}/userStatus")
  public ResponseEntity<UserStatusResponse> updateUserStatus(
      @PathVariable UUID userId,
      @RequestBody UserStatusUpdateRequest request
  ) {
    System.out.println("사용자 온라인 상태 업데이트 API 요청 들어옴.");

    UserStatus updated = userStatusService.updateByUserId(userId, request);

    UserStatusResponse response = new UserStatusResponse(
        updated.getUserId(),
        updated.isOnline(),
        updated.getUpdatedAt()
    );

    return ResponseEntity.ok(response);
  }
}
