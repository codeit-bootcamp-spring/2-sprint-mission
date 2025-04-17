package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.controller.user.*;
import com.sprint.mission.discodeit.dto.service.user.CreateUserCommand;
import com.sprint.mission.discodeit.dto.service.user.CreateUserResult;
import com.sprint.mission.discodeit.dto.service.user.FindUserResult;
import com.sprint.mission.discodeit.dto.service.user.UpdateUserCommand;
import com.sprint.mission.discodeit.dto.service.user.UpdateUserResult;
import com.sprint.mission.discodeit.dto.service.userStatus.UpdateUserStatusResult;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.swagger.UserApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController implements UserApi {

  private final UserService userService;
  private final UserStatusService userStatusService;
  private final UserMapper userMapper;
  private final UserStatusMapper userStatusMapper;

  @Override
  @PostMapping
  public ResponseEntity<CreateUserResponseDTO> createUser(
      @RequestPart("userCreateRequest") @Valid CreateUserRequestDTO createUserRequestDTO,
      @RequestPart(value = "profile", required = false) MultipartFile multipartFile) {
    CreateUserCommand createUserCommand = userMapper.toCreateUserCommand(createUserRequestDTO);
    CreateUserResult createUserResult = userService.create(createUserCommand, multipartFile);
    CreateUserResponseDTO createdUser = userMapper.toCreateUserResponseDTO(createUserResult);

    return ResponseEntity.ok(createdUser);
  }

  @Override
  @GetMapping("/{userId}")
  public ResponseEntity<FindUserResponseDTO> getUser(@PathVariable("userId") UUID id) {
    FindUserResult findUserResult = userService.find(id);
    FindUserResponseDTO userResponseDTO = userMapper.toFindUserResponseDTO(findUserResult);

    return ResponseEntity.ok(userResponseDTO);
  }

  @Override
  @GetMapping
  public ResponseEntity<List<FindUserResponseDTO>> getUserAll() {
    List<FindUserResult> users = userService.findAll();
    List<FindUserResponseDTO> userResponseDTOList = users.stream()
        .map(userMapper::toFindUserResponseDTO)
        .toList();

    return ResponseEntity.ok(userResponseDTOList);
  }

  @Override
  @PatchMapping("/{userId}")
  public ResponseEntity<UpdateUserResponseDTO> updateUser(@PathVariable("userId") UUID id,
      @RequestPart("userUpdateRequest") @Valid UpdateUserRequestDTO updateUserRequestDTO,
      @RequestPart(value = "profile", required = false) MultipartFile multipartFile) {
    UpdateUserCommand updateUserCommand = userMapper.toUpdateUserCommand(updateUserRequestDTO);
    UpdateUserResult updateUserResult = userService.update(id, updateUserCommand, multipartFile);
    UpdateUserResponseDTO updatedUser = userMapper.toUpdateUserResponseDTO(updateUserResult);

    return ResponseEntity.ok(updatedUser);
  }

  @Override
  @PatchMapping("/{userId}/userStatus")
  public ResponseEntity<UpdateUserStatusResponseDTO> updateUserStatus(
      @PathVariable("userId") UUID id) {
    UpdateUserStatusResult updateUserStatusResult = userStatusService.updateByUserId(id);
    UpdateUserStatusResponseDTO updatedUserStatus = userStatusMapper.toUpdateUserStatusResponseDTO(
        updateUserStatusResult);
    return ResponseEntity.ok(updatedUserStatus);
  }

  @Override
  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable("userId") UUID id) {
    userService.delete(id);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }
}
