package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.User.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final UserMapper userMapper;
  private final BinaryContentService binaryContentService;
  private final UserStatusService userStatusService;

  @Override
  @Transactional
  public UserDto createUser(CreateUserRequest request, MultipartFile profile) {
    log.info("Create user requested: {}", request);
    String hashedPassword = BCrypt.hashpw(request.password(), BCrypt.gensalt());

    if (userRepository.existsByEmail(request.email())) {
      log.warn("User created failed: Email already exists - {}", request.email());
      throw new DiscodeitException(ErrorCode.EMAIL_ALREADY_EXISTS);
    }
    if (userRepository.existsByUsername(request.username())) {
      log.warn("User created failed: Username already exists - {}", request.username());
      throw new DiscodeitException(ErrorCode.USERNAME_ALREADY_EXISTS);
    }

    User user = User.builder()
        .username(request.username())
        .email(request.email())
        .password(hashedPassword)
        .build();

    userRepository.save(user);

    UserStatus userStatus = userStatusService.create(user);
    userStatusRepository.save(userStatus);
    user.setStatus(userStatus);

    if (profile != null && !profile.isEmpty()) {
      BinaryContent binaryContent = binaryContentService.createBinaryContent(profile);
      updateProfile(user, binaryContent);
    }

    log.info("User created successfully: {}", user.getUsername());
    return userMapper.toDto(user);
  }

  @Override
  public UserDto findUserById(UUID userId) {
    return userMapper.toDto(findUserOrThrow(userId));
  }

  @Override
  public String findUserNameById(UUID userId) {
    return findUserById(userId).username();
  }

  @Override
  public List<UserDto> findUsersByIds(Set<UUID> userIds) {
    return userRepository.findByIdIn(userIds).stream()
        .map(userMapper::toDto)
        .toList();
  }

  @Override
  public List<UserDto> getAllUsers() {
    return userRepository.findAllWithDetails().stream()
        .map(userMapper::toDto)
        .toList();
  }

  @Override
  public BinaryContent findProfileById(UUID userId) {
    return findUserOrThrow(userId).getProfile();
  }

  private void updateProfile(User user, BinaryContent binaryContent) {
    log.info("Update profile by id: {}", user.getId());
    user.updateProfile(binaryContent);
    log.info("User profile updated successfully: {}", user);
  }

  @Transactional
  @Override
  public UserDto updateUser(UUID userId, UpdateUserRequest request, MultipartFile profile) {
    log.info("Update user by id: {}, request: {}", userId, request);
    User user = findUserOrThrow(userId);

    if (userRepository.existsByEmail(request.newEmail())) {
      log.warn("User updated failed: Email already exists - {}", request.newEmail());
      throw new DiscodeitException(ErrorCode.EMAIL_ALREADY_EXISTS);
    }
    if (userRepository.existsByUsername(request.newUsername())) {
      log.warn("User updated failed: Username already exists - {}", request.newUsername());
      throw new DiscodeitException(ErrorCode.USERNAME_ALREADY_EXISTS);
    }

    if (request.newUsername() != null) {
      user.updateUsername(request.newUsername());
    }
    if (request.newPassword() != null) {
      user.updatePassword(request.newPassword());
    }
    if (request.newEmail() != null) {
      user.updateEmail(request.newEmail());
    }
    if (profile != null && !profile.isEmpty()) {
      BinaryContent binaryContent = binaryContentService.createBinaryContent(profile);
      updateProfile(user, binaryContent);
    }

    log.info("User updated successfully: {}", userId);
    return userMapper.toDto(user);
  }

  @Override
  public void deleteUser(UUID userId) {
    log.warn("Delete user by id: {}", userId);
    validateUserExists(userId);
    userRepository.deleteById(userId);
    log.info("User deleted successfully: {}", userId);
  }

  @Override
  public void validateUserExists(UUID userId) {
    if (!userRepository.existsById(userId)) {
      log.warn("Validate user by id: {}", userId);
      throw new UserNotFoundException(userId);
    }
  }

  private User findUserOrThrow(UUID userId) {
    return userRepository.findWithDetailsById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));
  }
}
