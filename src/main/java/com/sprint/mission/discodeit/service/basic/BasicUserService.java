package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentDto;
import com.sprint.mission.discodeit.service.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.service.dto.user.UserDto;
import com.sprint.mission.discodeit.service.dto.user.UserUpdateRequest;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final BasicBinaryContentService basicBinaryContentService;

  @Override
  @Transactional
  public UserDto create(UserCreateRequest createRequest,
      BinaryContentCreateRequest binaryRequest) {
    String username = createRequest.username();
    String email = createRequest.email();

    validDuplicateUsername(username);
    validDuplicateEmail(email);

    BinaryContent profile = (binaryRequest != null)
        ? basicBinaryContentService.create(binaryRequest) : null;
    User user = new User(username, email, createRequest.password(), profile);
    new UserStatus(user, Instant.now());
    userRepository.save(user);

    return assembleUserResponse(user);
  }

  @Override
  public UserDto find(UUID userId) {
    if (!userRepository.existsById(userId)) {
      throw new NoSuchElementException(userId + " 에 해당하는 User를 찾을 수 없음");
    }
    User user = userRepository.findByIdWithProfile(userId);
    return assembleUserResponse(user);
  }

  @Override
  public List<UserDto> findAll() {
    List<User> userList = userRepository.findAllWithProfile();
    return userList.stream()
        .map(this::assembleUserResponse).toList();
  }

  @Override
  @Transactional
  public UserDto update(UUID userId, UserUpdateRequest updateRequest,
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

    BinaryContent newProfile = (binaryRequest != null)
        ? basicBinaryContentService.create(binaryRequest) : null;
    user.update(newUsername, newEmail, newPassword, newProfile);
    userRepository.save(user);

    return assembleUserResponse(user);
  }

  @Override
  @Transactional
  public void delete(UUID userId) {
    if (!userRepository.existsById(userId)) {
      throw new NoSuchElementException(userId + " 에 해당하는 User를 찾을 수 없음");
    }
    userRepository.deleteById(userId);
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  @Override
  public UserDto assembleUserResponse(User user) {
    BinaryContentDto contentResponse = BinaryContentDto.of(user.getProfile());
    UserStatus status = user.getUserStatus();

    return UserDto.of(user, contentResponse, status.isOnline());
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