package com.sprint.mission.discodeit.provider;

import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.updater.UserUpdater;

import java.util.List;

public interface UserUpdaterProvider {
    List<UserUpdater> getApplicableUpdaters(User user, UserUpdateRequest request);
}
