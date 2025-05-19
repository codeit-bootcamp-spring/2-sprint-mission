package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.DuplicateEmailException;
import com.sprint.mission.discodeit.exception.user.DuplicateUsernameException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final UserMapper userMapper;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;

  @Transactional
  @Override
  public UserDto create(UserCreateRequest userCreateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    String username = userCreateRequest.username();
    String email = userCreateRequest.email();

    if (userRepository.existsByEmail(email)) {
      log.warn("이미 존재하는 Email로 생성 시도: {}", email);
      Map<String, Object> details = new HashMap<>();
      details.put("email", email);
      throw new DuplicateEmailException(Instant.now(), ErrorCode.DUPLICATE_EMAIL, details);
    }
    if (userRepository.existsByUsername(username)) {
      log.warn("이미 존재하는 Username으로 생성 시도: {}", username);
      Map<String, Object> details = new HashMap<>();
      details.put("username", username);
      throw new DuplicateUsernameException(Instant.now(), ErrorCode.DUPLICATE_USERNAME, details);
    }

    BinaryContent nullableProfile = optionalProfileCreateRequest
        .map(profileRequest -> {
          log.info("프로필 이미지 저장 시도 - 파일명: {}", profileRequest.fileName());

          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();
          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType);
          binaryContentRepository.save(binaryContent);
          binaryContentStorage.put(binaryContent.getId(), bytes);

          log.info("프로필 이미지 저장 완료 - ID: {}", binaryContent.getId());

          return binaryContent;
        })
        .orElse(null);
    String password = userCreateRequest.password();

    User user = new User(username, email, password, nullableProfile);
    Instant now = Instant.now();
    UserStatus userStatus = new UserStatus(user, now);

    userRepository.save(user);
    return userMapper.toDto(user);
  }

  @Override
  public UserDto find(UUID userId) {
    return userRepository.findById(userId)
        .map(userMapper::toDto)
        .orElseThrow(() -> {
          log.warn("user 찾기 실패 - ID: {}", userId);
          Map<String, Object> details = new HashMap<>();
          details.put("userId", userId);
          return new UserNotFoundException(Instant.now(), ErrorCode.USER_NOT_FOUND, details);
        });
  }

  @Override
  public List<UserDto> findAll() {
    return userRepository.findAllWithProfileAndStatus()
        .stream()
        .map(userMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.warn("User 수정 실패 - ID: {} - 존재하지 않는 사용자", userId);
          Map<String, Object> details = new HashMap<>();
          details.put("userId", userId);
          return new UserNotFoundException(Instant.now(), ErrorCode.USER_NOT_FOUND, details);
        });

    String newUsername = userUpdateRequest.newUsername();
    String newEmail = userUpdateRequest.newEmail();
    if (userRepository.existsByEmail(newEmail)) {
      log.warn("이미 존재하는 Email로 수정 시도: {}", newEmail);
      Map<String, Object> details = new HashMap<>();
      details.put("email", newEmail);
      throw new DuplicateEmailException(Instant.now(), ErrorCode.DUPLICATE_EMAIL, details);
    }
    if (userRepository.existsByUsername(newUsername)) {
      log.warn("이미 존재하는 Username으로 수정 시도: {}", newUsername);
      Map<String, Object> details = new HashMap<>();
      details.put("username", newUsername);
      throw new DuplicateUsernameException(Instant.now(), ErrorCode.DUPLICATE_USERNAME, details);
    }

    BinaryContent nullableProfile = optionalProfileCreateRequest
        .map(profileRequest -> {
          log.info("프로필 이미지 업데이트 시도 - 파일명: {}", profileRequest.fileName());

          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();
          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType);
          binaryContentRepository.save(binaryContent);
          binaryContentStorage.put(binaryContent.getId(), bytes);

          log.info("프로필 이미지 업데이트 완료 - ID: {}", binaryContent.getId());

          return binaryContent;
        })
        .orElse(null);

    String newPassword = userUpdateRequest.newPassword();
    user.update(newUsername, newEmail, newPassword, nullableProfile);

    return userMapper.toDto(user);
  }

  @Transactional
  @Override
  public void delete(UUID userId) {
    if (userRepository.existsById(userId)) {
      log.warn("User 삭제 실패 - ID: {} - 존재하지 않는 사용자", userId);
      Map<String, Object> details = new HashMap<>();
      details.put("userId", userId);
      throw new UserNotFoundException(Instant.now(), ErrorCode.USER_NOT_FOUND, details);
    }
    userRepository.deleteById(userId);
  }
}
