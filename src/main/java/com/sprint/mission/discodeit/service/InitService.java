package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserDto;

public interface InitService {
    UserDto createAdminIfNotExists();
}
