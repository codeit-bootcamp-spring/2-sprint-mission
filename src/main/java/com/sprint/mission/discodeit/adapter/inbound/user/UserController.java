package com.sprint.mission.discodeit.adapter.inbound.user;


import com.sprint.mission.discodeit.adapter.inbound.user.request.UserCreateRequest;
import com.sprint.mission.discodeit.adapter.inbound.user.request.UserStatusRequest;
import com.sprint.mission.discodeit.adapter.inbound.user.request.UserUpdateRequest;
import com.sprint.mission.discodeit.adapter.inbound.user.response.UserDeleteResponse;
import com.sprint.mission.discodeit.adapter.inbound.user.response.UserResponse;
import com.sprint.mission.discodeit.adapter.inbound.user.response.UserStatusResponse;
import com.sprint.mission.discodeit.core.content.usecase.dto.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.core.status.usecase.user.dto.OnlineUserStatusCommand;
import com.sprint.mission.discodeit.core.status.usecase.user.dto.UserStatusResult;
import com.sprint.mission.discodeit.core.user.usecase.UserService;
import com.sprint.mission.discodeit.core.user.usecase.dto.CreateUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UpdateUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserListResult;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

@Tag(name = "User", description = "사용자 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final UserStatusDtoMapper userStatusDtoMapper;
  private final UserDtoMapper userDtoMapper;
  private final UserService userService;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserResponse> register(
      @RequestPart("userCreateRequest") @Valid UserCreateRequest requestBody,
      @RequestPart(value = "profile", required = false) MultipartFile file
  ) {

    Optional<CreateBinaryContentCommand> binaryContentRequest = Optional.ofNullable(file)
        .flatMap(this::resolveProfileRequest);

    CreateUserCommand command = userDtoMapper.toCreateUserCommand(requestBody);
    UserResult result = userService.create(command, binaryContentRequest);
    return ResponseEntity.ok(userDtoMapper.toCreateResponse(result));
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
  public ResponseEntity<UserResponse> updateUser(
      @PathVariable UUID userId,
      @RequestPart("userUpdateRequest") UserUpdateRequest requestBody,
      @RequestPart(value = "profile", required = false) MultipartFile file) {

    Optional<CreateBinaryContentCommand> binaryContentRequest = Optional.ofNullable(file)
        .flatMap(this::resolveProfileRequest);

    UpdateUserCommand command = userDtoMapper.toUpdateUserCommand(userId, requestBody);

    UserResult result = userService.update(command, binaryContentRequest);
    return ResponseEntity.ok(userDtoMapper.toCreateResponse(result));
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<UserDeleteResponse> deleteUser(@PathVariable UUID userId) {
    userService.delete(userId);
    return ResponseEntity.ok(new UserDeleteResponse(true));
  }

  @PatchMapping("/{userId}/userStatus")
  public ResponseEntity<UserStatusResponse> online(@PathVariable UUID userId,
      @RequestBody UserStatusRequest requestBody) {
    UserStatusResult result = userService.online(
        OnlineUserStatusCommand.create(userId, requestBody));

    return ResponseEntity.ok(userStatusDtoMapper.toCreateResponse(result));
  }
}
