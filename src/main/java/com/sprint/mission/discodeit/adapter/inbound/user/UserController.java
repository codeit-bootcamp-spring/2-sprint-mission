package com.sprint.mission.discodeit.adapter.inbound.user;


import static com.sprint.mission.discodeit.adapter.inbound.user.UserDtoMapper.toCreateUserCommand;
import static com.sprint.mission.discodeit.adapter.inbound.user.UserDtoMapper.toUpdateUserCommand;

import com.sprint.mission.discodeit.adapter.inbound.user.request.UserCreateRequest;
import com.sprint.mission.discodeit.adapter.inbound.user.request.UserStatusRequest;
import com.sprint.mission.discodeit.adapter.inbound.user.request.UserUpdateRequest;
import com.sprint.mission.discodeit.adapter.inbound.user.response.UserCreateResponse;
import com.sprint.mission.discodeit.adapter.inbound.user.response.UserDeleteResponse;
import com.sprint.mission.discodeit.adapter.inbound.user.response.UserStatusResponse;
import com.sprint.mission.discodeit.adapter.inbound.user.response.UserUpdateResponse;
import com.sprint.mission.discodeit.core.content.usecase.dto.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.core.status.entity.UserStatus;
import com.sprint.mission.discodeit.core.status.usecase.user.UserStatusService;
import com.sprint.mission.discodeit.core.user.usecase.UserService;
import com.sprint.mission.discodeit.core.user.usecase.dto.CreateUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.CreateUserResult;
import com.sprint.mission.discodeit.core.user.usecase.dto.UpdateUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UpdateUserResult;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserListResult;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserResult;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserCreateResponse> register(
      @RequestPart("userCreateRequest") UserCreateRequest requestBody,
      @RequestPart(value = "profile", required = false) MultipartFile file
  ) {

    Optional<CreateBinaryContentCommand> binaryContentRequest = Optional.ofNullable(file)
        .flatMap(this::resolveProfileRequest);

    CreateUserCommand command = toCreateUserCommand(requestBody);
    CreateUserResult result = userService.create(command, binaryContentRequest);
    return ResponseEntity.ok(UserCreateResponse.create(result.user()));
  }

  private Optional<CreateBinaryContentCommand> resolveProfileRequest(MultipartFile profileFile) {
    if (profileFile.isEmpty()) {
      return Optional.empty();
    } else {
      try {
        CreateBinaryContentCommand binaryContentCreateRequest = CreateBinaryContentCommand.create(
            profileFile);
        return Optional.of(binaryContentCreateRequest);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @GetMapping
  public ResponseEntity<List<UserResult>> findAll() {
    UserListResult result = userService.findAll();

    return ResponseEntity.ok(result.userList());
  }

  @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserUpdateResponse> updateUser(
      @PathVariable UUID userId,
      @RequestPart("userUpdateRequest") UserUpdateRequest requestBody,
      @RequestPart(value = "profile", required = false) MultipartFile file) {

    Optional<CreateBinaryContentCommand> binaryContentRequest = Optional.ofNullable(file)
        .flatMap(this::resolveProfileRequest);

    UpdateUserCommand command = toUpdateUserCommand(userId, requestBody);

    UpdateUserResult result = userService.update(command, binaryContentRequest);
    return ResponseEntity.ok(UserUpdateResponse.create(result.user()));
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<UserDeleteResponse> deleteUser(@PathVariable UUID userId) {
    userService.delete(userId);
    return ResponseEntity.ok(new UserDeleteResponse(true));
  }

  @PatchMapping("/{userId}/userStatus")
  public ResponseEntity<UserStatusResponse> online(@PathVariable UUID userId,
      @RequestBody UserStatusRequest requestBody) {
    UserStatus status = userStatusService.findByUserId(userId);

    status.update(requestBody.newLastActiveAt());
    boolean online = status.isOnline();

    return ResponseEntity.ok(UserStatusResponse.create(status, online));
  }
//
//  @PutMapping("/offline/{userId}")
//  public ResponseEntity<UUID> offline(@PathVariable UUID userId) {
//    UserStatus userStatus = userStatusService.findByUserId(userId);
//    userStatus.setOffline();
//
//    return ResponseEntity.ok(userStatus.getUserStatusId());
//  }
}
