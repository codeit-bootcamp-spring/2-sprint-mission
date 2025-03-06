package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

public interface UserRepository {
    User userSave(String nickname, String password);
}
