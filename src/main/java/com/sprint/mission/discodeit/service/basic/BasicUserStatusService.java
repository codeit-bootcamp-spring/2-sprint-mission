package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserStatusResponseDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.dto.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.UserStatusUpdateRequest;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;

  @Override
  public UserStatus createUserStatus(UserStatusCreateRequest request) {
    checkUserExists(request.userId());
    checkUserStatusExistsByUserId(request.userId());

    return userStatusRepository.save(request.convertCreateRequestToUserStatus());
  }

  @Override
  public UserStatus findById(UUID userStatusId) {
    return userStatusRepository.findById(userStatusId)
        .orElseThrow(
            () -> new NoSuchElementException("해당 ID의 UserStatus를 찾을 수 없습니다: " + userStatusId));
  }

  @Override
  public List<UserStatus> findAll() {
    return userStatusRepository.findAll();
  }

  @Override
  public UserStatus findByUserId(UUID userId) {
    return userStatusRepository.findByUserId(userId)
        .orElseThrow(
            () -> new NoSuchElementException("해당 사용자 ID의 UserStatus를 찾을 수 없습니다: " + userId));
  }

  @Override
  public void updateUserStatus(UUID userStatusId, UserStatusUpdateRequest request) {
    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(
            () -> new NoSuchElementException("해당 ID의 UserStatus를 찾을 수 없습니다: " + userStatusId));
    checkUserExists(userStatus.getUserId());

    userStatus.update(request.newLastActiveAt());
    userStatusRepository.save(userStatus);
  }

  @Override
  public UserStatusResponseDto updateByUserId(UUID userId,
      UserStatusUpdateRequest request) {
    checkUserExists(userId);
    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(
            () -> new NoSuchElementException("해당 사용자 ID의 UserStatus를 찾을 수 없습니다: " + userId));

    userStatus.update(request.newLastActiveAt());
    UserStatus updatedUserStatus = userStatusRepository.save(userStatus);
    return UserStatusResponseDto.convertToResponseDto(updatedUserStatus);
  }

  @Override
  public void deleteUserStatus(UUID userStatusId) {
    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(
            () -> new NoSuchElementException("해당 ID의 UserStatus를 찾을 수 없습니다: " + userStatusId));
    checkUserExists(userStatus.getUserId());

    userStatusRepository.deleteById(userStatusId);
  }


  /*******************************
   * Validation check
   *******************************/
  private void checkUserStatusExistsByUserId(UUID userId) {
    if (findByUserId(userId) == null) {
      throw new NoSuchElementException("해당 사용자의 UserStatus를 찾을 수 없습니다: " + userId);
    }
  }

  private void checkUserExists(UUID userId) {
    if (userRepository.findById(userId).isEmpty()) {
      throw new NoSuchElementException("해당 사용자가 존재하지 않습니다. : " + userId);
    }
  }

}
