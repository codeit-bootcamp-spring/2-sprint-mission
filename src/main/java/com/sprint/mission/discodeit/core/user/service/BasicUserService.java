package com.sprint.mission.discodeit.core.user.service;


import com.sprint.mission.discodeit.core.auth.dto.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.core.notification.dto.NotificationEvent;
import com.sprint.mission.discodeit.core.notification.entity.NotificationTitle;
import com.sprint.mission.discodeit.core.notification.entity.NotificationType;
import com.sprint.mission.discodeit.core.storage.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.core.storage.entity.BinaryContent;
import com.sprint.mission.discodeit.core.storage.service.BinaryContentService;
import com.sprint.mission.discodeit.core.user.UserException;
import com.sprint.mission.discodeit.core.user.dto.UserDto;
import com.sprint.mission.discodeit.core.user.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.core.user.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.repository.JpaUserRepository;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.security.jwt.JwtSessionRepository;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicUserService implements UserService {

  private final PasswordEncoder passwordEncoder;
  private final JpaUserRepository userRepository;
  private final BinaryContentService binaryContentService;
  private final JwtSessionRepository jwtSessionRepository;
  private final ApplicationEventPublisher eventPublisher; // 이벤트 발행을 위한 객체

  @Override
  @Transactional
  @CacheEvict(cacheNames = "users", allEntries = true)
  public UserDto create(UserCreateRequest request,
      Optional<BinaryContentCreateRequest> binaryContentRequest) throws IOException {
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

    return UserDto.from(user);
  }

  @Override
  @Transactional
  @CacheEvict(cacheNames = "users", allEntries = true)
  public UserDto update(UUID id, UserUpdateRequest request,
      Optional<BinaryContentCreateRequest> binaryContentRequest) throws IOException {
    User user = userRepository.findByUserId(id);
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
  @CacheEvict(cacheNames = "users", allEntries = true)
  public UserDto updateRole(UserRoleUpdateRequest request) {
    UUID id = request.userId();
    User user = userRepository.findByUserId(id);
    user.updateRole(request.newRole());
    userRepository.save(user);
    jwtSessionRepository.findByUserId(id).ifPresent(jwtSessionRepository::delete);

    eventPublisher.publishEvent(new NotificationEvent(id,
        NotificationTitle.UPDATE.getTitle(),
        "권한이 변경되었습니다",
        NotificationType.ROLE_CHANGED,
        id));

    return UserDto.from(user);
  }

  @Override
  @Transactional
  @CacheEvict(cacheNames = "users", allEntries = true)
  public void delete(UUID id) {
    User user = userRepository.findByUserId(id);
    BinaryContent profile = user.getProfile();

    if (profile != null) {
      binaryContentService.delete(profile.getId());
    }
    userRepository.delete(user);

    log.info("[UserService] User deleted {}", id);
  }
}
