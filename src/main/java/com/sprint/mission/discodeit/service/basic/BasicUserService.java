package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mepper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final UserMapper userMapper;

  @Override
  public UserDto createUser(CreateUserRequest request) {
    String hashedPassword = BCrypt.hashpw(request.password(), BCrypt.gensalt());

    if (userRepository.existsByEmail(request.email())) {
      throw new IllegalArgumentException("이미 존재하는 Email입니다");
    }
    if (userRepository.existsByUsername(request.username())) {
      throw new IllegalArgumentException("이미 존재하는 Username입니다");
    }

    User user = User.builder()
        .username(request.username())
        .email(request.email())
        .password(hashedPassword)
        .build();

    UserStatus userStatus = UserStatus.builder()
        .user(user)
        .lastActiveAt(Instant.now())
        .build();

    userRepository.save(user);
    userStatusRepository.save(userStatus);

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
    return userRepository.findAll().stream()
        .map(userMapper::toDto)
        .toList();
  }

  @Override
  public BinaryContent findProfileById(UUID userId) {
    return findUserOrThrow(userId).getProfile();
  }

  @Transactional
  @Override
  public UserDto updateProfile(UUID userId, BinaryContent binaryContent) {
    User user = findUserOrThrow(userId);
    user.updateProfile(binaryContent);
    return userMapper.toDto(user);
  }

  @Transactional
  @Override
  public UserDto updateUser(UUID userId, UpdateUserRequest request) {
    User user = findUserOrThrow(userId);

    if (request.newUsername() != null) {
      user.updateUsername(request.newUsername());
    }
    if (request.newPassword() != null) {
      user.updatePassword(request.newPassword());
    }
    if (request.newEmail() != null) {
      user.updateEmail(request.newEmail());
    }

    return userMapper.toDto(user);
  }

  @Override
  public void deleteUser(UUID userId) {
    validateUserExists(userId);
    userRepository.deleteById(userId);
  }

  @Override
  public void validateUserExists(UUID userId) {
    if (!userRepository.existsById(userId)) {
      throw new NoSuchElementException("UserId: " + userId + " not found");
    }
  }

  private User findUserOrThrow(UUID userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("UserId: " + userId + " not found"));
  }
}
