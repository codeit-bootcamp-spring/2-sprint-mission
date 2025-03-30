package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.dto.user.UserRequest;
import com.sprint.mission.discodeit.application.dto.user.UserResult;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResult register(UserRequest userRequest, UUID profileId);

    UserResult findById(UUID userId);

    UserResult findByName(String name);

    List<UserResult> findAll();

    UserResult findByEmail(String email);

    List<UserResult> findAllByIds(List<UUID> userIds);

    void updateName(UUID userId, String name);

    UserResult updateProfileImage(UUID userId, UUID profileId);

    void delete(UUID userId);
}
