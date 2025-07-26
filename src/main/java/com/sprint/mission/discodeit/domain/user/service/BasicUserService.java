package com.sprint.mission.discodeit.domain.user.service;

import com.sprint.mission.discodeit.domain.storage.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.domain.storage.dto.BinaryContentDto;
import com.sprint.mission.discodeit.domain.storage.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.storage.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.domain.storage.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.domain.storage.repository.BinaryContentStorage;
import com.sprint.mission.discodeit.domain.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.domain.user.UserNotFoundException;
import com.sprint.mission.discodeit.domain.user.dto.UserCreateRequest;
import com.sprint.mission.discodeit.domain.user.dto.UserDto;
import com.sprint.mission.discodeit.domain.user.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.security.jwt.JwtSession;
import com.sprint.mission.discodeit.sse.SseEmitterService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final SseEmitterService sseEmitterService;

  @Transactional
  @CacheEvict(value = "users", key = "'all'")
  @Override
  public UserDto create(UserCreateRequest userCreateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    log.debug("사용자 생성 시작: {}", userCreateRequest);

    String username = userCreateRequest.username();
    String email = userCreateRequest.email();

    if (userRepository.existsByEmail(email)) {
      throw UserAlreadyExistsException.withEmail(email);
    }
    if (userRepository.existsByUsername(username)) {
      throw UserAlreadyExistsException.withUsername(username);
    }
    String password = userCreateRequest.password();

    String hashedPassword = passwordEncoder.encode(password);
    User user = new User(username, email, hashedPassword, null);

    BinaryContent nullableProfile = optionalProfileCreateRequest
        .map(profileRequest -> {
          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();
          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType);
          binaryContentRepository.save(binaryContent);
          TransactionSynchronizationManager.registerSynchronization(
              new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                  binaryContentStorage.putAsync(binaryContent.getId(), bytes)
                      .thenAccept(result -> {
                        log.debug("프로필 이미지 업로드 성공: {}", binaryContent.getId());
                        binaryContentRepository.updateUploadStatus(binaryContent.getId(),
                            BinaryContentUploadStatus.SUCCESS);
                        sseEmitterService.broadcast(
                            "binaryContents.status",
                            BinaryContentDto.from(binaryContent)
                        );
                      })
                      .exceptionally(throwable -> {
                        log.error("프로필 이미지 업로드 실패: {}", throwable.getMessage());
                        binaryContentRepository.updateUploadStatus(binaryContent.getId(),
                            BinaryContentUploadStatus.FAILED);
                        sseEmitterService.broadcast(
                            "binaryContents.status",
                            BinaryContentDto.from(binaryContent)
                        );
                        return null;
                      })
                  ;
                }
              });

          return binaryContent;
        })
        .orElse(null);
    user.update(null, null, null, nullableProfile);
    log.info("사용자 생성 완료: id={}, username={}", user.getId(), username);
    userRepository.save(user);

    sseEmitterService.broadcast("users.refresh", Map.of("userId", user.getId()));

    return UserDto.from(user);
  }

  @Transactional(readOnly = true)
  @Override
  public UserDto find(UUID userId) {
    log.debug("사용자 조회 시작: id={}", userId);
    UserDto userDto = userRepository.findById(userId)
        .map(UserDto::from)
        .orElseThrow(() -> UserNotFoundException.withId(userId));
    log.info("사용자 조회 완료: id={}", userId);
    return userDto;
  }

  @Cacheable(value = "users", key = "'all'", unless = "#result.isEmpty()")
  @Override
  public List<UserDto> findAll() {
    log.debug("모든 사용자 조회 시작");
    Set<UUID> onlineUserIds = jwtService.getActiveJwtSessions().stream()
        .map(JwtSession::getUserId)
        .collect(Collectors.toSet());

    List<UserDto> userDtos = userRepository.findAllWithProfile()
        .stream()
        .map(user -> UserDto.of(user, onlineUserIds.contains(user.getId())))
        .toList();
    log.info("모든 사용자 조회 완료: 총 {}명", userDtos.size());
    return userDtos;
  }

  @PreAuthorize("hasRole('ADMIN') or principal.userDto.id == #userId")
  @Transactional
  @CacheEvict(value = "users", key = "'all'")
  @Override
  public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    log.debug("사용자 수정 시작: id={}, request={}", userId, userUpdateRequest);

    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          UserNotFoundException exception = UserNotFoundException.withId(userId);
          return exception;
        });

    String newUsername = userUpdateRequest.newUsername();
    String newEmail = userUpdateRequest.newEmail();

    if (userRepository.existsByEmail(newEmail)) {
      throw UserAlreadyExistsException.withEmail(newEmail);
    }

    if (userRepository.existsByUsername(newUsername)) {
      throw UserAlreadyExistsException.withUsername(newUsername);
    }

    BinaryContent nullableProfile = optionalProfileCreateRequest
        .map(profileRequest -> {
          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();
          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType);
          binaryContentRepository.save(binaryContent);
          TransactionSynchronizationManager.registerSynchronization(
              new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                  binaryContentStorage.putAsync(binaryContent.getId(), bytes)
                      .thenAccept(result -> {
                        log.debug("프로필 이미지 업로드 성공: {}", binaryContent.getId());
                        binaryContentRepository.updateUploadStatus(binaryContent.getId(),
                            BinaryContentUploadStatus.SUCCESS);
                        sseEmitterService.broadcast(
                            "binaryContents.status",
                            BinaryContentDto.from(binaryContent)
                        );
                      })
                      .exceptionally(throwable -> {
                        log.error("프로필 이미지 업로드 실패: {}", throwable.getMessage());
                        binaryContentRepository.updateUploadStatus(binaryContent.getId(),
                            BinaryContentUploadStatus.FAILED);
                        sseEmitterService.broadcast(
                            "binaryContents.status",
                            BinaryContentDto.from(binaryContent)
                        );
                        return null;
                      })
                  ;
                }
              });

          return binaryContent;
        })
        .orElse(null);

    String newPassword = userUpdateRequest.newPassword();
    String hashedNewPassword = Optional.ofNullable(newPassword).map(passwordEncoder::encode)
        .orElse(null);
    user.update(newUsername, newEmail, hashedNewPassword, nullableProfile);

    sseEmitterService.broadcast("users.refresh", Map.of("userId", user.getId()));

    log.info("사용자 수정 완료: id={}", userId);
    return UserDto.from(user);
  }

  @PreAuthorize("hasRole('ADMIN') or principal.userDto.id == #userId")
  @Transactional
  @CacheEvict(value = "users", key = "'all'")
  @Override
  public void delete(UUID userId) {
    log.debug("사용자 삭제 시작: id={}", userId);

    if (!userRepository.existsById(userId)) {
      throw UserNotFoundException.withId(userId);
    }

    userRepository.deleteById(userId);

    sseEmitterService.broadcast("users.refresh", Map.of("userId", userId));

    log.info("사용자 삭제 완료: id={}", userId);
  }
}
