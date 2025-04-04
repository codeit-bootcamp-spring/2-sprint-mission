package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.SaveUserStatusParamDto;
import com.sprint.mission.discodeit.dto.userStatus.UpdateUserStatusResponse;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;

  @Override
  public void save(SaveUserStatusParamDto saveUserStatusParamDto) {
    UserStatus userStatus = UserStatus.builder()
        .userId(saveUserStatusParamDto.userUUID())
        .build();
    userStatusRepository.save(userStatus);
  }

  @Override
  public UserStatus findById(UUID userStatusUUID) {
    return userStatusRepository.findById(userStatusUUID)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자 상태"));
  }

  @Override
  public List<UserStatus> findAll() {
    return userStatusRepository.findAll();
  }

  @Override
  public UpdateUserStatusResponse updateByUserId(UUID userId,
      UserStatusUpdateRequest userStatusUpdateRequest) {
    User user = userRepository.findUserById(userId)
        .orElseThrow(() -> new NoSuchElementException(
            String.format("User with userId %s not found", userId)));
    UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
        .orElseThrow(() -> new NoSuchElementException(
            String.format("UserStatus with userId %s not found", userId)));
    userStatus.updateLastLoginTime(userStatusUpdateRequest.newLastActiveAt());
    userStatusRepository.save(userStatus);
    return new UpdateUserStatusResponse(userStatus.getId(), userStatus.getUserId(),
        userStatus.getLastActiveAt(), userStatus.isLastStatus(), userStatus.getCreatedAt(),
        userStatus.getUpdatedAt());
  }


  @Override
  public void delete(UUID userStatusUUID) {
    userStatusRepository.delete(userStatusUUID);
  }
}
