package com.sprint.mission.discodeit.updater;

import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserProfileIdUpdater implements UserUpdater {
    @Override
    public boolean supports(User user, UserUpdateRequest userUpdateRequest) {
        return !user.getProfileId().equals(userUpdateRequest.profileId());
    }

    @Override
    public void update(User user, UserUpdateRequest request, UserRepository userRepository) {
        userRepository.updateProfileId(user.getId(), request.profileId());
    }
}
