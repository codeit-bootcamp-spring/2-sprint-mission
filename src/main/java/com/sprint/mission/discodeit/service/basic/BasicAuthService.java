package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.LoginRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final UserService userService;
  private final BinaryContentService binaryContentService;
  private final UserMapper userMapper;

  @Transactional
  @Override
  public UserDto register(CreateUserRequest request, MultipartFile profile) {

    UserDto userDto = userService.createUser(request, profile);

    log.info("Registered user successfully: {}", userDto.id());
    return userDto;
  }

  @Transactional
  @Override
  public UserDto login(LoginRequest request) {
    log.info("Login user: {}", request);
    User user = userRepository.findByUsername(request.username())
        .orElseThrow(() -> new DiscodeitException(ErrorCode.USERNAME_NOT_FOUND));

    if (!BCrypt.checkpw(request.password(), user.getPassword())) {
      log.warn("Wrong password: {}", request.password());
      throw new DiscodeitException(ErrorCode.PASSWORD_NOT_CORRECT);
    }

    userStatusRepository.findByUserId(user.getId())
        .ifPresent(UserStatus::updateLastActiveAt);

    log.info("Login user successfully: {}", user.getId());
    return userMapper.toDto(user);
  }
}
