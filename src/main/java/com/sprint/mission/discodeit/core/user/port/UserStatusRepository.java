package com.sprint.mission.discodeit.core.user.port;

import com.sprint.mission.discodeit.core.user.entity.UserStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserStatusRepository {
    UserStatus save(UserStatus userStatus);

    UserStatus findByUserId(UUID userId);

    UserStatus findByStatusId(UUID userStatusId);

    List<UserStatus> findAll();

    UserStatus update(UserStatus userStatus);

    void deleteById(UUID userStatusId);

    void deleteByUserId(UUID userStatusId);
}
