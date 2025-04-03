package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

  @Operation(summary = "사용자 생성")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "사용자 생성 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청")
  })
  @PostMapping(
      consumes = {
          MediaType.MULTIPART_FORM_DATA_VALUE,
          MediaType.APPLICATION_JSON_VALUE
      }
  )
  public ResponseEntity<UserResponse> createUser(
      @RequestPart(value = "userCreateRequest", required = false) CreateUserRequest request,
      @RequestPart(value = "profile", required = false) MultipartFile profileFile) {
    UUID userId = userService.createUser(request).getId();
    return userService.getUserById(userId)
        .map(user -> ResponseEntity.status(HttpStatus.CREATED).body(user))
        .orElse(ResponseEntity.badRequest().build());
  }

  @Operation(
      summary = "사용자 수정",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "사용자 수정 성공",
              content = @Content(mediaType = "*/*")
          )
      }
  )
  @PatchMapping(
      path = "/{userId}",
      consumes = {
          MediaType.MULTIPART_FORM_DATA_VALUE,
          MediaType.APPLICATION_JSON_VALUE
      }
  )
  public ResponseEntity<Void> updateUser(
      @PathVariable UUID userId,
      @RequestPart(value = "userUpdateRequest", required = false) UpdateUserRequest request,
      @RequestPart(value = "profile", required = false) MultipartFile profileFile) {
    userService.updateUser(new UpdateUserRequest(
        userId,
        request.newUsername(),
        request.newPassword(),
        request.newEmail(),
        request.profileImageFileName(),
        request.profileImageFilePath()
    ));
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "사용자 삭제")
  @ApiResponse(responseCode = "204", description = "사용자 삭제 성공")
  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
    userService.deleteUser(userId);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "전체 사용자 조회")
  @GetMapping
  public ResponseEntity<List<UserResponse>> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsers());
  }

  @Operation(
      summary = "온라인 상태 변경",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "상태 변경 성공",
              content = @Content(mediaType = "*/*")
          )
      }
  )
  @PatchMapping("/{userId}/userStatus")
  public ResponseEntity<Void> updateOnlineStatus(@PathVariable UUID userId) {
    userStatusService.updateByUserId(userId);
    return ResponseEntity.ok().build();
  }
}
