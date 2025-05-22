package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.service.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.service.user.UserDto;
import com.sprint.mission.discodeit.dto.service.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final BasicBinaryContentService basicBinaryContentService;
  private final UserMapper userMapper;

  @Override
  @Transactional
  public UserDto create(UserCreateRequest createRequest,
      BinaryContentCreateRequest binaryRequest) {
    log.debug("사용자 생성 시작: request={}, file={}", createRequest, binaryRequest);
    String username = createRequest.username();
    String email = createRequest.email();

    validDuplicateUsername(username);
    validDuplicateEmail(email);

    BinaryContent profile = (binaryRequest != null)
        ? basicBinaryContentService.create(binaryRequest) : null;
    User user = new User(username, email, createRequest.password(), profile);
    new UserStatus(user, Instant.now());
    userRepository.save(user);
    log.info("사용자 생성 완료: id={}, username={}, email={}", user.getId(), username, email);

    return userMapper.toDto(user);

  }

  @Override
  public UserDto find(UUID userId) {
    User user = userRepository.findByIdWithProfileAndUserStatus(userId)
        .orElseThrow(() -> new UserNotFoundException().notFoundWithId(userId));
    return userMapper.toDto(user);
  }

  @Override
  public List<UserDto> findAll() {
    List<User> userList = userRepository.findAllWithProfileAndUserStatus();
    return userMapper.toDtoList(userList);
  }

  @Override
  @Transactional
  public UserDto update(UUID userId, UserUpdateRequest updateRequest,
      BinaryContentCreateRequest binaryRequest) {
    log.debug("사용자 수정 시작: id={}, request={}, file={}", userId, updateRequest, binaryRequest);
    User user = userRepository.findByIdWithProfileAndUserStatus(userId)
        .orElseThrow(() -> new UserNotFoundException().notFoundWithId(userId));
    String newUsername = updateRequest.newUsername();
    String newEmail = updateRequest.newEmail();
    String newPassword = updateRequest.newPassword();

    if (!user.getUsername().equals(newUsername)) {
      validDuplicateUsername(newUsername);
    }
    if (!user.getEmail().equals(newEmail)) {
      validDuplicateEmail(newEmail);
    }

    BinaryContent newProfile = (binaryRequest != null)
        ? basicBinaryContentService.create(binaryRequest) : null;
    user.update(newUsername, newEmail, newPassword, newProfile);
    log.info("사용자 수정 완료: id={}, username={}, email={}", userId, newUsername, newEmail);

    return userMapper.toDto(user);
  }

  @Override
  @Transactional
  public void delete(UUID userId) {
    log.debug("사용자 삭제 시작: id={}", userId);
    if (!userRepository.existsById(userId)) {
      throw new UserNotFoundException().notFoundWithId(userId);
    }
    userRepository.deleteById(userId);
    log.info("사용자 삭제 완료: id={}", userId);
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  private void validDuplicateUsername(String username) {
    if (userRepository.existsByUsername(username)) {
      throw new UserAlreadyExistsException().duplicateUsername(username);
    }
  }

  private void validDuplicateEmail(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new UserAlreadyExistsException().duplicateEmail(email);
    }
  }
}