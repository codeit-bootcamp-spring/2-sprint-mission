package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.controller.dto.UserCreateRequest;
import com.sprint.mission.discodeit.controller.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.controller.dto.UserDto;
import com.sprint.mission.discodeit.controller.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity._BinaryContent;
import com.sprint.mission.discodeit.entity._User;
import com.sprint.mission.discodeit.entity._UserStatus;
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
    String username = userCreateRequest.getUsername().toString();
    String email = userCreateRequest.getEmail().toString();

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
    String password = userCreateRequest.getPassword().toString();

    _User user = new _User(username, email, password, nullableProfileId);
    _User createdUser = userRepository.save(user);

    OffsetDateTime now = OffsetDateTime.now();
    _UserStatus userStatus = new _UserStatus(createdUser.getId(), now);
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

    String newUsername = userUpdateRequest.getNewUsername().toString();
    String newEmail = userUpdateRequest.getNewEmail().toString();
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

    String newPassword = userUpdateRequest.getNewPassword().toString();
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
        .map(_UserStatus::isOnline)
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
