package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.*;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.util.LogMapUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User Api")
public class UserController {

  private static final Logger log = LoggerFactory.getLogger(UserController.class);
  private final UserService userService;
  private final UserStatusService userStatusService;

  @Operation(
      summary = "User 등록",
      description = "새로운 사용자를 등록합니다. 프로필 이미지를 함께 업로드할 수 있습니다.",
      operationId = "create",
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "User가 성공적으로 생성됨",
              content = @Content(schema = @Schema(implementation = UserDto.class))
          ),
          @ApiResponse(
              responseCode = "400",
              description = "같은 email 또는 username를 사용하는 User가 이미 존재함",
              content = @Content(
                  mediaType = "*/*",
                  examples = @ExampleObject(value = "User with email {email} already exists")
              )
          )
      }
  )
  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<UserDto> createUser(
      @RequestPart("userCreateRequest") UserCreateRequest userRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile) {
    BinaryContentCreateRequest profileRequest = resolveProfileRequest(profile);
    UserDto userDto = userService.create(userRequest, profileRequest);
    log.info("{}", LogMapUtil.of("action", "createUser")
        .add("userDto", userDto));

    return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
  }

  @PutMapping(path = "/{userKey}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<User> updateUser(
      @PathVariable UUID userKey,
      @RequestPart("userUpdateRequest") UserUpdateRequest userRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    BinaryContentCreateRequest profileRequest = resolveProfileRequest(profile);
    User updatedUser = userService.update(userKey, userRequest, profileRequest);
    log.info("{}", LogMapUtil.of("action", "updateUser")
        .add("updatedUser", updatedUser));
    return ResponseEntity.ok(updatedUser);
  }

  @GetMapping
  public ResponseEntity<List<UserDto>> readAllUsers() {
    List<UserDto> userDtos = userService.readAll();
    log.info("{}", LogMapUtil.of("action", "readAllUsers")
        .add("userDtos", userDtos));

    return ResponseEntity.ok(userDtos);
  }

  @DeleteMapping("/{userKey}")
  public ResponseEntity<Void> deleteUser(@PathVariable UUID userKey) {
    userService.delete(userKey);
    log.info("{}", LogMapUtil.of("action", "deleteUser")
        .add("userKey", userKey));

    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{userKey}/status")
  public ResponseEntity<UserStatus> updateUserStatus(@PathVariable UUID userKey) {
    UserStatus updatedUserStatus = userStatusService.update(userKey);
    log.info("{}", LogMapUtil.of("action", "updateStatus")
        .add("updatedUserStatus", updatedUserStatus));

    return ResponseEntity.ok(updatedUserStatus);
  }

  private BinaryContentCreateRequest resolveProfileRequest(MultipartFile profileFile) {
    if (profileFile == null || profileFile.isEmpty()) {
      return null;
    }

    try {
      return new BinaryContentCreateRequest(
          profileFile.getOriginalFilename(),
          profileFile.getContentType(),
          profileFile.getBytes()
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
