package com.sprint.mission.discodeit.Repository;

import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserStatusRepository {
    void save(UserStatus userStatus);

    UserStatus find(UUID userId);

    List<UserStatus> findAll();

    void update();

    void updateByUserId(UUID userId);

    void delete(UUID userId);
}
