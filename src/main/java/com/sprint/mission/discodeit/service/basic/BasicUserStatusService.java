package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.user.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.service.user.userstatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.service.user.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.userstatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;
  private final UserStatusMapper userStatusMapper;

  @Override
  @Transactional
  public UserStatusDto create(UserStatusCreateRequest request) {
    log.debug("사용자 상태 생성 시작: {}", request);
    User user = userRepository.findById(request.userId())
        .orElseThrow(() -> new UserNotFoundException().notFoundWithId(request.userId()));

    if (userStatusRepository.existsByUserId(request.userId())) {
      throw new UserStatusNotFoundException().notFoundWithUserId(request.userId());
    }
    Instant lastActiveAt = request.lastActiveAt();
    UserStatus userStatus = new UserStatus(user, lastActiveAt);
    userStatusRepository.save(userStatus);
    log.info("사용자 상태 생성 완료: id={}", userStatus.getId());
    return userStatusMapper.toDto(userStatus);
  }

  @Override
  @Transactional(readOnly=true)
  public UserStatusDto find(UUID id) {
    UserStatus userStatus = userStatusRepository.findById(id)
        .orElseThrow(() -> new UserStatusNotFoundException().notFoundWithId(id));
    return userStatusMapper.toDto(userStatus);
  }

  @Override
  @Transactional(readOnly=true)
  public List<UserStatusDto> findAll() {
    List<UserStatus> userStatusList = userStatusRepository.findAll();
    return userStatusMapper.toDtoList(userStatusList);
  }

  @Override
  public UserStatus findByUserId(UUID userId) {
    return userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new UserStatusNotFoundException().notFoundWithUserId(userId));
  }

  @Override
  @Transactional
  public UserStatusDto update(UUID id, UserStatusUpdateRequest request) {
    log.debug("사용자 상태 수정 시작: id={}, request={}", id, request);
    Instant newLastActiveAt = request.newLastActiveAt();
    UserStatus userStatus = userStatusRepository.findById(id)
        .orElseThrow(() -> new UserStatusNotFoundException().notFoundWithId(id));
    userStatus.update(newLastActiveAt);

    userStatusRepository.save(userStatus);
    log.info("사용자 상태 수정 완료: id={}", userStatus.getId());
    return userStatusMapper.toDto(userStatus);
  }

  @Override
  @Transactional
  public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request) {
    log.debug("사용자의 사용자 상태 수정 시작: userId={}, request={}", userId, request);
    UserStatus userStatus = findByUserId(userId);
    Instant newLastActiveAt = request.newLastActiveAt();
    userStatus.update(newLastActiveAt);
    userStatusRepository.save(userStatus);
    log.info("사용자의 사용자 상태 수정 완료: id={}", userStatus.getId());

    return userStatusMapper.toDto(userStatus);
  }

  @Override
  @Transactional
  public void delete(UUID id) {
    log.debug("사용자 상태 삭제 시작: id={}", id);
    if (!userStatusRepository.existsById(id)) {
      throw new UserStatusNotFoundException().notFoundWithId(id);
    }
    userStatusRepository.deleteById(id);
    log.info("사용자 상태 삭제 완료: id={}", id);
  }
}
