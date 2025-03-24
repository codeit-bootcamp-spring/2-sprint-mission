package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

public interface UserStatusRepository {
    void save(UserStatus userStatus);
    User findById(UUID userId);
    List<ReadStatus> findAllByUserId(UUID userId);
    void delete(UUID userId);
}
