package com.sprint.mission.discodeit.adapter.outbound.status.user;

import com.sprint.mission.discodeit.core.status.entity.UserStatus;
import com.sprint.mission.discodeit.core.status.port.UserStatusRepositoryPort;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserStatusRepositoryPortAdapter implements UserStatusRepositoryPort {

  private final JpaUserStatusRepository jpaUserStatusRepository;

  @Override
  public UserStatus save(UserStatus userStatus) {
    return jpaUserStatusRepository.save(userStatus);
  }

  @Override
  public Optional<UserStatus> findByUserId(UUID userId) {
    return jpaUserStatusRepository.findByUser_Id(userId);
  }

  @Override
  public Optional<UserStatus> findByStatusId(UUID userStatusId) {
    return jpaUserStatusRepository.findById(userStatusId);
  }

  @Override
  public List<UserStatus> findAll() {
    return jpaUserStatusRepository.findAll();
  }

  @Override
  public void delete(UUID userStatusId) {
    jpaUserStatusRepository.deleteById(userStatusId);
  }

  @Override
  public boolean existsById(UUID userStatusId) {
    return jpaUserStatusRepository.existsById(userStatusId);
  }
}
