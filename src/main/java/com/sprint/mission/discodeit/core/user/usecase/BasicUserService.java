package com.sprint.mission.discodeit.core.user.usecase;


import com.sprint.mission.discodeit.core.storage.entity.BinaryContent;
import com.sprint.mission.discodeit.core.storage.usecase.BinaryContentService;
import com.sprint.mission.discodeit.core.storage.usecase.dto.BinaryContentCreateCommand;
import com.sprint.mission.discodeit.core.status.entity.UserStatus;
import com.sprint.mission.discodeit.core.status.usecase.user.UserStatusService;
import com.sprint.mission.discodeit.core.status.usecase.dto.UserStatusCreateCommand;
import com.sprint.mission.discodeit.core.status.usecase.dto.UserStatusDto;
import com.sprint.mission.discodeit.core.user.controller.dto.UserStatusRequest;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.exception.UserAlreadyExistsException;
import com.sprint.mission.discodeit.core.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.core.user.repository.JpaUserRepository;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserCreateCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserUpdateCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserDto;
import com.sprint.mission.discodeit.exception.ErrorCode;
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
  public UserDto create(UserCreateCommand command,
      Optional<BinaryContentCreateCommand> binaryContentDTO) {
    BinaryContent profile = null;

    if (userRepository.existsByName(command.username())) {
      throw new UserAlreadyExistsException(ErrorCode.USER_NAME_ALREADY_EXISTS, command.username());
    }

    if (userRepository.existsByEmail(command.email())) {
      throw new UserAlreadyExistsException(ErrorCode.USER_EMAIL_ALREADY_EXISTS, command.email());
    }

    if (binaryContentDTO.isPresent()) {
      profile = binaryContentService.create(binaryContentDTO.orElse(null));
    }
    String encode = passwordEncoder.encode(command.password());
    User user = User.create(command.username(), command.email(), encode, profile);
    userRepository.save(user);
    log.info("[UserService] User registered: id {}, name {}", user.getId(), user.getName());

    UserStatusCreateCommand statusCommand = new UserStatusCreateCommand(user, Instant.now());
    UserStatus userStatus = userStatusService.create(statusCommand);
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
  public UserDto update(UserUpdateCommand command,
      Optional<BinaryContentCreateCommand> binaryContentDTO) {
    User user = userRepository.findById(command.id())
        .orElseThrow(
            () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, command.id()));

    BinaryContent profile = user.getProfile();
    if (profile != null && binaryContentDTO.isPresent()) {
      binaryContentService.delete(profile.getId());
    }
    BinaryContent newProfile = binaryContentService.create(binaryContentDTO.orElse(null));

    user.update(command.newName(), command.newEmail(), command.newPassword(), newProfile);
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
        .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, userId));

    UUID id = user.getId();
    BinaryContent profile = user.getProfile();

    if (profile != null && profile.getId() != null) {
      binaryContentService.delete(profile.getId());
    }

    userStatusService.delete(id);
    userRepository.deleteById(id);

    log.info("[UserService] User deleted {}", userId);
  }

  @Override
  public UserStatusDto online(UUID userId, UserStatusRequest requestBody) {
    User user = userRepository.findById(userId).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, userId));

    UserStatus userStatus = user.getUserStatus();
    userStatus.update(requestBody.newLastActiveAt());
    return UserStatusDto.create(userStatus);
  }
}
