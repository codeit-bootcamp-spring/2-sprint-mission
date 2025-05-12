package com.sprint.mission.discodeit.core.user.controller;


import com.sprint.mission.discodeit.core.content.usecase.dto.BinaryContentCreateCommand;
import com.sprint.mission.discodeit.core.status.usecase.user.dto.UserStatusOnlineCommand;
import com.sprint.mission.discodeit.core.status.usecase.user.dto.UserStatusDto;
import com.sprint.mission.discodeit.core.user.controller.dto.UserCreateRequest;
import com.sprint.mission.discodeit.core.user.controller.dto.UserStatusRequest;
import com.sprint.mission.discodeit.core.user.controller.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.core.user.controller.dto.UserDeleteResponse;
import com.sprint.mission.discodeit.core.user.usecase.UserService;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserCreateCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserUpdateCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserDto;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserDto> register(
      @RequestPart("userCreateRequest") @Valid UserCreateRequest requestBody,
      @RequestPart(value = "profile", required = false) MultipartFile file
  ) {

    Optional<BinaryContentCreateCommand> binaryContentRequest = Optional.ofNullable(file)
        .flatMap(this::resolveProfileRequest);

    UserCreateCommand command = UserDtoMapper.toCreateUserCommand(requestBody);
    UserDto result = userService.create(command, binaryContentRequest);

    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  private Optional<BinaryContentCreateCommand> resolveProfileRequest(MultipartFile profileFile) {
    if (profileFile.isEmpty()) {
      return Optional.empty();
    } else {
      try {
        BinaryContentCreateCommand binaryContentCreateRequest = BinaryContentCreateCommand.create(
            profileFile);
        return Optional.of(binaryContentCreateRequest);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @GetMapping
  public ResponseEntity<List<UserDto>> findAll() {
    List<UserDto> result = userService.findAll();

    return ResponseEntity.ok(result);
  }

  @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserDto> update(
      @PathVariable UUID userId,
      @RequestPart("userUpdateRequest") @Valid UserUpdateRequest requestBody,
      @RequestPart(value = "profile", required = false) MultipartFile file) {

    Optional<BinaryContentCreateCommand> binaryContentRequest = Optional.ofNullable(file)
        .flatMap(this::resolveProfileRequest);
    UserUpdateCommand command = UserDtoMapper.toUpdateUserCommand(userId, requestBody);

    UserDto result = userService.update(command, binaryContentRequest);
    return ResponseEntity.ok(result);
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<UserDeleteResponse> deleteUser(@PathVariable UUID userId) {
    userService.delete(userId);
    return ResponseEntity.ok(new UserDeleteResponse(true));
  }

  @PatchMapping("/{userId}/userStatus")
  public ResponseEntity<UserStatusDto> online(@PathVariable UUID userId,
      @RequestBody UserStatusRequest requestBody) {

    UserStatusOnlineCommand command = UserStatusOnlineCommand.create(userId,
        requestBody);

    UserStatusDto result = userService.online(command);

    return ResponseEntity.ok(result);
  }
}
