package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.UserDto;
import org.springframework.security.core.Authentication;

public interface AuthService {

    UserDto getCurrentUser(Authentication authentication);
}
