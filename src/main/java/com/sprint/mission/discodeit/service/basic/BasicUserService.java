package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentResponse;
import com.sprint.mission.discodeit.service.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.service.dto.user.UserResponse;
import com.sprint.mission.discodeit.service.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.service.dto.user.userstatus.UserStatusCreateRequest;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStatusService userStatusService;
  private final BasicBinaryContentService basicBinaryContentService;

  @Override
  public UserResponse create(UserCreateRequest createRequest,
      BinaryContentCreateRequest binaryRequest) {
    validDuplicateUsername(createRequest.username());
    validDuplicateEmail(createRequest.email());

    UUID binaryId = (binaryRequest != null)
        ? basicBinaryContentService.create(binaryRequest).getId() : null;
    User user = new User(createRequest.username(), createRequest.email(), createRequest.password(),
        binaryId);
    userRepository.save(user);
    UserStatusCreateRequest statusRequest = new UserStatusCreateRequest(user.getId(),
        Instant.now());
    userStatusService.create(statusRequest);

    return assembleUserResponse(user);
  }

  @Override
  public UserResponse find(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException(userId + " 에 해당하는 User를 찾을 수 없음"));
    UserStatus status = userStatusService.findByUserId(user.getId());
    BinaryContentResponse contentResponse = basicBinaryContentService.findById(user.getId());

    return UserResponse.of(user, contentResponse, status.isOnline());
  }

  @Override
  public List<UserResponse> findAll() {
    List<User> userList = userRepository.findAll();
    return userList.stream()
        .map(this::assembleUserResponse).toList();
  }

  @Override
  public UserResponse update(UUID userId, UserUpdateRequest updateRequest,
      BinaryContentCreateRequest binaryRequest) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException(
            userId + " 에 해당하는 User를 찾을 수 없음"));
    String newUsername = updateRequest.newUsername();
    String newEmail = updateRequest.newEmail();
    String newPassword = updateRequest.newPassword();

    if (!user.getUsername().equals(newUsername)) {
      validDuplicateUsername(newUsername);
    }
    if (!user.getEmail().equals(newEmail)) {
      validDuplicateEmail(newEmail);
    }

    UUID binaryContentId = null;
    if (binaryRequest != null) {
      if (user.getProfileId() != null) {
        basicBinaryContentService.delete(user.getProfileId());
      }
      binaryContentId = basicBinaryContentService.create(binaryRequest).getId();

    }
    user.update(newUsername, newEmail, newPassword, binaryContentId);
    userRepository.save(user);

    return assembleUserResponse(user);
  }

  @Override
  public void delete(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException(userId + " 에 해당하는 user를 찾을 수 없음"));
    if (user.getProfileId() != null) {
      basicBinaryContentService.delete(user.getProfileId());
    }
    UserStatus userStatus = userStatusService.findByUserId(userId);
    userStatusService.delete(userStatus.getId());

    userRepository.deleteById(userId);
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  @Override
  public UserResponse assembleUserResponse(User user) {
    BinaryContentResponse contentResponse = basicBinaryContentService.findById(user.getId());
    UserStatus status = userStatusService.findByUserId(user.getId());

    return UserResponse.of(user, contentResponse, status.isOnline());
  }

  private void validDuplicateUsername(String username) {
    if (userRepository.existsByUsername(username)) {
      throw new IllegalArgumentException(username + " 은 중복된 username");
    }
  }

  private void validDuplicateEmail(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new IllegalArgumentException(email + " 은 중복된 email");
    }
  }
}