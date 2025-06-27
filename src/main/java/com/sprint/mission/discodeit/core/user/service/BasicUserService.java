package com.sprint.mission.discodeit.core.user.service;


import com.sprint.mission.discodeit.core.storage.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.core.storage.entity.BinaryContent;
import com.sprint.mission.discodeit.core.storage.service.BinaryContentService;
import com.sprint.mission.discodeit.core.user.dto.UserDto;
import com.sprint.mission.discodeit.core.user.dto.UserStatusDto;
import com.sprint.mission.discodeit.core.user.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.core.user.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.core.user.dto.request.UserStatusRequest;
import com.sprint.mission.discodeit.core.user.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.entity.UserStatus;
import com.sprint.mission.discodeit.core.user.repository.JpaUserRepository;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.core.user.UserException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicUserService implements UserService {

  private final PasswordEncoder passwordEncoder;
  private final JpaUserRepository userRepository;
  private final UserStatusService userStatusService;
  private final BinaryContentService binaryContentService;

  @Override
  @Transactional
  public UserDto create(UserCreateRequest request,
      Optional<BinaryContentCreateRequest> binaryContentRequest) {
    if (userRepository.existsByNameOrEmail(request.username(), request.email())) {
      throw new UserException(ErrorCode.USER_NAME_ALREADY_EXISTS);
    }

    BinaryContent profile = null;
    if (binaryContentRequest.isPresent()) {
      profile = binaryContentService.create(binaryContentRequest.orElse(null));
    }

    String encode = passwordEncoder.encode(request.password());
    User user = User.create(request.username(), request.email(), encode, profile);
    userRepository.save(user);

    log.info("[UserService] User registered: id {}, name {}", user.getId(), user.getName());

    UserStatus userStatus = userStatusService.create(
        new UserStatusCreateRequest(user, Instant.now()));
    user.setUserStatus(userStatus);

    return UserDto.from(user);
  }


  @Override
  @Transactional(readOnly = true)
  public List<UserDto> findAll() {
    List<User> userList = userRepository.findAllWithProfileAndStatus();
    return userList.stream().map(UserDto::from).toList();
  }

  @Override
  @Transactional
  public UserDto update(UUID id, UserUpdateRequest request,
      Optional<BinaryContentCreateRequest> binaryContentRequest) {
    User user = userRepository.findById(id).orElseThrow(
        () -> new UserException(ErrorCode.USER_NOT_FOUND, id));

    BinaryContent newProfile = binaryContentService.create(binaryContentRequest.orElse(null));
    user.update(request.newUsername(), request.newEmail(), newProfile);

    if (request.newPassword() != null) {
      user.updatePassword(passwordEncoder, request.newPassword());
    }

    userRepository.save(user);

    log.info("[UserService] User Updated: username {}, email {}, password {}", user.getName(),
        user.getEmail(),
        user.getPassword());
    return UserDto.from(user);
  }

  @Override
  @Transactional
  public void delete(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, userId));

    UUID id = user.getId();
    BinaryContent profile = user.getProfile();

    if (profile != null) {
      binaryContentService.delete(profile.getId());
    }

    userStatusService.delete(id);
    userRepository.delete(user);

    log.info("[UserService] User deleted {}", userId);
  }

  @Override
  public UserStatusDto online(UUID userId, UserStatusRequest request) {
    User user = userRepository.findById(userId).orElseThrow(
        () -> new UserException(ErrorCode.USER_NOT_FOUND, userId));

    UserStatus userStatus = user.getUserStatus();
    userStatus.update(request.newLastActiveAt());
    return UserStatusDto.create(user, userStatus);
  }
}
