package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final UserStatusRepository userStatusRepository;

  @Override
  public User createUser(CreateUserRequest request) {
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

    userRepository.addUser(user);
    UserStatus userStatus = UserStatus.builder()
        .userid(user.getId())
        .lastActiveAt(Instant.now())
        .build();
    userStatusRepository.addUserStatus(userStatus);

    return user;
  }

  @Override
  public User findUserById(UUID userId) {
    return userRepository.findUserById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id: " + userId + "not found"));
  }

  @Override
  public String findUserNameById(UUID userId) {
    return findUserById(userId).getUsername();
  }

  @Override
  public List<UserDto> findUsersByIds(Set<UUID> userIds) {
    return userRepository.findUsersByIds(userIds).stream()
        .map(this::mapToDto)
        .collect(Collectors.toList());
  }

  @Override
  public List<UserDto> getAllUsers() {
    return userRepository.findUserAll().stream()
        .map(this::mapToDto)
        .collect(Collectors.toList());
  }

  @Override
  public BinaryContent findProfileById(UUID userId) {
    return binaryContentRepository.findBinaryContentById(findUserById(userId).getProfile().getId())
        .orElse(null);
  }

  @Override
  public User updateProfile(UUID userId, BinaryContent binaryContent) {
    findUserById(userId).updateProfile(binaryContent);
    userRepository.save();

    return findUserById(userId);
  }

  @Override
  public User updateUser(UUID userId, UpdateUserRequest request) {
    User user = findUserById(userId);

    if (request.newUsername() != null) {
      user.updateUsername(request.newUsername());
    }
    if (request.newPassword() != null) {
      user.updatePassword(request.newPassword());
    }
    if (request.newEmail() != null) {
      user.updateEmail(request.newEmail());
    }

    return user;
  }

  @Override
  public void deleteUser(UUID userId) {
    userRepository.deleteUserById(userId);
    userStatusRepository.deleteUserStatusById(userId);
    binaryContentRepository.deleteBinaryContentById(userId);
  }

  @Override
  public void validateUserExists(UUID userId) {
    if (!userRepository.existsById(userId)) {
      throw new RuntimeException("존재하지 않는 유저입니다.");
    }
  }

  @Override
  public UserDto mapToDto(User user) {
    Boolean isOnline = userStatusRepository.findUserStatusById(user.getId())
        .map(UserStatus::isUserOnline)
        .orElse(null);

    return UserDto.from(user, isOnline);
  }

}
