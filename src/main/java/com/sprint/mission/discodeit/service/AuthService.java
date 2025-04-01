package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.service.dto.authdto.AuthServiceLoginResponse;
import com.sprint.mission.discodeit.service.dto.authdto.AuthServiceLoginRequest;

public interface AuthService {

    AuthServiceLoginResponse login(AuthServiceLoginRequest authServiceLoginRequest);

}
