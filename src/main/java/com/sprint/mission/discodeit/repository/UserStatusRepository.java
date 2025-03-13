package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.List;
import java.util.UUID;

public interface UserStatusRepository {
    UserStatus save(UserStatus userStatus);

    List<UserStatus> findAll();

    UserStatus findById(UUID userId);

    void delete(UUID userId);
}
