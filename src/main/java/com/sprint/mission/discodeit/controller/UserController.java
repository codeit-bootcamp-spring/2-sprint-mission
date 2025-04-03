package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.controller.dto.User;
import com.sprint.mission.discodeit.controller.dto.UserCreateRequest;
import com.sprint.mission.discodeit.controller.dto.UserDto;
import com.sprint.mission.discodeit.controller.dto.UserStatus;
import com.sprint.mission.discodeit.controller.dto.UserStatusCreateRequest;
import com.sprint.mission.discodeit.controller.dto.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.controller.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity._User;
import com.sprint.mission.discodeit.entity._UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Controller
@ResponseBody
@RequestMapping("/api/user")
public class UserController implements UserApi {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @Override
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<User> create(
      @RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile) {
    Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
        .flatMap(this::resolveProfileRequest);

    _User user =   userService.create(userCreateRequest, profileRequest);
    UserStatusCreateRequest userStatusCreateRequest = new UserStatusCreateRequest(
        user.getId(), user.getCreatedAt()
    );
    userStatusService.create(userStatusCreateRequest);
    ModelMapper modelMapper = new ModelMapper();
    User apiUser = modelMapper.map(user, User.class);

    return ResponseEntity.status(HttpStatus.CREATED).body(apiUser);
  }

  @Override
  public ResponseEntity<Void> delete(Object userId) {
    UUID uuid = UUID.fromString(userId.toString());
    userService.delete(uuid);
    userStatusService.delete(uuid);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  @Override
  public ResponseEntity<Object> findAll() {
    List<UserDto> userDtos =   userService.findAll();
    ModelMapper modelMapper = new ModelMapper();
    List<User> users = new ArrayList<>();
    for(UserDto userDto : userDtos) {
      users.add(modelMapper.map(userService.find(UUID.fromString
              (userDto.getId().toString())),
          User.class));
    }

    return ResponseEntity.status(HttpStatus.OK).body(users);
  }

  @Override
  public ResponseEntity<User> update(Object userId, UserUpdateRequest userUpdateRequest,
  @RequestPart(value = "profile", required = false) MultipartFile profile) {
    UUID uuid = UUID.fromString(userId.toString());
    Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
        .flatMap(this::resolveProfileRequest);

    _User updatedUser = userService.update(uuid, userUpdateRequest, profileRequest);
    UserStatusUpdateRequest userStatusUpdateRequest = new UserStatusUpdateRequest();
    userStatusUpdateRequest.newLastActiveAt(updatedUser.getCreatedAt());

    userStatusService.update(uuid, userStatusUpdateRequest);
    ModelMapper modelMapper = new ModelMapper();
    User apiUser = modelMapper.map(updatedUser, User.class);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(apiUser);
  }

  @Override
  public ResponseEntity<UserStatus> updateUserStatusByUserId(Object userId,
      UserStatusUpdateRequest userStatusUpdateRequest) {
    UUID uuid = UUID.fromString(userId.toString());

    _UserStatus updatedUserStatus = userStatusService.update(uuid, userStatusUpdateRequest);

    ModelMapper modelMapper = new ModelMapper();
    UserStatus apiUserStatus = modelMapper.map(userStatusUpdateRequest, UserStatus.class);

    return  ResponseEntity
        .status(HttpStatus.OK)
        .body(apiUserStatus);
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
