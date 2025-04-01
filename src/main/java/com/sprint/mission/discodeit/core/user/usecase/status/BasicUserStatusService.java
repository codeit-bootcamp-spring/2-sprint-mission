package com.sprint.mission.discodeit.core.user.usecase.status;

import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.DuplicateUserStatusException;
import com.sprint.mission.discodeit.logging.CustomLogging;
import com.sprint.mission.discodeit.core.user.port.UserRepository;
import com.sprint.mission.discodeit.core.user.port.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;

  @Override
  @CustomLogging
  public UserStatus create(UUID userId) {

    User user = userRepository.findById(userId);
    UserStatus userStatus = userStatusRepository.findByUserId(userId);

    if (userStatus == null) {
      userStatus = UserStatus.create(user.getId());
    } else {
      throw new DuplicateUserStatusException("중복된 유저 상태가 있습니다.");
    }
    UserStatus save = userStatusRepository.save(userStatus);
    return save;
  }


  @Override
  public UserStatus findByUserId(UUID userId) {
    UserStatus userStatus = userStatusRepository.findByUserId(userId);
    return userStatus;
  }

  @Override
  public UserStatus findByStatusId(UUID userStatusId) {
    UserStatus userStatus = userStatusRepository.findByStatusId(userStatusId);
    return userStatus;
  }


  @Override
  public List<UserStatus> findAll() {
    List<UserStatus> all = userStatusRepository.findAll();
    return all;
  }

  @Override
  @CustomLogging
  public UUID update(UUID userId) {
    UserStatus userStatus = userStatusRepository.findByUserId(userId);

    UserStatus update = userStatusRepository.update(userStatus);
    return update.getUserStatusId();
  }

  @Override
  @CustomLogging
  public void deleteById(UUID userStatusId) {
    userStatusRepository.deleteByUserId(userStatusId);
  }

  @Override
  @CustomLogging
  public void deleteByUserId(UUID userId) {
    userStatusRepository.deleteByUserId(userId);
  }


}
