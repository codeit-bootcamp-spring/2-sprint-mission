package com.sprint.mission.discodeit.updater;

import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

public interface UserUpdater {
    boolean supports(User user, UserUpdateRequest userUpdateRequest);
    void update(User user, UserUpdateRequest request, UserRepository userRepository);
}
