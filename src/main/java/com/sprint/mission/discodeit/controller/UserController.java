package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.controller.user.*;
import com.sprint.mission.discodeit.dto.service.user.CreateUserCommand;
import com.sprint.mission.discodeit.dto.service.user.CreateUserResult;
import com.sprint.mission.discodeit.dto.service.user.FindUserResult;
import com.sprint.mission.discodeit.dto.service.user.UpdateUserCommand;
import com.sprint.mission.discodeit.dto.service.user.UpdateUserResult;
import com.sprint.mission.discodeit.dto.service.userStatus.UpdateUserStatusResult;
import com.sprint.mission.discodeit.exception.file.ProfileFileTypeException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.swagger.UserApi;
import com.sprint.mission.discodeit.util.MaskingUtil;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Slf4j
public class UserController implements UserApi {

  private static final Set<String> ALLOWED_MIME_TYPES = Set.of("image/jpeg", "image/png",
      "image/gif", "image/webp");
  private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif", "webp");

  private final UserService userService;
  private final UserStatusService userStatusService;
  private final UserMapper userMapper;
  private final UserStatusMapper userStatusMapper;
  private final Tika tika = new Tika();

  @Override
  @PostMapping
  public ResponseEntity<CreateUserResponseDTO> createUser(
      @RequestPart("userCreateRequest") @Valid CreateUserRequestDTO createUserRequestDTO,
      @RequestPart(value = "profile", required = false) MultipartFile multipartFile) {
    log.info("User create request (username: {}, email: {}, profileSize: {})",
        // username이나 email은 개인 정보이므로, 로그에 노출하면 위험 + 로그에 userId를 사용할 수 없는 로직 -> 마스킹하여 처리
        MaskingUtil.maskUsername(createUserRequestDTO.username()),
        MaskingUtil.maskEmail(createUserRequestDTO.email()),
        multipartFile != null ? multipartFile.getSize() : 0);
    validateProfileImageType(multipartFile);
    CreateUserCommand createUserCommand = userMapper.toCreateUserCommand(createUserRequestDTO);
    CreateUserResult createUserResult = userService.create(createUserCommand, multipartFile);
    CreateUserResponseDTO createdUser = userMapper.toCreateUserResponseDTO(createUserResult);

    return ResponseEntity.ok(createdUser);
  }

  // 단순 조회는 진입 로그를 남기지 않음 (API 호출이 상대적으로 많고, 비즈니스적으로 로그의 중요성이 떨어짐)
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
    log.info("User update request (userId: {},profile size: {})", id,
        multipartFile != null ? multipartFile.getSize() : 0);
    validateProfileImageType(multipartFile);
    UpdateUserCommand updateUserCommand = userMapper.toUpdateUserCommand(updateUserRequestDTO);
    UpdateUserResult updateUserResult = userService.update(id, updateUserCommand, multipartFile);
    UpdateUserResponseDTO updatedUser = userMapper.toUpdateUserResponseDTO(updateUserResult);

    return ResponseEntity.ok(updatedUser);
  }

  @Override
  @PatchMapping("/{userId}/userStatus")
  public ResponseEntity<UpdateUserStatusResponseDTO> updateUserStatus(
      @PathVariable("userId") UUID id) {
    log.info("UserStatus update request (userId: {})", id);
    UpdateUserStatusResult updateUserStatusResult = userStatusService.updateByUserId(id);
    UpdateUserStatusResponseDTO updatedUserStatus = userStatusMapper.toUpdateUserStatusResponseDTO(
        updateUserStatusResult);
    return ResponseEntity.ok(updatedUserStatus);
  }

  @Override
  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable("userId") UUID id) {
    log.info("User delete request (userId: {})", id);
    userService.delete(id);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  private void validateProfileImageType(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      return;
    }
    // 1. MIME 타입 검증 (Tika로 실제 파일 내용 기반 검사)
    try {
      String mimeType = tika.detect(file.getInputStream());
      if (!ALLOWED_MIME_TYPES.contains(mimeType)) {
        throw new ProfileFileTypeException(Map.of("mimeType", mimeType));
      }
    } catch (IOException e) {
      throw new ProfileFileTypeException();
    }

    // 2. 확장자 검증 (이중 체크)
    String extension = FilenameUtils.getExtension(file.getOriginalFilename());
    if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
      throw new ProfileFileTypeException(Map.of("extension", extension));
    }
  }
}
