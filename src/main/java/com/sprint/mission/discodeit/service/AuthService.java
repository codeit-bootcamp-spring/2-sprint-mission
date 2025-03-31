package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.dto.authdto.AuthServiceLoginRep;
import com.sprint.mission.discodeit.service.dto.authdto.AuthServiceLoginReq;

public interface AuthService {

    AuthServiceLoginRep login(AuthServiceLoginReq authServiceLoginReq);

}
