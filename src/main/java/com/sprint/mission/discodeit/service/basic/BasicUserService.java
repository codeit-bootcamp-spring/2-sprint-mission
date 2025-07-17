package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.security.jwt.JwtSession;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import io.micrometer.core.annotation.Timed;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  @Timed(value = "user.create.time", description = "User creation execution time", longTask = true)
  @Transactional
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

    final byte[] profileBytes = optionalProfileCreateRequest
        .map(BinaryContentCreateRequest::bytes)
        .orElse(null);

    BinaryContent nullableProfile = optionalProfileCreateRequest
        .map(profileRequest -> {
          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          BinaryContent binaryContent = new BinaryContent(fileName, (long) profileBytes.length,
              contentType);
          binaryContentRepository.save(binaryContent);

          return binaryContent;
        })
        .orElse(null);

    String password = userCreateRequest.password();
    String hashedPassword = passwordEncoder.encode(password);
    User user = new User(username, email, hashedPassword, nullableProfile);
    userRepository.save(user);

    // 트랜잭션 끝나고 비동기 업로드 작업
    if (nullableProfile != null) {
      updateUploadStatusAfterPut(nullableProfile, profileBytes);
    }

    log.info("사용자 생성 완료: id={}, username={}", user.getId(), username);
    return userMapper.toDto(user);
  }

  @Transactional(readOnly = true)
  @Override
  public UserDto find(UUID userId) {
    log.debug("사용자 조회 시작: id={}", userId);
    UserDto userDto = userRepository.findById(userId)
        .map(userMapper::toDto)
        .orElseThrow(() -> UserNotFoundException.withId(userId));
    log.info("사용자 조회 완료: id={}", userId);
    return userDto;
  }

  @Override
  public List<UserDto> findAll() {
    log.debug("모든 사용자 조회 시작");
    Set<UUID> onlineUserIds = jwtService.getActiveJwtSessions().stream()
        .map(JwtSession::getUserId)
        .collect(Collectors.toSet());

    List<UserDto> userDtos = userRepository.findAllWithProfile()
        .stream()
        .map(user -> userMapper.toDto(user, onlineUserIds.contains(user.getId())))
        .toList();
    log.info("모든 사용자 조회 완료: 총 {}명", userDtos.size());
    return userDtos;
  }

  @Timed(value = "user.create.time", description = "User creation execution time", longTask = true)
  @PreAuthorize("hasRole('ADMIN') or principal.userDto.id == #userId")
  @Transactional
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

    final byte[] profileBytes = optionalProfileCreateRequest
        .map(BinaryContentCreateRequest::bytes)
        .orElse(null);

    BinaryContent nullableProfile = optionalProfileCreateRequest
        .map(profileRequest -> {
          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          BinaryContent binaryContent = new BinaryContent(fileName, (long) profileBytes.length,
              contentType);
          binaryContentRepository.save(binaryContent);
          return binaryContent;
        })
        .orElse(null);

    String newPassword = userUpdateRequest.newPassword();
    String hashedNewPassword = Optional.ofNullable(newPassword).map(passwordEncoder::encode)
        .orElse(null);
    user.update(newUsername, newEmail, hashedNewPassword, nullableProfile);

    // 트랜잭션 끝나고 비동기 업로드 작업
    if (nullableProfile != null) {
      updateUploadStatusAfterPut(nullableProfile, profileBytes);
    }

    log.info("사용자 수정 완료: id={}", userId);
    return userMapper.toDto(user);
  }

  @PreAuthorize("hasRole('ADMIN') or principal.userDto.id == #userId")
  @Transactional
  @Override
  public void delete(UUID userId) {
    log.debug("사용자 삭제 시작: id={}", userId);

    if (!userRepository.existsById(userId)) {
      throw UserNotFoundException.withId(userId);
    }

    userRepository.deleteById(userId);
    log.info("사용자 삭제 완료: id={}", userId);
  }

  private void updateUploadStatusAfterPut(BinaryContent binaryContent, byte[] profileBytes) {
    TransactionSynchronizationManager.registerSynchronization(
        new TransactionSynchronizationAdapter() {
          public void afterCommit() {
            try {
              // 비동기 put 호출
              UUID uploadedId = binaryContentStorage.put(
                  binaryContent.getId(), profileBytes);

              // BinaryContent 조회 및 상태 업데이트
              BinaryContent updatedContent = binaryContentRepository.findById(
                      binaryContent.getId())
                  .orElseThrow(
                      () -> BinaryContentNotFoundException.withId(binaryContent.getId()));

              if (uploadedId != null) {
                updatedContent.markUploadSuccess();
              } else {
                updatedContent.markUploadFailed();
              }
              binaryContentRepository.save(updatedContent); // 상태 저장
              log.info("업로드 상태 업데이트 완료: ID = {}, 상태 = {}",
                  binaryContent.getId(), updatedContent.getUploadStatus());
            } catch (Exception e) {
              log.error("afterCommit 중 업로드 실패: ID = {}, 오류 = {}",
                  binaryContent.getId(), e.getMessage());
            }
          }
        });
  }

}
