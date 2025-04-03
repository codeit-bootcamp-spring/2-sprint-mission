package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.dto.authdto.AuthServiceLoginRequest;

public interface AuthService {

    User login(AuthServiceLoginRequest authServiceLoginRequest);

}
