package com.sprint.mission.discodeit.adapter.inbound.user;


import static com.sprint.mission.discodeit.adapter.inbound.user.UserDtoMapper.toCreateUserCommand;
import static com.sprint.mission.discodeit.adapter.inbound.user.UserDtoMapper.toLoginUserCommand;
import static com.sprint.mission.discodeit.adapter.inbound.user.UserDtoMapper.toUpdateUserCommand;

import com.sprint.mission.discodeit.adapter.inbound.content.dto.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.adapter.inbound.user.request.UserCreateRequest;
import com.sprint.mission.discodeit.adapter.inbound.user.response.UserCreateResponse;
import com.sprint.mission.discodeit.adapter.inbound.user.response.UserDeleteResponse;
import com.sprint.mission.discodeit.adapter.inbound.user.request.UserLoginRequest;
import com.sprint.mission.discodeit.adapter.inbound.user.response.UserLoginResponse;
import com.sprint.mission.discodeit.adapter.inbound.user.request.UserUpdateRequest;
import com.sprint.mission.discodeit.adapter.inbound.user.response.UserUpdateResponse;
import com.sprint.mission.discodeit.core.user.usecase.dto.CreateUserResult;
import com.sprint.mission.discodeit.core.user.usecase.dto.LoginUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.LoginUserResult;
import com.sprint.mission.discodeit.core.user.usecase.UserService;
import com.sprint.mission.discodeit.core.user.usecase.dto.CreateUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UpdateUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UpdateUserResult;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserListResult;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserResult;
import com.sprint.mission.discodeit.core.status.usecase.user.UserStatusService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
      @RequestPart("user") UserCreateRequest requestBody,
      @RequestPart(value = "profileImage", required = false) MultipartFile file
  ) throws IOException {

    Optional<CreateBinaryContentCommand> binaryContentRequest = Optional.empty();

    if (file != null && !file.isEmpty()) {
      binaryContentRequest = Optional.of(new CreateBinaryContentCommand(
          file.getOriginalFilename(),
          file.getContentType(),
          file.getBytes()
      ));
    }

    CreateUserCommand command = toCreateUserCommand(requestBody);
    CreateUserResult result = userService.create(command, binaryContentRequest);
    return ResponseEntity.ok(UserCreateResponse.create(result.user()));
  }

  @PostMapping("/login")
  public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest requestBody) {
    LoginUserCommand command = toLoginUserCommand(requestBody);
    LoginUserResult result = userService.login(command);

    return ResponseEntity.ok(UserLoginResponse.create(result.user()));
  }

  @GetMapping
  public ResponseEntity<List<UserResult>> findAll() {
    UserListResult result = userService.findAll();

    //TODO. 25.04.02 더 좋은 API 설계가 떠오르지 않아 임시로 List<UserResult> 반환
    return ResponseEntity.ok(result.userList());
  }

  @PutMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserUpdateResponse> updateUser(
      @PathVariable UUID userId,
      @RequestPart("user") UserUpdateRequest requestBody,
      @RequestPart(value = "profileImage", required = false) MultipartFile file)
      throws IOException {

    Optional<CreateBinaryContentCommand> binaryContentRequest = Optional.empty();

    if (file != null && !file.isEmpty()) {
      binaryContentRequest = Optional.of(new CreateBinaryContentCommand(
          file.getOriginalFilename(),
          file.getContentType(),
          file.getBytes()
      ));
    }
    UpdateUserCommand command = toUpdateUserCommand(userId, requestBody);

    UpdateUserResult result = userService.update(command, binaryContentRequest);
    return ResponseEntity.ok(UserUpdateResponse.create(result.user()));
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<UserDeleteResponse> deleteUser(@PathVariable UUID userId) {
    userService.delete(userId);
    return ResponseEntity.ok(new UserDeleteResponse(true));
  }
//  TODO. 25.04.02 online 상태 구현해야 함
//  @PutMapping("/online/{userId}")
//  public ResponseEntity<UUID> online(@PathVariable UUID userId) {
//    UserStatus userStatus = userStatusService.findByUserId(userId);
//    userStatus.updatedTime();
//
//    return ResponseEntity.ok(userStatus.getUserStatusId());
//  }
//
//  @PutMapping("/offline/{userId}")
//  public ResponseEntity<UUID> offline(@PathVariable UUID userId) {
//    UserStatus userStatus = userStatusService.findByUserId(userId);
//    userStatus.setOffline();
//
//    return ResponseEntity.ok(userStatus.getUserStatusId());
//  }
}
