package com.sprint.mission.discodeit.updater.user;

import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.UUID;

public interface UserUpdater {
    boolean supports(User user, UserUpdateRequest userUpdateRequest);
    void update(UUID userId, UserUpdateRequest request, UserRepository userRepository);
}
