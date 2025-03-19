package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.dto.UserStatusParam;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public interface UserStatusService {
    void create(UserStatusParam statusparam);

    UserStatus find(UUID id);

    List<UserStatus> findAll();

    UserStatus findByUserId(UUID userId);

    UserStatus update(UserStatusParam statusparam);

    UserStatus updateByUserId(UUID userId);

    void delete(UUID id);
}
