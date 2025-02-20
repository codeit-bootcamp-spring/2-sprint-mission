package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

public interface UserService {
    User register();

    User updateName(User user);

    User randomRegister();
}
