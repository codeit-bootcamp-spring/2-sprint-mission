package com.sprint.mission.discodeit.core.user.usecase.status;

import com.sprint.mission.discodeit.core.user.entity.UserStatus;
import com.sprint.mission.discodeit.core.user.port.UserRepositoryPort;
import com.sprint.mission.discodeit.core.user.port.UserStatusRepository;
import com.sprint.mission.discodeit.exception.user.UserNotFoundError;
import com.sprint.mission.discodeit.exception.user.UserStatusNotFoundException;
import com.sprint.mission.discodeit.logging.CustomLogging;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserRepositoryPort userRepositoryPort;
  private final UserStatusRepository userStatusRepository;

  @Override
  @CustomLogging
  public void create(UUID userId) {
    boolean existId = userRepositoryPort.existId(userId);
    if (!existId) {
      throw new UserNotFoundError("유저가 존재하지 않습니다.");
    }

    userStatusRepository.findByUserId(userId).ifPresentOrElse(userStatus -> {

        },
        () -> {
          UserStatus userStatus = UserStatus.create(userId);
          userStatusRepository.save(userStatus);
        });
  }

  @Override
  public UserStatus findByUserId(UUID userId) {
    return userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new UserStatusNotFoundException("유저 상태를 찾을 수 없습니다."));

  }

  @Override
  public UserStatus findByStatusId(UUID userStatusId) {
    return userStatusRepository.findByStatusId(userStatusId)
        .orElseThrow(() -> new UserStatusNotFoundException("유저 상태를 찾을 수 없습니다."));

  }


  @Override
  public List<UserStatus> findAll() {
    List<UserStatus> all = userStatusRepository.findAll();
    return all;
  }

//  @Override
//  @CustomLogging
//  public UUID update(UUID userId) {
//    UserStatus userStatus = userStatusRepository.findByUserId(userId);
//
//    UserStatus update = userStatusRepository.update(userStatus);
//    return update.getUserStatusId();
//  }

  @Override
  @CustomLogging
  public void deleteById(UUID userStatusId) {
    userStatusRepository.deleteById(userStatusId);
  }

//  @Override
//  @CustomLogging
//  public void deleteByUserId(UUID userId) {
//    userStatusRepository.deleteByUserId(userId);
//  }

}
