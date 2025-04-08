package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.EmailAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.NameAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final BinaryContentRepository binaryContentRepository;

  @Override
  public User create(UserCreateRequest request,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    if (userRepository.findAll().stream()
        .anyMatch(user -> user.getUsername().equals(request.username()))) {
      throw new NameAlreadyExistsException("같은 이름을 가진 사람이 있습니다.");
    }
    if (userRepository.findAll().stream()
        .anyMatch(user -> user.getEmail().equals(request.email()))) {
      throw new EmailAlreadyExistsException("같은 메일을 가진 사람이 있습니다.");
    }

    UUID nullableProfileId = optionalProfileCreateRequest
        .map(profileRequest -> {
          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();
          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType, bytes);
          return binaryContentRepository.save(binaryContent).getId();
        })
        .orElse(null);

    User user = new User(request.username(), request.email(), request.password(),
        nullableProfileId);
    userRepository.save(user);

    UserStatus userStatus = new UserStatus(user.getId(), Instant.now());
    userStatusRepository.save(userStatus);

    return user;
  }

  @Override
  public UserResponseDto find(UUID userId) {
    UserStatus userStatus = userStatusRepository.findByUserId(userId);
    User user = userRepository.findById(userId);

    return new UserResponseDto(user.getId(), user.getCreatedAt(), user.getUpdatedAt(),
        user.getUsername(),
        user.getEmail(), user.getProfileId(), userStatus.isOnline());
  }

  @Override
  public List<UserResponseDto> findAll() {
    return userRepository.findAll()
        .stream()
        .map(
            user -> {
              UserStatus userStatus = userStatusRepository.findByUserId(user.getId());
              return new UserResponseDto(user.getId(), user.getCreatedAt(), user.getUpdatedAt(),
                  user.getUsername(),
                  user.getEmail(), user.getProfileId(), userStatus.isOnline());
            }).toList();
  }

  @Override
  public User update(UserUpdateRequestDto dto,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    User user = Optional.ofNullable(userRepository.findById(dto.getUserID()))
        .orElseThrow(
            () -> new UserNotFoundException("User with id " + dto.getUserID() + " not found"));

    if (userRepository.findAll().stream()
        .anyMatch(user1 -> user1.getUsername().equals(dto.getNewUserName()))) {
      throw new NameAlreadyExistsException("같은 이름을 가진 사람이 있습니다.");
    }
    if (userRepository.findAll().stream()
        .anyMatch(user1 -> user1.getEmail().equals(dto.getNewEmail()))) {
      throw new EmailAlreadyExistsException("같은 메일을 가진 사람이 있습니다.");
    }

    UUID nullableProfileId = optionalProfileCreateRequest
        .map(profileRequest -> {
          Optional.ofNullable(user.getProfileId())
              .ifPresent(binaryContentRepository::delete);

          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();
          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType, bytes);
          return binaryContentRepository.save(binaryContent).getId();
        })
        .orElse(null);

    return userRepository.update(user, dto.getNewUserName(), dto.getNewEmail(),
        dto.getNewPassword(), nullableProfileId);
  }

  @Override
  public void delete(UUID userId) {
    User user = userRepository.findById(userId);

    userRepository.delete(userId);
    userStatusRepository.delete(userStatusRepository.findByUserId(userId).getId());

    if (user.getProfileId() != null) {
      BinaryContent binaryContent = binaryContentRepository.findById(user.getProfileId());
      binaryContentRepository.delete(binaryContent.getId());
    }
  }
}
