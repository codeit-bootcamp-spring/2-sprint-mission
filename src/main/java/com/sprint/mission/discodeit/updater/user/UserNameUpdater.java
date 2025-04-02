package com.sprint.mission.discodeit.updater.user;

import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserNameUpdater implements UserUpdater {
    @Override
    public boolean supports(User user, UserUpdateRequest userUpdateRequest) {
        return !user.getUserName().equals(userUpdateRequest.userName());
    }

    @Override
    public void update(UUID userId,  UserUpdateRequest request, UserRepository userRepository) {
        userRepository.updateUserName(userId, request.userName());
    }
}
