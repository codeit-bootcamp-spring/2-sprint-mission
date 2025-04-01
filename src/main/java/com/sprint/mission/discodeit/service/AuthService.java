package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.exception.InvalidRequestException;

public interface AuthService { ;

    String login(UserDto.Login loginDto) throws InvalidRequestException;
}
