package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserInfoDto;
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

  private void saveUser() {
    userRepository.save();
  }

  @Override
  public User createUser(CreateUserRequest request) {
    String hashedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());

    if (userRepository.existsByEmail(request.getEmail())) {
      throw new IllegalArgumentException("이미 존재하는 Email입니다");
    }
    if (userRepository.existsByUsername(request.getUsername())) {
      throw new IllegalArgumentException("이미 존재하는 Username입니다");
    }

    User user = new User(request.getUsername(), request.getEmail(), hashedPassword);
    userRepository.addUser(user);
    userStatusRepository.addUserStatus(new UserStatus(user.getId(), Instant.now()));

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
  public List<UserInfoDto> findUsersByIds(Set<UUID> userIds) {
    return userRepository.findUsersByIds(userIds).stream()
        .map(this::mapToDto)
        .collect(Collectors.toList());
  }

  @Override
  public List<UserInfoDto> getAllUsers() {
    return userRepository.findUserAll().stream()
        .map(this::mapToDto)
        .collect(Collectors.toList());
  }

  @Override
  public BinaryContent findProfileById(UUID userId) {
    return binaryContentRepository.findBinaryContentById(findUserById(userId).getProfileId())
        .orElse(null);
  }

  @Override
  public User updateProfile(UUID userId, UUID profileId) {
    findUserById(userId).updateProfile(profileId);
    userRepository.save();

    return findUserById(userId);
  }

  @Override
  public User updateUser(UUID userId, UpdateUserRequest request) {
    User user = findUserById(userId);

    if (request.getNewUsername() != null) {
      user.updateUsername(request.getNewUsername());
    }
    if (request.getNewPassword() != null) {
      user.updatePassword(request.getNewPassword());
    }
    if (request.getNewEmail() != null) {
      user.updateEmail(request.getNewEmail());
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
  public UserInfoDto mapToDto(User user) {
    Boolean isOnline = userStatusRepository.findUserStatusById(user.getId())
        .map(UserStatus::isUserOnline)
        .orElse(null);

    UserInfoDto dto = new UserInfoDto();
    dto.setId(user.getId());
    dto.setCreateAt(user.getCreatedAt());
    dto.setUpdateAt(user.getUpdatedAt());
    dto.setUsername(user.getUsername());
    dto.setEmail(user.getEmail());
    dto.setOnline(isOnline);
    dto.setProfileId(user.getProfileId());
    return dto;
  }

}
