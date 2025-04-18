package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final UserStatusRepository userStatusRepository;

  @Override
  @Transactional
  public User create(UserCreateRequest userCreateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {

    validateDuplication(userCreateRequest.username(), userCreateRequest.email());

    BinaryContent profile = optionalProfileCreateRequest
        .map(req -> new BinaryContent(req.fileName(), (long) req.bytes().length, req.contentType(),
            req.bytes()))
        .orElse(null);

    UserStatus status = new UserStatus(null, Instant.now());

    User user = new User(
        userCreateRequest.username(),
        userCreateRequest.email(),
        userCreateRequest.password(),
        profile,
        status
    );

    User createdUser = userRepository.save(user);

    status.setUser(createdUser);
    userStatusRepository.save(status);

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
  @Transactional
  public User update(UUID userId, UserUpdateRequest userUpdateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

    if (userRepository.existsByEmail(userUpdateRequest.newEmail())) {
      throw new IllegalArgumentException("User with email already exists");
    }
    if (userRepository.existsByUsername(userUpdateRequest.newUsername())) {
      throw new IllegalArgumentException("User with username already exists");
    }

    BinaryContent newProfile = optionalProfileCreateRequest
        .map(req -> {
          Optional.ofNullable(user.getProfile())
              .ifPresent(binaryContentRepository::delete);
          return binaryContentRepository.save(new BinaryContent(
              req.fileName(), (long) req.bytes().length, req.contentType(), req.bytes()));
        })
        .orElse(null);

    user.update(
        userUpdateRequest.newUsername(),
        userUpdateRequest.newEmail(),
        userUpdateRequest.newPassword(),
        newProfile
    );

    return user;
  }

  @Override
  @Transactional
  public void delete(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

    Optional.ofNullable(user.getProfile())
        .ifPresent(binaryContentRepository::delete);

    userStatusRepository.deleteByUser(user);
    userRepository.delete(user);
  }

  private void validateDuplication(String username, String email) {
    if (userRepository.existsByEmail(email)) {
      throw new IllegalArgumentException("User with email already exists");
    }
    if (userRepository.existsByUsername(username)) {
      throw new IllegalArgumentException("User with username already exists");
    }
  }

  private UserDto toDto(User user) {
    Boolean online = userStatusRepository.findByUser(user)
        .map(UserStatus::isOnline)
        .orElse(null);

    UUID profileId = Optional.ofNullable(user.getProfile())
        .map(BinaryContent::getId)
        .orElse(null);

    return new UserDto(
        user.getId(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        user.getUsername(),
        user.getEmail(),
        profileId,
        online
    );
  }
}
