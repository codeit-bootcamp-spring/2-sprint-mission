package com.sprint.mission.discodeit.updater.user;

import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Component;

// 2025 03 21 현재 updater를 통해서 userProfileId를 update 할 수 있도록 해놨음 (message에는 attachmentIds updater 가 없는 것과는 다르게. 다음 미션의 컨트롤러 작성에서 어떻게 할지 결정해야 할듯!)
@Component
public class UserProfileIdUpdater implements UserUpdater {
    @Override
    public boolean supports(User user, UserUpdateRequest userUpdateRequest) {
        return !user.getProfileId().equals(userUpdateRequest.profileId());
    }

    @Override
    public void update(UserUpdateRequest request, UserRepository userRepository) {
        userRepository.updateProfileId(request.userId(), request.profileId());
    }
}
