package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.Api.UserApi;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Controller
@ResponseBody
//@RequestMapping("/api/users")
public class UserController implements UserApi {

  private final UserService userService;
  private final UserStatusService userStatusService;
  private final UserMapper userMapper;

  @Override
  public ResponseEntity<UserDto> create(UserCreateRequest userCreateRequest,
      MultipartFile profile) {
    if (userCreateRequest == null) {
      throw new IllegalArgumentException("UserCreateRequest가 null입니다!");
    }

    Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
        .flatMap(this::resolveProfileRequest);

    User user = userService.create(userCreateRequest, profileRequest);
    UserStatusCreateRequest userStatusCreateRequest = new UserStatusCreateRequest(
        user.getId(), user.getCreatedAt()
    );
    userStatusService.create(userStatusCreateRequest);
    UserDto dto = userMapper.toDto(user);

    return ResponseEntity.status(HttpStatus.CREATED).body(dto);
  }


  @Override
  public ResponseEntity<Void> delete(UUID userId) {
    userService.delete(userId);
    userStatusService.delete(userId);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  @Override
  public ResponseEntity<List<UserDto>> findAll() {
    List<UserDto> userDtos = userService.findAll(); // Spring ImmutableCollections$ListN

    return ResponseEntity.status(HttpStatus.OK).body(userDtos);
  }

  @Override
  public ResponseEntity<UserDto> update(UUID userId, UserUpdateRequest userUpdateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile) {

    Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
        .flatMap(this::resolveProfileRequest);

    User updatedUser = userService.update(userId, userUpdateRequest, profileRequest);
    UserStatusUpdateRequest userStatusUpdateRequest = new UserStatusUpdateRequest(
        updatedUser.getUpdatedAt()
    );

    userStatusService.update(userId, userStatusUpdateRequest);

    UserDto dto = userMapper.toDto(updatedUser);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(dto);
  }

  @Override
  public ResponseEntity<UserStatusDto> updateUserStatusByUserId(UUID userId,
      UserStatusUpdateRequest userStatusUpdateRequest) {

    UserStatus updated = userStatusService.update(userId, userStatusUpdateRequest);

    UserStatusDto dto = new UserStatusDto(updated.getId(), updated.getUser().getId(),
        updated.getLastActiveAt());

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(dto);
  }

  /*
  @RequestMapping(
      path = "create",
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
  )
  public ResponseEntity<User> create(
      @RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
        .flatMap(this::resolveProfileRequest);
    User createdUser = userService.create(userCreateRequest, profileRequest);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdUser);
  }

  @RequestMapping(
      path = "update",
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
  )
  public ResponseEntity<User> update(
      @RequestParam("userId") UUID userId,
      @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
        .flatMap(this::resolveProfileRequest);
    User updatedUser = userService.update(userId, userUpdateRequest, profileRequest);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(updatedUser);
  }

  @RequestMapping(path = "delete")
  public ResponseEntity<Void> delete(@RequestParam("userId") UUID userId) {
    userService.delete(userId);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  @RequestMapping(path = "findAll")
  public ResponseEntity<List<UserDto>> findAll() {
    List<UserDto> users = userService.findAll();
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(users);
  }

  @RequestMapping(path = "updateUserStatusByUserId")
  public ResponseEntity<UserStatus> updateUserStatusByUserId(@RequestParam("userId") UUID userId,
      @RequestBody UserStatusUpdateRequest request) {
    UserStatus updatedUserStatus = userStatusService.updateByUserId(userId, request);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(updatedUserStatus);
  }
*/
  private Optional<BinaryContentCreateRequest> resolveProfileRequest(MultipartFile profileFile) {
    if (profileFile.isEmpty()) {
      return Optional.empty();
    } else {
      try {
        BinaryContentCreateRequest binaryContentCreateRequest = new BinaryContentCreateRequest(
            profileFile.getOriginalFilename(),
            profileFile.getContentType(),
            profileFile.getBytes()
        );
        return Optional.of(binaryContentCreateRequest);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
