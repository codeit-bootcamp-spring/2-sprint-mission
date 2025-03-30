package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.dto.user.UserRequest;
import com.sprint.mission.discodeit.application.dto.user.UserResult;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResult register(UserRequest userRequest, UUID profileId);

    UserResult getById(UUID userId);

    UserResult getByName(String name);

    List<UserResult> getAll();

    UserResult getByEmail(String email);

    List<UserResult> getAllByIds(List<UUID> userIds);

    UserResult updateName(UUID userId, String name);

    UserResult updateProfileImage(UUID userId, UUID profileId);

    void delete(UUID userId);
}
