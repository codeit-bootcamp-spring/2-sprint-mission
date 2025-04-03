package com.sprint.mission.discodeit.controller.basic;

import com.sprint.mission.discodeit.controller.BinaryContentRequestResolver;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.basic.UserService;
import com.sprint.mission.discodeit.service.basic.UserStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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

@Tag(name = "User", description = "User API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class BasicUserController {

  private final UserService userService;
  private final UserStatusService userStatusService;
  private final BinaryContentRequestResolver binaryContentRequestResolver;
  private static final Logger logger = LoggerFactory.getLogger(BasicUserController.class);

  @Operation(summary = "user 등록")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "User가 성공적으로 생성됨"),
      @ApiResponse(responseCode = "400", description = "같은 newEmail 또는 username를 사용하는 User가 이미 존재함")
  })
  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<User> create(
      @Parameter(description = "User 생성 요청 정보",
          schema = @Schema(implementation = UserCreateRequest.class))
      @RequestPart("userCreateRequest") UserCreateRequest request,
      @Parameter(description = "User 프로필 이미지(선택 사항)",
          schema = @Schema(type = "string", format = "binary"))
      @RequestPart(value = "profile", required = false) MultipartFile binaryContent) {

    Optional<BinaryContentCreateRequest> optionalBinaryContent = binaryContentRequestResolver.resolve(
        binaryContent);

    User response = userService.create(request, optionalBinaryContent);
    logger.info("Successfully created user: {}", response);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "User 정보 수정")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "User 정보가 성공적으로 수정됨"),
      @ApiResponse(responseCode = "400", description = "같은 newEmail 또는 username를 사용하는 User가 이미 존재함"),
      @ApiResponse(responseCode = "404", description = "User를 찾을 수 없음")
  })
  @PatchMapping(value = "/{userId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<User> update(
      @Parameter(description = "수정할 User ID")
      @PathVariable("userId") UUID userId,
      @Parameter(description = "User 수정 요청 정보", required = true)
      @RequestPart("user") UserUpdateRequest request,
      @Parameter(description = "수정할 User 프로필 이미지(선택 사항)")
      @RequestPart(value = "binaryContent", required = false) MultipartFile binaryContent) {

    Optional<BinaryContentCreateRequest> optionalBinaryContent = binaryContentRequestResolver.resolve(
        binaryContent);

    User response = userService.update(userId, request, optionalBinaryContent);
    logger.info("Successfully updated user: {}", response);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "User 삭제")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "User가 성공적으로 삭제됨"),
      @ApiResponse(responseCode = "404", description = "User를 찾을 수 없음")
  })
  @DeleteMapping("/{userId}")
  public ResponseEntity<String> delete(
      @Parameter(description = "삭제할 User ID")
      @PathVariable UUID userId) {
    userService.delete(userId);
    logger.info("Successfully deleted user: {}", userId);
    return ResponseEntity.ok("Successfully deleted user: " + userId);
  }

  @Operation(summary = "user 목록 조회")
  @ApiResponse(responseCode = "200", description = "user 목록 조회 성공")
  @GetMapping
  public ResponseEntity<List<UserDto>> findAll() {
    List<UserDto> users = userService.findAll();
    logger.info("Successfully find all users: {}", users);
    return ResponseEntity.ok(users);
  }

  @Operation(summary = "User 온라인 상태 업데이트")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "User 온라인 상태가 성공적으로 업데이트됨"),
      @ApiResponse(responseCode = "404", description = "해당 User의 UserStatus를 찾을 수 없음")
  })
  @PatchMapping("/{userId}/userStatus")
  public ResponseEntity<UserStatus> updateUserStatusByUserId(
      @Parameter(description = "상태를 변경할 User Id")
      @PathVariable UUID userId,
      @Parameter(description = "상태 변경 요청 정보")
      @RequestBody UserStatusUpdateRequest request) {
    UserStatus response = userStatusService.update(userId, request);
    logger.info("Successfully updated user status: {}", response);
    return ResponseEntity.ok(response);
  }
}
