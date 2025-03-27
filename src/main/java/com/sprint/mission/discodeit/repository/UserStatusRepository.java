package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStatusRepository {
    UserStatus save(UserStatus userStatus);

    UserStatus update(UserStatusUpdateRequestDto dto);

    List<UserStatus> findAll();

    UserStatus findById(UUID userStatusId);

    UserStatus findByUserId(UUID userId);

    void delete(UUID userStatusId);
}
