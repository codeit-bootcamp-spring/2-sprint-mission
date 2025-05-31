package com.sprint.mission.discodeit.core.user.usecase;

import com.sprint.mission.discodeit.core.user.usecase.dto.UserLoginCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserDto;


public interface UserLoginUseCase {

  UserDto login(UserLoginCommand command);
}
