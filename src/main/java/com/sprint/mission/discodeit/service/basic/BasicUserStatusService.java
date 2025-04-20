package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.domain.UserStatus;
import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;
  private final UserStatusMapper userStatusMapper;

  @Transactional
  @Override
  public UserStatusDto createUserStatus(UserStatusCreateRequest request) {
    UUID userId = request.userId();
    Instant lastActiveAt = request.lastActiveAt();

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException(
            "해당 ID의 사용자를 찾을 수 없습니다: " + userId));

    checkUserStatusExistsByUserId(userId);

    UserStatus userStatus = UserStatus.create(user, lastActiveAt);
    UserStatus createdUserStatus = userStatusRepository.save(userStatus);

    return userStatusMapper.toDto(createdUserStatus);
  }

  @Transactional(readOnly = true)
  @Override
  public UserStatusDto findById(UUID userStatusId) {
    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(
            () -> new NoSuchElementException("해당 ID의 UserStatus를 찾을 수 없습니다: " + userStatusId));
    return userStatusMapper.toDto(userStatus);
  }

  @Transactional(readOnly = true)
  @Override
  public List<UserStatusDto> findAll() {
    List<UserStatus> userStatuses = userStatusRepository.findAll();
    List<UserStatusDto> userStatusDtos = new ArrayList<>();
    userStatuses.forEach(userStatus -> userStatusDtos.add(userStatusMapper.toDto(userStatus)));
    return userStatusDtos;
  }

  @Transactional(readOnly = true)
  @Override
  public UserStatusDto findByUserId(UUID userId) {
    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(
            () -> new NoSuchElementException("해당 사용자 ID의 UserStatus를 찾을 수 없습니다: " + userId));
    return userStatusMapper.toDto(userStatus);
  }

  @Transactional
  @Override
  public UserStatusDto updateUserStatus(UUID userStatusId, UserStatusUpdateRequest request) {
    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(
            () -> new NoSuchElementException("해당 ID의 UserStatus를 찾을 수 없습니다: " + userStatusId));
    checkUserExists(userStatus.getUser().getId());

    userStatus.update(request.newLastActiveAt());
    return userStatusMapper.toDto(userStatus);
  }

  @Transactional
  @Override
  public UserStatusDto updateByUserId(UUID userId,
      UserStatusUpdateRequest request) {
    checkUserExists(userId);
    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(
            () -> new NoSuchElementException("해당 사용자 ID의 UserStatus를 찾을 수 없습니다: " + userId));

    userStatus.update(request.newLastActiveAt());
    return userStatusMapper.toDto(userStatus);
  }

  @Transactional
  @Override
  public void deleteUserStatus(UUID userStatusId) {
    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(
            () -> new NoSuchElementException("해당 ID의 UserStatus를 찾을 수 없습니다: " + userStatusId));
    checkUserExists(userStatus.getUser().getId());

    userStatusRepository.deleteById(userStatusId);
  }


  /*******************************
   * Validation check
   *******************************/
  private void checkUserStatusExistsByUserId(UUID userId) {
    if (userStatusRepository.findByUserId(userId).isEmpty()) {
      throw new NoSuchElementException("해당 사용자의 UserStatus를 찾을 수 없습니다: " + userId);
    }
  }

  private void checkUserExists(UUID userId) {
    if (userRepository.findById(userId).isEmpty()) {
      throw new NoSuchElementException("해당 사용자가 존재하지 않습니다. : " + userId);
    }
  }

}
