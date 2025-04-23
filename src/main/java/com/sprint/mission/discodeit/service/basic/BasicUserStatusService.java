package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.user.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.service.user.userstatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.service.user.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;
  private final UserStatusMapper userStatusMapper;

  @Override
  @Transactional
  public UserStatusDto create(UserStatusCreateRequest request) {
    User user = userRepository.findById(request.userId())
        .orElseThrow(() -> new NoSuchElementException(request.userId() + " 에 해당하는 User를 찾을 수 없음"));

    if (userStatusRepository.existsByUserId(request.userId())) {
      throw new IllegalArgumentException(request.userId() + " 에 해당하는 UserStatus를 이미 존재함");
    }
    Instant lastActiveAt = request.lastActiveAt();
    UserStatus userStatus = new UserStatus(user, lastActiveAt);
    userStatusRepository.save(userStatus);
    return userStatusMapper.toDto(userStatus);
  }

  @Override
  @Transactional(readOnly=true)
  public UserStatusDto find(UUID id) {
    UserStatus userStatus = userStatusRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException(id + " 에 해당하는 UserStatus를 찾을 수 없음"));
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
        .orElseThrow(() -> new NoSuchElementException(userId + " 에 해당하는 UserStatus를 찾을 수 없음"));
  }

  @Override
  @Transactional
  public UserStatus update(UUID id, UserStatusUpdateRequest request) {
    Instant newLastActiveAt = request.newLastActiveAt();
    UserStatus userStatus = userStatusRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException(id + " 에 해당하는 UserStatus를 찾을 수 없음"));
    userStatus.update(newLastActiveAt);

    return userStatusRepository.save(userStatus);
  }

  @Override
  @Transactional
  public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request) {
    UserStatus userStatus = findByUserId(userId);
    Instant newLastActiveAt = request.newLastActiveAt();
    userStatus.update(newLastActiveAt);

    return userStatusMapper.toDto(userStatus);
  }

  @Override
  @Transactional
  public void delete(UUID id) {
    if (!userStatusRepository.existsById(id)) {
      throw new NoSuchElementException(id + " 에 해당하는 UserStatus를 찾을 수 없음");
    }
    userStatusRepository.deleteById(id);
  }
}
