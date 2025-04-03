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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Tag(name = "User-Controller", description = "User 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;
  private final UserMapper userMapper;
  private final UserStatusMapper userStatusMapper;

  @Operation(summary = "회원가입",
      description = "User를 생성합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "유저 회원가입 성공"),
          @ApiResponse(responseCode = "400", description = "필드가 올바르게 입력되지 않음 (DTO 유효성 검증 실패)"),
          @ApiResponse(responseCode = "404", description = "binaryContentId에 해당하는 BinaryContent를 찾지 못함"),
          @ApiResponse(responseCode = "409", description = "중복된 username 또는 email 입력됨"),
          @ApiResponse(responseCode = "500", description = "프로필 파일 읽기에 실패함"),

      })
  @PostMapping
  public ResponseEntity<CreateUserResponseDTO> createUser(
      @RequestPart("userCreateRequest") @Valid CreateUserRequestDTO createUserRequestDTO,
      @RequestPart(value = "profile", required = false) MultipartFile multipartFile) {
    CreateUserParam createUserParam = userMapper.toCreateUserParam(createUserRequestDTO);
    UserDTO userDTO = userService.create(createUserParam, multipartFile);
    CreateUserResponseDTO createdUser = userMapper.toCreateUserResponseDTO(userDTO);

    return ResponseEntity.ok(createdUser);
  }

  @Operation(summary = "유저 단건 조회",
      description = "userId에 해당하는 유저를 조회합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "유저 단건 조회 성공"),
          @ApiResponse(responseCode = "404", description = "userId에 해당하는 User를 찾지 못함")
      })
  @GetMapping("/{userId}")
  public ResponseEntity<UserResponseDTO> getUser(@PathVariable("userId") UUID id) {
    UserDTO userDTO = userService.find(id);
    UserResponseDTO userResponseDTO = userMapper.toUserResponseDTO(userDTO);

    return ResponseEntity.ok(userResponseDTO);
  }

  @Operation(summary = "유저 다건 조회",
      description = "모든 유저를 조회합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "유저 다건 조회 성공")
      })
  @GetMapping
  public ResponseEntity<List<UserDTO>> getUserAll() {
    List<UserDTO> users = userService.findAll();

    return ResponseEntity.ok(users);
  }

  @Operation(summary = "유저 수정",
      description = "userId에 해당하는 유저를 수정합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "유저 수정 성공"),
          @ApiResponse(responseCode = "404", description = "userId에 해당하는 User를 찾지 못함")
      })
  @PatchMapping("/{userId}")
  public ResponseEntity<UpdateUserResponseDTO> updateUser(@PathVariable("userId") UUID id,
      @RequestPart("user") @Valid UpdateUserRequestDTO updateUserRequestDTO,
      @RequestPart(value = "file", required = false) MultipartFile multipartFile) {
    UpdateUserParam updateUserParam = userMapper.toUpdateUserParam(updateUserRequestDTO);
    UpdateUserDTO updateUserDTO = userService.update(id, updateUserParam, multipartFile);
    UpdateUserResponseDTO updatedUser = userMapper.toUpdateUserResponseDTO(updateUserDTO);

    return ResponseEntity.ok(updatedUser);
  }

  @Operation(summary = "유저상태 수정",
      description = "userId에 해당하는 유저상태를 수정합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "유저상태 수정 성공"),
          @ApiResponse(responseCode = "404", description = "userStatusId에 해당하는 UserStatus를 찾지 못함")
      })
  @PatchMapping("/{userId}/userStatus")
  public ResponseEntity<UpdateUserStatusResponseDTO> updateUserStatus(
      @PathVariable("userId") UUID id) {
    UserStatus userStatus = userStatusService.updateByUserId(id);
    UpdateUserStatusResponseDTO updatedUserStatus = userStatusMapper.toUpdateUserStatusResponseDTO(
        userStatus);
    return ResponseEntity.ok(updatedUserStatus);
  }

  @Operation(summary = "유저 삭제",
      description = "userId로 유저를 삭제합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "유저 삭제 성공")
      })
  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable("userId") UUID id) {
    userService.delete(id);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }
}
