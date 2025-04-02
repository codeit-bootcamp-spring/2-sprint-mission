package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

  @Operation(summary = "User 등록")
  @ApiResponse(responseCode = "201", description = "User가 성공적으로 생성됨", content = @Content(mediaType = "*/*", schema = @Schema(implementation = User.class)))
  @ApiResponse(responseCode = "400", description = "같은 email 또는 username를 사용하는 User가 이미 존재함", content = @Content(mediaType = "*/*", examples = {
      @ExampleObject(value = "User with email {email} already exists")}))
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserDto> create(
      @RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
      @RequestPart(value = "profile", required = false) @Schema(description = "User 프로필 이미지") Optional<MultipartFile> profileFile) {

    Optional<BinaryContentCreateRequest> profileCreateRequest = profileFile.map(file -> {
      try {
        return new BinaryContentCreateRequest(file.getOriginalFilename(), file.getContentType(),
            file.getBytes());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });

    User createdUser = userService.create(userCreateRequest, profileCreateRequest);
    UserDto userDto = userService.find(createdUser.getId());

    return new ResponseEntity<>(userDto, HttpStatus.CREATED);
  }

  @Operation(summary = "User 정보 수정")
  @ApiResponse(responseCode = "404", description = "User를 찾을 수 없음", content = @Content(mediaType = "*/*", examples = {
      @ExampleObject(value = "User with id {userId} not found")}))
  @ApiResponse(responseCode = "400", description = "같은 email 또는 username를 사용하는 User가 이미 존재함", content = @Content(mediaType = "*/*", examples = {
      @ExampleObject(value = "user with email {newEmail} already exists")}))
  @ApiResponse(responseCode = "200", description = "User 정보가 성공적으로 수정됨", content = @Content(mediaType = "*/*", schema = @Schema(implementation = User.class)))
  @PatchMapping(path = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  private ResponseEntity<User> update(
      @Parameter(description = "수정할 User ID") @PathVariable("userId") UUID userId,
      @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,
      @RequestPart(value = "profile", required = false) @Schema(description = "수정할 User 프로필 이미지") Optional<MultipartFile> profileFile) {

    Optional<BinaryContentCreateRequest> profileCreateRequest = profileFile.map(file -> {
      try {
        return new BinaryContentCreateRequest(file.getOriginalFilename(), file.getContentType(),
            file.getBytes());
      } catch (IOException e) {
        throw new RuntimeException("File processing error", e);
      }
    });

    User updatedUser = userService.update(userId, userUpdateRequest, profileCreateRequest);

    return new ResponseEntity<>(updatedUser, HttpStatus.OK);
  }

  @Operation(summary = "User 삭제")
  @ApiResponse(responseCode = "204", description = "User가 성공적으로 삭제됨")
  @ApiResponse(responseCode = "404", description = "User를 찾을 수 없음", content = @Content(mediaType = "*/*", examples = {
      @ExampleObject(value = "User with id {id} not found")}))
  @DeleteMapping("/{userId}")
  private ResponseEntity<Void> delete(
      @Parameter(description = "삭제할 User ID") @PathVariable UUID userId) {

    userService.delete(userId);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Operation(summary = "전체 User 목록 조회")
  @ApiResponse(responseCode = "200", description = "User 목록 조회 성공", content = @Content(mediaType = "*/*", array = @ArraySchema(schema = @Schema(implementation = UserDto.class))))
  @GetMapping
  public ResponseEntity<List<UserDto>> findAll() {
    List<UserDto> users = userService.findAll();

    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  @Operation(summary = "User 온라인 상태 업데이트")
  @ApiResponse(responseCode = "404", description = "해당 User의 UserStatus를 찾을 수 없음", content = @Content(mediaType = "*/*", examples = {
      @ExampleObject(value = "UserStatus with userId {userId} not found")}))
  @ApiResponse(responseCode = "200", description = "User 온라인 상태가 성공적으로 업데이트됨", content = @Content(mediaType = "*/*", schema = @Schema(implementation = UserStatus.class)))
  @PatchMapping("/{userId}/userStatus")
  public ResponseEntity<Void> updateUserStatus(@PathVariable UUID userId,
      @RequestBody UserStatusUpdateRequest updateRequest) {

    userStatusService.updateByUserId(userId, updateRequest);

    return new ResponseEntity<>(HttpStatus.OK);
  }
}


