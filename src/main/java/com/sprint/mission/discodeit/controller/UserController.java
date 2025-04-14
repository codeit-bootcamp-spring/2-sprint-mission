package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserStatusService userStatusService;
  private final UserService userService;

  private Optional<BinaryContentCreateRequest> toBinaryContentCreateRequest(MultipartFile file) {
    return Optional.ofNullable(file)
        .map(f -> {
          try {
            return new BinaryContentCreateRequest(
                f.getOriginalFilename(),
                f.getContentType(),
                f.getBytes()
            );
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });
  }

  @RequestMapping(method = RequestMethod.POST, consumes = "multipart/form-data")
  public ResponseEntity<UserDto> register(
      @RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profileFile) {
    Optional<BinaryContentCreateRequest> optionalProfileCreateRequest = toBinaryContentCreateRequest(
        profileFile);
    User user = userService.create(userCreateRequest, optionalProfileCreateRequest);
    UserDto userDto = userService.find(user.getId());

    return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
  }

  @RequestMapping(value = "/{userId}", method = RequestMethod.PATCH, consumes = "multipart/form-data")
  public ResponseEntity<UserDto> update(@PathVariable UUID userId,
      @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profileFile) {
    Optional<BinaryContentCreateRequest> optionalProfileCreateRequest = toBinaryContentCreateRequest(
        profileFile);
    User user = userService.update(userId, userUpdateRequest, optionalProfileCreateRequest);
    UserDto userDto = userService.find(user.getId());

    return ResponseEntity.status(HttpStatus.OK).body(userDto);
  }

  @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
  public ResponseEntity<UserDto> delete(@PathVariable UUID userId) {
    userService.delete(userId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<UserDto>> findAll() {
    List<UserDto> userDtos = userService.findAll();
    return ResponseEntity.status(HttpStatus.OK).body(userDtos);
  }

  @RequestMapping(value = "/{userId}/userStatus", method = RequestMethod.PATCH)
  public ResponseEntity<UserStatus> updateUserStatusByUserId(@PathVariable UUID userId,
      @RequestBody UserStatusUpdateRequest request) {
    UserStatus updatedUserStatus = userStatusService.updateByUserId(userId, request);
    return ResponseEntity.status(HttpStatus.OK).body(updatedUserStatus);
  }
}