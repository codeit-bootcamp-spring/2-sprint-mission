package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.controller.dto.UserCreateRequest;
import com.sprint.mission.discodeit.controller.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.controller.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity._BinaryContent;
import com.sprint.mission.discodeit.entity._User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  //
  private final BinaryContentRepository binaryContentRepository;
  private final UserStatusRepository userStatusRepository;

  @Override
  public _User create(UserCreateRequest userCreateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    String username = userCreateRequest.getUsername();
    String email = userCreateRequest.getEmail();

    if (userRepository.existsByEmail(email)) {
      throw new IllegalArgumentException("User with email " + email + " already exists");
    }
    if (userRepository.existsByUsername(username)) {
      throw new IllegalArgumentException("User with username " + username + " already exists");
    }

    UUID nullableProfileId = optionalProfileCreateRequest
        .map(profileRequest -> {
          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();
          _BinaryContent binaryContent = new _BinaryContent(fileName, (long) bytes.length,
              contentType, bytes);
          return binaryContentRepository.save(binaryContent).getId();
        })
        .orElse(null);
    String password = userCreateRequest.getPassword();

    _User user = new _User(username, email, password, nullableProfileId);
    _User createdUser = userRepository.save(user);

    OffsetDateTime now = OffsetDateTime.now();
    UserStatus userStatus = new UserStatus(createdUser.getId(), now);
    userStatusRepository.save(userStatus);

    return createdUser;
  }

  @Override
  public UserDto find(UUID userId) {
    return userRepository.findById(userId)
        .map(this::toDto)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
  }

  @Override
  public List<UserDto> findAll() {
    return userRepository.findAll()
        .stream()
        .map(this::toDto)
        .toList();
  }

  @Override
  public _User update(UUID userId, UserUpdateRequest userUpdateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    _User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

    String newUsername = userUpdateRequest.getNewUsername();
    String newEmail = userUpdateRequest.getNewEmail();
    if (userRepository.existsByEmail(newEmail)) {
      throw new IllegalArgumentException("User with email " + newEmail + " already exists");
    }
    if (userRepository.existsByUsername(newUsername)) {
      throw new IllegalArgumentException("User with username " + newUsername + " already exists");
    }

    UUID nullableProfileId = optionalProfileCreateRequest
        .map(profileRequest -> {
          Optional.ofNullable(user.getProfileId())
              .ifPresent(binaryContentRepository::deleteById);

          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();
          _BinaryContent binaryContent = new _BinaryContent(fileName, (long) bytes.length,
              contentType, bytes);
          return binaryContentRepository.save(binaryContent).getId();
        })
        .orElse(null);

    String newPassword = userUpdateRequest.getNewPassword();
    user.update(newUsername, newEmail, newPassword, nullableProfileId);

    return userRepository.save(user);
  }

  @Override
  public void delete(UUID userId) {
    _User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

    Optional.ofNullable(user.getProfileId())
        .ifPresent(binaryContentRepository::deleteById);
    userStatusRepository.deleteByUserId(userId);

    userRepository.deleteById(userId);
  }

  private UserDto toDto(_User user) {
    Boolean online = userStatusRepository.findByUserId(user.getId())
        .map(UserStatus::isOnline)
        .orElse(null);

    return new UserDto(
        user.getId(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        user.getUsername(),
        user.getEmail(),
        user.getProfileId(),
        online
    );
  }
}
