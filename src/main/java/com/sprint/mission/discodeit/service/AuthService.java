package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.dto.request.authdto.AuthServiceLoginDto;

public interface AuthService {

    User login(AuthServiceLoginDto authServiceLoginDto);

}
