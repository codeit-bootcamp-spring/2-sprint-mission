package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.dto.AuthServiceLoginDto;

public interface AuthService {

    User login(AuthServiceLoginDto authServiceLoginDto);

}
