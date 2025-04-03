package com.sprint.mission.discodeit.controller;

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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User", description = "User API")
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;

  // 사용자 등록
  @Operation(summary = "User 등록")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "User가 성공적으로 생성됨",
          content = @Content(mediaType = "*/*", schema = @Schema(implementation = UserResponse.class))),
      @ApiResponse(responseCode = "400", description = "같은 email 또는 username를 사용하는 User가 이미 존재함",
          content = @Content(mediaType = "*/*", examples = {
              @ExampleObject(value = "{email}은 중복된 email")
          }))
  })
  @RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserResponse> create(
      @RequestPart("userDto") @Parameter(description = "User 생성 정보") UserCreateRequest userRequest,
      @RequestPart(value = "profile", required = false)
      @Parameter(description = "User 프로필 이미지") MultipartFile profile
  ) {
    BinaryContentCreateRequest fileData = BinaryContentCreateRequest.of(profile);
    User user = userService.create(userRequest, fileData);
    return ResponseEntity.ok(UserResponse.of(user));
  }

  // 사용자 정보 수정
  @Operation(summary = "User 정보 수정")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User가 성공적으로 수정됨",
          content = @Content(mediaType = "*/*", schema = @Schema(implementation = UserResponse.class))),
      @ApiResponse(responseCode = "404", description = "User를 찾을 수 없음",
          content = @Content(mediaType = "*/*", examples = {
              @ExampleObject(value = "{userId}에 해당하는 User를 찾을 수 없음")
          })),
      @ApiResponse(responseCode = "400", description = "같은 email 또는 username를 사용하는 User가 이미 존재함",
          content = @Content(mediaType = "*/*", examples = {
              @ExampleObject(value = "{newEmail}은 중복된 email")
          }))
  })
  @RequestMapping(value = "/{userId}", method = RequestMethod.PATCH
      , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserResponse> update(
      @PathVariable("userId") @Parameter(description = "수정할 User ID") UUID userId,
      @RequestPart("userUpdateDto") @Parameter(description = "수정할 User 정보") UserUpdateRequest request,
      @RequestPart(value = "profile", required = false)
      @Parameter(description = "수정할 User 프로필 이미지") MultipartFile profile
  ) {
    BinaryContentCreateRequest fileData = new BinaryContentCreateRequest(profile);
    User user = userService.update(userId, request, fileData);
    return ResponseEntity.ok(UserResponse.of(user));
  }

  // 사용자 삭제
  @Operation(summary = "User 삭제")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "User가 성공적으로 삭제됨"),
      @ApiResponse(responseCode = "404", description = "User를 찾을 수 없음",
          content = @Content(mediaType = "*/*", examples = {
              @ExampleObject(value = "{userId}에 해당하는 User를 찾을 수 없음")
          }))
  })
  @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
  public ResponseEntity<Void> delete(
      @PathVariable("userId") @Parameter(description = "삭제할 User ID") UUID userId) {
    userService.delete(userId);
    return ResponseEntity.noContent().build();
  }

  // 모든 사용자 조회
  @Operation(summary = "전체 User 목록 조회")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User 목록 조회 성공",
          content = @Content(mediaType = "*/*",
              array = @ArraySchema(schema = @Schema(implementation = UserResponse.class))))
  })
  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<UserDto>> findAll() {
    List<UserDto> response = userService.findAll();
    return ResponseEntity.ok(response);
  }

  // 사용자 온라인 상태 변경
  @Operation(summary = "User 온라인 상태 업데이트")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User 온라인 상태가 성공적으로 업데이트됨",
          content = @Content(mediaType = "*/*", schema = @Schema(implementation = UserStatusUpdateResponse.class))),
      @ApiResponse(responseCode = "404", description = "해당 User의 UserStatus를 찾을 수 없음",
          content = @Content(mediaType = "*/*", examples = {
              @ExampleObject(value = "{userId}에 해당하는 UserStatus를 찾을 수 없음")
          }))
  })
  @RequestMapping(value = "/{userId}/userStatus", method = RequestMethod.PUT)
  public ResponseEntity<UserStatusUpdateResponse> updateStatus(
      @PathVariable("userId") @Parameter(description = "상태를 변경할 User ID") UUID userId,
      @RequestBody UserStatusUpdateRequest request
  ) {
    UserStatus userStatus = userStatusService.updateByUserId(userId, request);
    UserStatusUpdateResponse response = UserStatusUpdateResponse.of(userStatus);
    return ResponseEntity.ok(response);
  }
}
