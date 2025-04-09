package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserResponseDto;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;
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
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final BinaryContentRepository binaryContentRepository;

  @Override
  public User createUser(UserCreateRequest request,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    String username = request.username();
    String email = request.email();
    String password = request.password();

    checkUserEmailExists(request.email());
    checkUserUsernameExists(request.username());

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

    User user = new User(username, email, password, nullableProfileId);
    User createdUser = userRepository.save(user);

    UserStatus userStatus = new UserStatus(createdUser.getId(), Instant.now());
    userStatusRepository.save(userStatus);

    return createdUser;
  }

  @Override
  public UserResponseDto findById(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("해당 ID의 사용자를 찾을 수 없습니다: " + userId));
    boolean isOnline = checkUserOnlineStatus(userId);

    return UserResponseDto.convertToResponseDto(user, isOnline);
  }

  @Override
  public UserResponseDto findByUsername(String username) {
    return userRepository.findByUsername(username)
        .map(user -> {
          boolean isOnline = checkUserOnlineStatus(user.getId());
          return UserResponseDto.convertToResponseDto(user, isOnline);
        })
        .orElse(null);
  }

  @Override
  public UserResponseDto findByEmail(String email) {
    return userRepository.findByEmail(email)
        .map(user -> {
          boolean isOnline = checkUserOnlineStatus(user.getId());
          return UserResponseDto.convertToResponseDto(user, isOnline);
        })
        .orElse(null);
  }

  @Override
  public List<UserResponseDto> findAll() {
    return userRepository.findAll()
        .stream()
        .map(user -> {
          boolean isOnline = checkUserOnlineStatus(user.getId());
          return UserResponseDto.convertToResponseDto(user, isOnline);
        })
        .collect(Collectors.toList());
  }

  @Override
  public User updateUser(UUID userId, UserUpdateRequest request,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("해당 ID의 사용자를 찾을 수 없습니다: " + userId));

    String newUsername = request.newUsername();
    String newEmail = request.newEmail();
    String newPassword = request.newPassword();

    checkUserEmailExists(request.newEmail());
    checkUserUsernameExists(request.newUsername());

    UUID nullableProfileId = optionalProfileCreateRequest
        .map(profileRequest -> {
          Optional.ofNullable(user.getProfileId())
              .ifPresent(binaryContentRepository::deleteById);

          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();

          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType, bytes);
          return binaryContentRepository.save(binaryContent).getId();
        })
        .orElse(null);

    user.update(newUsername, newEmail, newPassword, nullableProfileId);
    return userRepository.save(user);
  }

  @Override
  public void deleteUser(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("해당 ID의 사용자를 찾을 수 없습니다: " + userId));

    if (user.getProfileId() != null) {
      binaryContentRepository.deleteById(user.getProfileId());
    }

    UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
        .orElseThrow(() -> new NoSuchElementException("해당 사용자의 UserStatus를 찾을 수 없습니다: " + userId));
    userStatusRepository.deleteById(userStatus.getId());

    userRepository.deleteById(userId);
  }


  /****************************
   * Validation check
   ****************************/
  private boolean checkUserOnlineStatus(UUID userId) {
    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new NoSuchElementException("해당 사용자의 UserStatus를 찾을 수 없습니다: " + userId));
    return userStatus.isOnline();
  }

  private void checkUserEmailExists(String email) {
    if (findByEmail(email) != null) {
      throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
    }
  }

  private void checkUserUsernameExists(String username) {
    if (findByUsername(username) != null) {
      throw new IllegalArgumentException("이미 사용 중인 이름입니다.");
    }
  }

}
