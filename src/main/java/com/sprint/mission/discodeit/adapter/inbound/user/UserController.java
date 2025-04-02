package com.sprint.mission.discodeit.adapter.inbound.user;


import static com.sprint.mission.discodeit.adapter.inbound.user.UserDtoMapper.toCreateUserCommand;
import static com.sprint.mission.discodeit.adapter.inbound.user.UserDtoMapper.toLoginUserCommand;
import static com.sprint.mission.discodeit.adapter.inbound.user.UserDtoMapper.toUpdateUserCommand;

import com.sprint.mission.discodeit.adapter.inbound.content.dto.BinaryContentCreateRequestDTO;
import com.sprint.mission.discodeit.adapter.inbound.user.dto.UserCreateRequest;
import com.sprint.mission.discodeit.adapter.inbound.user.dto.UserCreateResponse;
import com.sprint.mission.discodeit.adapter.inbound.user.dto.UserDeleteResponse;
import com.sprint.mission.discodeit.adapter.inbound.user.dto.UserLoginRequest;
import com.sprint.mission.discodeit.adapter.inbound.user.dto.UserLoginResponse;
import com.sprint.mission.discodeit.adapter.inbound.user.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.adapter.inbound.user.dto.UserUpdateResponse;
import com.sprint.mission.discodeit.core.user.usecase.LoginUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.LoginUserResult;
import com.sprint.mission.discodeit.core.user.usecase.crud.UserService;
import com.sprint.mission.discodeit.core.user.usecase.crud.dto.CreateUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.crud.dto.UpdateUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.crud.dto.UserListResult;
import com.sprint.mission.discodeit.core.user.usecase.crud.dto.UserResult;
import com.sprint.mission.discodeit.core.user.usecase.status.UserStatusService;
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

  @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserCreateResponse> register(
      @RequestPart("user") UserCreateRequest requestBody,
      @RequestPart(value = "profileImage", required = false) MultipartFile file
  ) throws IOException {

    Optional<BinaryContentCreateRequestDTO> binaryContentRequest = Optional.empty();

    if (file != null && !file.isEmpty()) {
      binaryContentRequest = Optional.of(new BinaryContentCreateRequestDTO(
          file.getOriginalFilename(),
          file.getContentType(),
          file.getBytes()
      ));
    }

    CreateUserCommand command = toCreateUserCommand(requestBody);
    userService.create(command, binaryContentRequest);
    return ResponseEntity.ok(new UserCreateResponse(true, "User Create Successfully"));
  }

  @PostMapping("/login")
  public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest requestBody) {
    LoginUserCommand command = toLoginUserCommand(requestBody);
    LoginUserResult result = userService.login(command);
    //TODO. 25.04.02 현재 토큰 미구현으로 -1값을 넣어둠
    return ResponseEntity.ok(new UserLoginResponse(true, result.token()));
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

    Optional<BinaryContentCreateRequestDTO> binaryContentRequest = Optional.empty();

    if (file != null && !file.isEmpty()) {
      binaryContentRequest = Optional.of(new BinaryContentCreateRequestDTO(
          file.getOriginalFilename(),
          file.getContentType(),
          file.getBytes()
      ));
    }
    UpdateUserCommand command = toUpdateUserCommand(userId, requestBody);

    userService.update(command, binaryContentRequest);
    return ResponseEntity.ok(new UserUpdateResponse(true));
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
