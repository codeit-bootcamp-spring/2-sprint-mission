package com.sprint.mission.discodeit.provider;

import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.updater.UserUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DefaultUserUpdaterProvider implements UserUpdaterProvider {
    private final List<UserUpdater> userUpdaters;

    // support 되는 updater 들만 반환
    @Override
    public List<UserUpdater> getApplicableUpdaters(User user, UserUpdateRequest request) {
        return userUpdaters.stream()
                .filter(updater -> updater.supports(user, request))
                .toList();
    }
}
