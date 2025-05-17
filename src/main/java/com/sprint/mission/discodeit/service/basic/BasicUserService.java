package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.*;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.entity.common.BinaryContent;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.user.DuplicateEmailException;
import com.sprint.mission.discodeit.exception.user.DuplicateUsernameException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStatusService userStatusService;
  private final BinaryContentService binaryContentService;
  private final BinaryContentRepository binaryContentRepository;
  private final UserMapper userMapper;

  @Override
  public UserResponse create(UserCreateRequest userCreateRequest,
      BinaryContentCreateRequest profileCreateRequest) {
    log.info("유저 생성 시도: username={}", userCreateRequest.username());

    userRepository.findByUsername(userCreateRequest.username())
        .ifPresent(u -> {
          log.warn("유저 생성 실패 - 중복된 유저 이름: username={}", userCreateRequest.username());
          throw new DuplicateUsernameException();
        });
    userRepository.findByEmail(userCreateRequest.email())
        .ifPresent(u -> {
          log.warn("유저 생성 실패 - 중복된 이메일: email={}", userCreateRequest.email());
          throw new DuplicateEmailException();
        });

    BinaryContent profile = null;
    if (profileCreateRequest != null) {
      log.debug("프로필 이미지 생성 처리");
      profile = binaryContentService.create(profileCreateRequest);
    }

    User user = new User(userCreateRequest.username(),
        userCreateRequest.email(), userCreateRequest.password(), profile);

    userRepository.save(user);
    log.info("유저 생성 성공: userId={}", user.getId());

    userStatusService.create(new UserStatusCreateRequest(user.getId()));
    log.debug("유저 상태 생성 완료: userId={}", user.getId());

    return userMapper.toResponse(user);
  }

  @Override
  public UserResponse find(UUID userId) {
    log.debug("유저 조회 시도: userId={}", userId);
    User user = getUserBy(userId);
    log.debug("유저 조회 성공: userId={}", userId);
    return userMapper.toResponse(user);
  }

  @Override
  public List<UserResponse> findAll() {
    log.debug("모든 유저 조회 시도");
    List<UserResponse> users = userRepository.findAll().stream()
        .map(userMapper::toResponse).toList();
    log.debug("모든 유저 조회 성공: {}명 발견", users.size());
    return users;
  }

  @Override
  public UserResponse update(UUID userId, UserUpdateRequest userUpdateRequest,
      BinaryContentCreateRequest profileCreateRequest) {
    log.info("유저 수정 시도: userId={}", userId);
    User user = getUserBy(userId);

    if (userUpdateRequest.newUsername() != null) {
      log.debug("새 유저 이름 중복 검증: newUsername={}", userUpdateRequest.newUsername());
      validateUsernameDuplicate(userUpdateRequest.newUsername());
    }

    if (userUpdateRequest.newEmail() != null) {
      log.debug("새 이메일 중복 검증: newEmail={}", userUpdateRequest.newEmail());
      validateEmailDuplicate(userUpdateRequest.newEmail());
    }

    BinaryContent newProfile = null;
    if (profileCreateRequest != null) {
      log.debug("새 프로필 이미지 처리 시도: userId={}", userId);
      if (user.getProfile() != null) {
        log.debug("기존 프로필 이미지 삭제: userId={}", userId);
        binaryContentService.delete(user.getProfile());
      }
      newProfile = binaryContentService.create(profileCreateRequest);
      log.debug("새 프로필 이미지 생성 완료: userId={}", userId);
    }

    user.update(userUpdateRequest.newUsername(), userUpdateRequest.newEmail(),
        userUpdateRequest.newPassword(), newProfile);
    userRepository.save(user);
    log.info("유저 수정 성공: userId={}", userId);

    return userMapper.toResponse(user);
  }

  @Override
  public void delete(UUID userId) {
    log.info("유저 삭제 시도: userId={}", userId);
    try {
      userRepository.deleteById(userId);
      log.info("유저 삭제 성공: userId={}", userId);
    } catch (Exception e) {
      log.error("유저 삭제 실패: userId={}", userId, e);
      throw e;
    }
  }

  private User getUserBy(UUID userId) {
    log.debug("ID로 유저 조회: userId={}", userId);
    return userRepository.findById(userId)
        .orElseThrow(() -> {
          log.warn("ID로 유저 조회 실패 - 유저를 찾을 수 없음: userId={}", userId);
          return new UserNotFoundException();
        });
  }

  private void validateEmailDuplicate(String email) {
    log.debug("이메일 중복 검증: email={}", email);
    if (userRepository.existsByEmail(email)) {
      log.warn("이메일 중복 발생: email={}", email);
      throw new DuplicateEmailException();
    }
  }

  private void validateUsernameDuplicate(String username) {
    log.debug("유저 이름 중복 검증: username={}", username);
    if (userRepository.existsByUsername(username)) {
      log.warn("유저 이름 중복 발생: username={}", username);
      throw new DuplicateUsernameException();
    }
  }

  private boolean isOnline(UUID userId) {
    log.debug("유저 온라인 상태 확인: userId={}", userId);
    boolean isOnline = userStatusService.findByUserId(userId).isOnline();
    log.debug("유저 온라인 상태 결과: userId={}, isOnline={}", userId, isOnline);
    return isOnline;
  }
}