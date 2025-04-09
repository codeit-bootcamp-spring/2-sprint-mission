package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.controller.api.UserApi;
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
public class UserController implements UserApi {

  private final UserService userService;
  private final UserStatusService userStatusService;


  @Override
  @PostMapping
  public ResponseEntity<User> create(
      @RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profileFile) {

    Optional<BinaryContentCreateRequest> profileCreateRequest = Optional.ofNullable(profileFile)
        .map(file -> {
          try {
            return new BinaryContentCreateRequest(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getBytes()
            );
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });

    User createdUser = userService.create(userCreateRequest, profileCreateRequest);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(createdUser);
  }


  @Override
  @PatchMapping(path = "{userId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<User> update(
      @PathVariable("userId") UUID userId,
      @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profileFile) {

    Optional<BinaryContentCreateRequest> profileCreateRequest = Optional.ofNullable(profileFile)
        .map(file -> {
          try {
            return new BinaryContentCreateRequest(file.getOriginalFilename(), file.getContentType(),
                file.getBytes());
          } catch (IOException e) {
            throw new RuntimeException("File processing error", e);
          }
        });

    User updatedUser = userService.update(userId, userUpdateRequest, profileCreateRequest);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(updatedUser);
  }

  @Override
  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> delete(
      @Parameter(description = "삭제할 User ID") @PathVariable UUID userId) {

    userService.delete(userId);

    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  @Override
  @GetMapping
  public ResponseEntity<List<UserDto>> findAll() {
    List<UserDto> users = userService.findAll();

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(users);
  }

  @Override
  @PatchMapping("/{userId}/userStatus")
  public ResponseEntity<UserStatus> updateUserStatus(
      @PathVariable UUID userId,
      @RequestBody UserStatusUpdateRequest UserStatusUpdateRequest) {

    UserStatus updatedUserStatus = userStatusService.updateByUserId(userId,
        UserStatusUpdateRequest);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(updatedUserStatus);
  }
}


