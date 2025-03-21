package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.DTO.legacy.UserStatus.UserStatusCRUDDTO;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserStatusRepository {
    UserStatus save(UserStatus userStatus);

    UserStatus findByUserId(UUID userId);

    UserStatus findByStatusId(UUID userStatusId);

    List<UserStatus> findAll();

    UserStatus update(UserStatus userStatus, UserStatusCRUDDTO userStatusUpdateDTO);

    void delete(UUID id);
}
