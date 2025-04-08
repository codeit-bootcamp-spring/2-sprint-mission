package com.sprint.mission.discodeit.core.status.port;

import com.sprint.mission.discodeit.core.status.entity.UserStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStatusRepository {

  void save(UserStatus userStatus);

  Optional<UserStatus> findByUserId(UUID userId);

  Optional<UserStatus> findByStatusId(UUID userStatusId);

  List<UserStatus> findAll();

  void delete(UUID userStatusId);

  boolean existsById(UUID userStatusId);

//  void deleteByUserId(UUID userStatusId);
}
