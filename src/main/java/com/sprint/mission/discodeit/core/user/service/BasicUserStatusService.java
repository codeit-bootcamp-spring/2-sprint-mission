package com.sprint.mission.discodeit.core.user.service;

import com.sprint.mission.discodeit.core.status.usecase.dto.UserStatusCreateRequest;
import com.sprint.mission.discodeit.core.status.usecase.dto.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.entity.UserStatus;
import com.sprint.mission.discodeit.core.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.core.user.repository.JpaUserStatusRepository;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.UserException;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicUserStatusService implements UserStatusService {

  private final JpaUserStatusRepository userStatusRepository;

  @Transactional
  @Override
  public UserStatus create(UserStatusCreateRequest request) {
    User user = request.user();
    Instant lastActiveAt = request.lastActiveAt();

    return userStatusRepository.findByUserId(user.getId()).orElseGet(() -> {
          UserStatus status = UserStatus.create(user, lastActiveAt);
          UserStatus saved = userStatusRepository.save(status);
          log.info("[UserStatusService] User Status created : id {}, user id {}, last Active At {}",
              saved.getId(), user.getId(), lastActiveAt);
          return saved;
        }
    );
  }

  @Override
  @Transactional
  public void delete(UUID userId) {
    UserStatus userStatus = userStatusRepository.findByUserId(userId).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.USER_STATUS_NOT_FOUND, userId)
    );
    userStatusRepository.deleteById(userStatus.getId());
    log.info("[UserStatusService] User Status deleted : id {}", userStatus.getId());
  }

}
