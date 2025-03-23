package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreate;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdate;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStatusRepository {

    Map<UUID, UserStatus> getUserStatusData();

    UserStatus save(UserStatus userStatus);

    UserStatus update(UserStatusUpdate dto);

    List<UserStatus> findAll();

    UserStatus findById(UUID userStatusId);

    UserStatus findByUserId(UUID userId);

//    UserStatus userStatus = userStatusData.values().stream()
//            .filter(status -> status.getUserId().equals(userId))
//            .findFirst()
//            .orElseThrow(() -> new NoSuchElementException("UserStatus with userid " + userId + " not found"));


    Instant getLastOnlineTime(UUID userId);

    void delete(UUID userStatusId);
}
