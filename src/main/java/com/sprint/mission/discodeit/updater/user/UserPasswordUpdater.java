package com.sprint.mission.discodeit.updater.user;

import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserPasswordUpdater implements UserUpdater {
    @Override
    public boolean supports(User user, UserUpdateRequest userUpdateRequest) {
        return !user.getPassword().equals(userUpdateRequest.password());
    }

    @Override
    public void update(UUID userId, UserUpdateRequest request, UserRepository userRepository) {
        userRepository.updatePassword(userId, request.password());
    }
}
