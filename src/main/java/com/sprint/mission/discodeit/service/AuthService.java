package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.AuthDto;
import com.sprint.mission.discodeit.dto.UserFindDto;

public interface AuthService {
    UserFindDto login(AuthDto authDto);
}
