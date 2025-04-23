package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final UserStatusRepository userStatusRepository;

  @Override
  @Transactional
  public User create(UserCreateRequest request,
      Optional<BinaryContentCreateRequest> optionalBinaryContentCreateRequest) {
    // username과 email은 다른 유저와 같으면 안됩니다.
    validationUserCreateRequest(request);

    BinaryContent nullableProfile = optionalBinaryContentCreateRequest
        .map(profileRequest -> {
          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();
          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType);
          binaryContentRepository.save(binaryContent);
          return binaryContent;
        })
        .orElse(null);

    User user = new User(
        request.username(),
        request.email(),
        request.password(),
        nullableProfile
    );

    // UserStatus 를 같이 생성합니다.
    UserStatus userStatus = new UserStatus(user, Instant.now());
    user.setStatus(userStatus);
    userStatusRepository.save(userStatus);

    return userRepository.save(user);
  }

  @Override
  public UserDto find(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

    return toDto(user);
  }

  @Override
  public List<UserDto> findAll() {
    return userRepository.findAll().stream()
        .map(user -> {
          UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
              .orElseThrow(
                  () -> new NoSuchElementException("User with id " + user.getId() + " not found"));

          return toDto(user);
        }).toList();
  }

  @Override
  public User update(UUID userId, UserUpdateRequest request,
      Optional<BinaryContentCreateRequest> optionalBinaryContentRequest) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

    user.update(
        request.newUsername().orElse(user.getUsername()),
        request.newEmail().orElse(user.getEmail()),
        request.newPassword().orElse(user.getPassword())
    );

    // 업데이트 할 바이너리 컨텐츠가 있으면 교체
    BinaryContent nullableProfile = optionalBinaryContentRequest.ifPresent(binaryContentRequest -> {
      BinaryContent binaryContent = new BinaryContent(
          binaryContentRequest.fileName(),
          (long) binaryContentRequest.bytes().length,
          binaryContentRequest.contentType()
      );
      binaryContentRepository.save(binaryContent);
      return binaryContent;
    }).orElse(null);

    return userRepository.save(user);
  }

  @Override
  public void delete(UUID userId) {
    if (!userRepository.existsById(userId)) {
      throw new NoSuchElementException("User with id " + userId + " not found");
    }

    // 관련된 도메인도 같이 삭제합니다.
    // BinaryContent(프로필), UserStatus
    userRepository.findById(userId)
        .ifPresent(user -> {
          if (user.getProfile() != null) {
            binaryContentRepository.delete(user.getProfile().getId());
          }
        });
    userStatusRepository.findByUserId(userId)
        .ifPresent(status -> userStatusRepository.delete(status.getId()));

    userRepository.deleteById(userId);
  }

  private void validationUserCreateRequest(UserCreateRequest userCreateRequest) {
    // username과 email은 다른 유저와 같으면 안됩니다.
    boolean usernameExists = (userRepository.findAll().stream()
        .anyMatch(u -> u.getUsername().equals(userCreateRequest.username())));

    boolean emailExists = userRepository.findAll().stream()
        .anyMatch(u -> u.getEmail().equals(userCreateRequest.email()));

    if (usernameExists) {
      throw new IllegalArgumentException("Username already exists");
    }

    if (emailExists) {
      throw new IllegalArgumentException("Email already exists");
    }
  }

  private UserDto toDto(User user) {
    Boolean online = userStatusRepository.findByUserId(user.getId())
        .map(UserStatus::isOnline)
        .orElse(null);

    return new UserDto(
        user.getId(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        user.getUsername(),
        user.getEmail(),
        user.getProfile(),
        online
    );
  }
}
