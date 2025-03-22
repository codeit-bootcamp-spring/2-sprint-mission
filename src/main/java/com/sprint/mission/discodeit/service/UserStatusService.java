package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public interface UserStatusService {

    UserStatus create(UUID userId);

    UserStatus findByUserId(UUID userId);

    UserStatus findByStatusId(UUID userStatusId);

    List<UserStatus> findAll();

    UUID update(UUID userId);

    void delete(UUID userStatusId);
}
