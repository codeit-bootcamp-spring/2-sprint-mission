package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.validation.Valid;
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
//@RequestMapping("/api/users")
public class UserController implements UserApi {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @Override

  public ResponseEntity<User> create(
      @RequestPart(value = "userCreateRequest", required = false) @Valid
      UserCreateRequest userCreateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile) {
    if (userCreateRequest == null) {
      throw new IllegalArgumentException("UserCreateRequest가 null입니다!");
    }

    Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
        .flatMap(this::resolveProfileRequest);

    User user =   userService.create(userCreateRequest, profileRequest);
    UserStatusCreateRequest userStatusCreateRequest = new UserStatusCreateRequest(
        user.getId(), user.getCreatedAt()
    );
    userStatusService.create(userStatusCreateRequest);


    return ResponseEntity.status(HttpStatus.CREATED).body(user);
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
    List<UserDto> userDtos =  userService.findAll(); // Spring ImmutableCollections$ListN

    return ResponseEntity.status(HttpStatus.OK).body(userDtos);
  }

  @Override
  public ResponseEntity<User> update(UUID userId, UserUpdateRequest userUpdateRequest,
  @RequestPart(value = "profile", required = false) MultipartFile profile) {

    Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
        .flatMap(this::resolveProfileRequest);

    User updatedUser = userService.update(userId, userUpdateRequest, profileRequest);
    UserStatusUpdateRequest userStatusUpdateRequest = new UserStatusUpdateRequest(
        updatedUser.getUpdatedAt()
    );

    userStatusService.update(userId, userStatusUpdateRequest);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(updatedUser);
  }

  @Override
  public ResponseEntity<UserStatus> updateUserStatusByUserId(UUID userId,
      UserStatusUpdateRequest userStatusUpdateRequest) {

    UserStatus updatedUserStatus = userStatusService.update(userId, userStatusUpdateRequest);


    return  ResponseEntity
        .status(HttpStatus.OK)
        .body(updatedUserStatus);
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
