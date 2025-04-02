package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.controller.user.*;
import com.sprint.mission.discodeit.dto.service.user.CreateUserParam;
import com.sprint.mission.discodeit.dto.service.user.UpdateUserDTO;
import com.sprint.mission.discodeit.dto.service.user.UpdateUserParam;
import com.sprint.mission.discodeit.dto.service.user.UserDTO;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.RestExceptions;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;
  private final UserMapper userMapper;
  private final UserStatusMapper userStatusMapper;

  @PostMapping
  public ResponseEntity<CreateUserResponseDTO> createUser(
      @RequestPart("user") @Valid CreateUserRequestDTO createUserRequestDTO,
      @RequestPart(value = "file", required = false) MultipartFile multipartFile) {
    CreateUserParam createUserParam = userMapper.toCreateUserParam(createUserRequestDTO);
    UserDTO userDTO = null;
    userDTO = userService.create(createUserParam, multipartFile);
    CreateUserResponseDTO createUserResponseDTO = userMapper.toCreateUserResponseDTO(userDTO);

    return ResponseEntity.ok(createUserResponseDTO);
  }

  @GetMapping("/{userId}")
  public ResponseEntity<UserResponseDTO> getUser(@PathVariable("userId") UUID id) {
    UserDTO userDTO = userService.find(id);
    UserResponseDTO userResponseDTO = userMapper.toUserResponseDTO(userDTO);

    return ResponseEntity.ok(userResponseDTO);
  }

  @GetMapping
  public ResponseEntity<UserListResponseDTO> getUserAll() {
    List<UserDTO> userDTOList = userService.findAll();
    UserListResponseDTO userListResponseDTO = new UserListResponseDTO(userDTOList);

    return ResponseEntity.ok(userListResponseDTO);
  }

  @PutMapping("/{userId}")
  public ResponseEntity<UpdateUserResponseDTO> updateUser(@PathVariable("userId") UUID id,
      @RequestPart("user") @Valid UpdateUserRequestDTO updateUserRequestDTO,
      @RequestPart(value = "file", required = false) MultipartFile multipartFile) {
    UpdateUserParam updateUserParam = userMapper.toUpdateUserParam(updateUserRequestDTO);
    UpdateUserDTO updateUserDTO = userService.update(id, updateUserParam, multipartFile);

    return ResponseEntity.ok(userMapper.toUpdateUserResponseDTO(updateUserDTO));
  }

  @PutMapping("/{userId}/userstatus")
  public ResponseEntity<UpdateUserStatusResponseDTO> updateUserStatus(
      @PathVariable("userId") UUID id) {
    UserStatus userStatus = userStatusService.updateByUserId(id);
    return ResponseEntity.ok(userStatusMapper.toUpdateUserStatusResponseDTO(userStatus));
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<DeleteUserResponseDTO> deleteUser(@PathVariable("userId") UUID id) {
    userService.delete(id);
    return ResponseEntity.ok(new DeleteUserResponseDTO(id, id + "번 회원 삭제 완료"));
  }
}
