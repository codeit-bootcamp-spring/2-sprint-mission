package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.Mapper.UserStatusMapper;
import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.response.UserStatusDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;
  private final UserStatusMapper userStatusMapper;

  @Override
  public UserStatusDto create(UserStatusCreateRequest request) {
    UUID userId = request.userId();
    Instant lastActiveAt = request.lastActiveAt();
    log.info("UserStatus 생성 요청: userId={}, lastActiveAt={}", userId, lastActiveAt);

    if (!userRepository.existsById(userId)) {
      log.warn("UserStatus 생성 실패 (User 없음): userId={}", userId);
      throw new NoSuchElementException("User with id " + userId + " does not exist");
    }
    if (userStatusRepository.findByUserId(userId).isPresent()) {
      log.warn("UserStatus 생성 실패 (이미 존재): userId={}", userId);
      throw new IllegalArgumentException("UserStatus with userId " + userId + " already exists");
    }

    User user = userRepository.findById(userId).orElse(null);
    UserStatus status = userStatusRepository.save(new UserStatus(user, lastActiveAt));
    UserStatusDto dto = userStatusMapper.toDto(status);
    log.info("UserStatus 생성 완료: id={}, userId={}", status.getId(), userId);
    return dto;
  }

  @Override
  public UserStatusDto find(UUID userStatusId) {
    log.info("UserStatus 조회 요청: id={}", userStatusId);
    UserStatusDto dto = userStatusRepository.findById(userStatusId)
            .map(userStatusMapper::toDto)
            .orElseThrow(() -> {
              log.warn("UserStatus 조회 실패: id={} not found", userStatusId);
              return new NoSuchElementException("UserStatus with id " + userStatusId + " not found");
            });
    log.info("UserStatus 조회 완료: id={}", userStatusId);
    return dto;
  }

  @Override
  public List<UserStatusDto> findAll() {
    log.info("전체 UserStatus 목록 조회 요청");
    List<UserStatusDto> list = userStatusRepository.findAll().stream()
            .map(userStatusMapper::toDto)
            .toList();
    log.info("전체 UserStatus 목록 조회 완료: count={}", list.size());
    return list;
  }

  @Override
  public UserStatusDto update(UUID userStatusId, UserStatusUpdateRequest request) {
    Instant newLastActiveAt = request.newLastActiveAt();
    log.info("UserStatus 수정 요청: id={}, newLastActiveAt={}", userStatusId, newLastActiveAt);

    UserStatus status = userStatusRepository.findById(userStatusId)
            .orElseThrow(() -> {
              log.warn("UserStatus 수정 실패: id={} not found", userStatusId);
              return new NoSuchElementException("UserStatus with id " + userStatusId + " not found");
            });
    status.update(newLastActiveAt);
    UserStatusDto dto = userStatusMapper.toDto(userStatusRepository.save(status));
    log.info("UserStatus 수정 완료: id={}, lastActiveAt={}", userStatusId, newLastActiveAt);
    return dto;
  }

  @Override
  public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request) {
    Instant newLastActiveAt = request.newLastActiveAt();
    log.info("UserStatus byUserId 수정 요청: userId={}, newLastActiveAt={}", userId, newLastActiveAt);

    UserStatus status = userStatusRepository.findByUserId(userId)
            .orElseThrow(() -> {
              log.warn("UserStatus byUserId 수정 실패: userId={} not found", userId);
              return new NoSuchElementException("UserStatus with userId " + userId + " not found");
            });
    status.update(newLastActiveAt);
    UserStatusDto dto = userStatusMapper.toDto(userStatusRepository.save(status));
    log.info("UserStatus byUserId 수정 완료: id={}, userId={}", status.getId(), userId);
    return dto;
  }

  @Override
  public void delete(UUID userStatusId) {
    log.info("UserStatus 삭제 요청: id={}", userStatusId);
    if (!userStatusRepository.existsById(userStatusId)) {
      log.warn("UserStatus 삭제 실패: id={} not found", userStatusId);
      throw new NoSuchElementException("UserStatus with id " + userStatusId + " not found");
    }
    userStatusRepository.deleteById(userStatusId);
    log.info("UserStatus 삭제 완료: id={}", userStatusId);
  }
}