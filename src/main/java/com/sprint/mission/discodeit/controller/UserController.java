package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.controller.user.*;
import com.sprint.mission.discodeit.dto.service.user.CreateUserParam;
import com.sprint.mission.discodeit.dto.service.user.UpdateUserDTO;
import com.sprint.mission.discodeit.dto.service.user.UpdateUserParam;
import com.sprint.mission.discodeit.dto.service.user.UserDTO;
import com.sprint.mission.discodeit.entity.UserStatus;
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
    CreateUserParam createUserParam = userMapper.toCreateUserParam(createUserRequestDTO);
    UserDTO userDTO = userService.create(createUserParam, multipartFile);
    CreateUserResponseDTO createdUser = userMapper.toCreateUserResponseDTO(userDTO);

    return ResponseEntity.ok(createdUser);
  }

  @Override
  @GetMapping("/{userId}")
  public ResponseEntity<UserResponseDTO> getUser(@PathVariable("userId") UUID id) {
    UserDTO userDTO = userService.find(id);
    UserResponseDTO userResponseDTO = userMapper.toUserResponseDTO(userDTO);

    return ResponseEntity.ok(userResponseDTO);
  }

  @Override
  @GetMapping
  public ResponseEntity<List<UserDTO>> getUserAll() {
    List<UserDTO> users = userService.findAll();

    return ResponseEntity.ok(users);
  }

  @Override
  @PatchMapping("/{userId}")
  public ResponseEntity<UpdateUserResponseDTO> updateUser(@PathVariable("userId") UUID id,
      @RequestPart("userUpdateRequest") @Valid UpdateUserRequestDTO updateUserRequestDTO,
      @RequestPart(value = "profile", required = false) MultipartFile multipartFile) {
    UpdateUserParam updateUserParam = userMapper.toUpdateUserParam(updateUserRequestDTO);
    UpdateUserDTO updateUserDTO = userService.update(id, updateUserParam, multipartFile);
    UpdateUserResponseDTO updatedUser = userMapper.toUpdateUserResponseDTO(updateUserDTO);

    return ResponseEntity.ok(updatedUser);
  }

  @Override
  @PatchMapping("/{userId}/userStatus")
  public ResponseEntity<UpdateUserStatusResponseDTO> updateUserStatus(
      @PathVariable("userId") UUID id) {
    UserStatus userStatus = userStatusService.updateByUserId(id);
    UpdateUserStatusResponseDTO updatedUserStatus = userStatusMapper.toUpdateUserStatusResponseDTO(
        userStatus);
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
