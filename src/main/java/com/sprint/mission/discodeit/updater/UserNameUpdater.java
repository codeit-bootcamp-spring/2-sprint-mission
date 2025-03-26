package com.sprint.mission.discodeit.updater;

import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserNameUpdater implements UserUpdater {
    @Override
    public boolean supports(User user, UserUpdateRequest userUpdateRequest) {
        return !user.getUserName().equals(userUpdateRequest.userName());
    }

    @Override
    public void update(User user, UserUpdateRequest request, UserRepository userRepository) {
        userRepository.updateUserName(user.getId(), request.userName());
    }
}
