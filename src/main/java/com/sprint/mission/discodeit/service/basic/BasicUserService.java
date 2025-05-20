package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.domain.UserStatus;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
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
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final UserMapper userMapper;

  @Transactional
  @Override
  public UserDto createUser(UserCreateRequest request,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    String username = request.username();
    String email = request.email();
    String password = request.password();

    checkUserEmailExists(request.email());
    checkUserUsernameExists(request.username());

    BinaryContent nullableProfile = optionalProfileCreateRequest
        .map(profileRequest -> {
          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();

          BinaryContent binaryContent = BinaryContent.create(fileName, (long) bytes.length,
              contentType);
          BinaryContent createdBinaryContent = binaryContentRepository.save(binaryContent);
          binaryContentStorage.put(binaryContent.getId(), bytes);
          return createdBinaryContent;
        })
        .orElse(null);

    User user = User.create(username, email, password, nullableProfile);
    User createdUser = userRepository.save(user);

    UserStatus userStatus = UserStatus.create(user, Instant.now());
    user.assignStatus(userStatus);

    userStatusRepository.save(userStatus);

    boolean online = user.getStatus().isOnline();
    return userMapper.toDto(createdUser, online);
  }

  @Transactional(readOnly = true)
  @Override
  public UserDto findById(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));
    boolean online = user.getStatus().isOnline();

    return userMapper.toDto(user, online);
  }

  @Transactional(readOnly = true)
  @Override
  public UserDto findByUsername(String username) {
    return userRepository.findByUsername(username)
        .map(user -> {
          boolean online = user.getStatus().isOnline();
          return userMapper.toDto(user, online);
        })
        .orElse(null);
  }

  @Transactional(readOnly = true)
  @Override
  public UserDto findByEmail(String email) {
    return userRepository.findByEmail(email)
        .map(user -> {
          boolean online = user.getStatus().isOnline();
          return userMapper.toDto(user, online);
        })
        .orElse(null);
  }

  @Transactional(readOnly = true)
  @Override
  public List<UserDto> findAll() {
    return userRepository.findAll()
        .stream()
        .map(user -> {
          boolean online = user.getStatus().isOnline();
          return userMapper.toDto(user, online);
        })
        .collect(Collectors.toList());
  }

  @Transactional
  @Override
  public UserDto updateUser(UUID userId, UserUpdateRequest request,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));

    String newUsername = request.newUsername();
    String newEmail = request.newEmail();
    String newPassword = request.newPassword();

    checkUserEmailExists(request.newEmail());
    checkUserUsernameExists(request.newUsername());

    BinaryContent nullableProfile = optionalProfileCreateRequest
        .map(profileRequest -> {
          Optional.ofNullable(user.getProfile())
              .map(BinaryContent::getId)
              .ifPresent(id -> {
                binaryContentStorage.deleteById(id);
                binaryContentRepository.deleteById(id);
              });

          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();

          BinaryContent binaryContent = BinaryContent.create(fileName, (long) bytes.length,
              contentType);
          BinaryContent createdBinaryContent = binaryContentRepository.save(binaryContent);
          binaryContentStorage.put(binaryContent.getId(), bytes);
          return createdBinaryContent;
        })
        .orElse(null);

    user.update(newUsername, newEmail, newPassword, nullableProfile);
    boolean online = user.getStatus().isOnline();

    return userMapper.toDto(user, online);
  }

  @Transactional
  @Override
  public void deleteUser(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));

    if (user.getProfile() != null && user.getProfile().getId() != null) {
      UUID profileId = user.getProfile().getId();
      binaryContentStorage.deleteById(profileId);
      binaryContentRepository.deleteById(profileId);
    }

    UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
        .orElseThrow(() -> new NoSuchElementException("해당 사용자의 UserStatus를 찾을 수 없습니다: " + userId));
    userStatusRepository.deleteById(userStatus.getId());

    userRepository.deleteById(userId);
  }


  /****************************
   * Validation check
   ****************************/
  private void checkUserEmailExists(String email) {
    if (userRepository.existsByEmail(email)) {
      throw UserAlreadyExistException.byEmail(email);
    }
  }

  private void checkUserUsernameExists(String username) {
    if (userRepository.existsByUsername(username)) {
      throw UserAlreadyExistException.byUsername(username);
    }
  }

}
