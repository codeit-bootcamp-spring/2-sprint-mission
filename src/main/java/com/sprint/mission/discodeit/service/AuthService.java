package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.auth.AuthLoginDto;
import com.sprint.mission.discodeit.entity.User;

public interface AuthService {
    User login(AuthLoginDto authLoginDto);
}
