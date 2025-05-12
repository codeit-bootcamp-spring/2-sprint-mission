package com.sprint.mission.discodeit.core.user.usecase;


import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.core.content.usecase.BinaryContentService;
import com.sprint.mission.discodeit.core.content.usecase.dto.BinaryContentCreateCommand;
import com.sprint.mission.discodeit.core.status.entity.UserStatus;
import com.sprint.mission.discodeit.core.status.usecase.user.UserStatusService;
import com.sprint.mission.discodeit.core.status.usecase.user.dto.UserStatusCreateCommand;
import com.sprint.mission.discodeit.core.status.usecase.user.dto.UserStatusOnlineCommand;
import com.sprint.mission.discodeit.core.status.usecase.user.dto.UserStatusDto;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.exception.UserAlreadyExistsException;
import com.sprint.mission.discodeit.core.user.exception.UserLoginFailedException;
import com.sprint.mission.discodeit.core.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.core.user.repository.JpaUserRepository;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserCreateCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserLoginCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserUpdateCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserDto;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicUserService implements UserService {

  private final JpaUserRepository userRepository;
  private final UserStatusService userStatusService;
  private final BinaryContentService binaryContentService;

  @Override
  @Transactional
  public UserDto create(UserCreateCommand command,
      Optional<BinaryContentCreateCommand> binaryContentDTO) {
    BinaryContent profile = null;
    validateUser(command.username(), command.email());

    if (binaryContentDTO.isPresent()) {
      profile = binaryContentService.create(binaryContentDTO.orElse(null));
    }

    User user = User.create(command.username(), command.email(), command.password(), profile);
    userRepository.save(user);
    log.info("[UserService] User registered: id {}, name {}", user.getId(), user.getName());

    UserStatusCreateCommand statusCommand = new UserStatusCreateCommand(user, Instant.now());
    UserStatus userStatus = userStatusService.create(statusCommand);

    user.setUserStatus(userStatus);

    return UserDto.create(user, true);
  }

  @Override
  @Transactional
  public UserDto login(UserLoginCommand command) {
    User user = userRepository.findByName(command.username()).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, command.username())
    );

    if (!command.password().equals(user.getPassword())) {
      throw new UserLoginFailedException(ErrorCode.LOGIN_FAILED, user.getId(), "Password mismatch");
    }

    user.getUserStatus().updateTime(Instant.now());

    log.info("[UserService] User login: id {}, username {}, password  {}", user.getId(),
        user.getName(),
        user.getPassword());

    return UserDto.create(user, user.getUserStatus().isOnline());
  }

  private void validateUser(String name, String email) {
    if (userRepository.existsByName(name)) {
      throw new UserAlreadyExistsException(ErrorCode.USER_NAME_ALREADY_EXISTS, name);
    }

    if (userRepository.existsByEmail(email)) {
      throw new UserAlreadyExistsException(ErrorCode.USER_EMAIL_ALREADY_EXISTS, email);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public UserDto findById(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, userId));
    return UserDto.create(user, user.getUserStatus().isOnline());
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserDto> findAll() {
    List<User> userList = userRepository.findAllWithProfileAndStatus();

    return userList.stream().map(user -> UserDto.create(
        user,
        user.getUserStatus().isOnline())
    ).toList();
  }

  @Override
  @Transactional
  public UserDto update(UserUpdateCommand command,
      Optional<BinaryContentCreateCommand> binaryContentDTO) {
    User user = userRepository.findById(command.requestUserId())
        .orElseThrow(
            () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, command.requestUserId()));

    BinaryContent profile = user.getProfile();
    if (profile != null && binaryContentDTO.isPresent()) {
      binaryContentService.delete(profile.getId());
    }
    BinaryContent newProfile = binaryContentService.create(binaryContentDTO.orElse(null));

    user.update(command.newName(), command.newEmail(), command.newPassword(),
        newProfile);
    userRepository.save(user);

    log.info("[UserService] User Updated: username {}, email {}, password {}", user.getName(),
        user.getEmail(),
        user.getPassword());
    boolean online = user.getUserStatus().isOnline();
    return UserDto.create(user, online);
  }


  @Override
  @Transactional
  public void delete(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, userId));

    Optional.ofNullable(user.getProfile().getId())
        .ifPresent(binaryContentService::delete);

    userStatusService.delete(user.getId());
    userRepository.deleteById(user.getId());

    log.info("[UserService] User deleted {}", userId);
  }

  @Override
  public boolean existsById(UUID userId) {
    return userRepository.findById(userId).isPresent();
  }

  @Override
  public UserStatusDto online(UserStatusOnlineCommand command) {
    User user = userRepository.findById(command.userId()).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, command.userId())
    );
    UserStatus userStatus = user.getUserStatus();
    userStatus.update(command.lastActiveAt());
    return UserStatusDto.create(userStatus);
  }
}
