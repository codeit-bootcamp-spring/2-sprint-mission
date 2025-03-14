package com.sprint.mission.discodeit.Repository;

import com.sprint.mission.discodeit.DTO.UserStatusCreateDTO;
import com.sprint.mission.discodeit.DTO.UserStatusUpdateDTO;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserStatusRepository {
    void save(UserStatusCreateDTO userStatusCreateDTO);

    UserStatus find(UUID userId);

    List<UserStatus> findAll();

    void update(UserStatusUpdateDTO userStatusUpdateDTO);

    void delete(UUID userId);
}
