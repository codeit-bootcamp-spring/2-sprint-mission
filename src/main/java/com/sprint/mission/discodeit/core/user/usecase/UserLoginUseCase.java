package com.sprint.mission.discodeit.core.user.usecase;

import com.sprint.mission.discodeit.core.user.usecase.dto.LoginUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserResult;


public interface UserLoginUseCase {

  UserResult login(LoginUserCommand userLoginDTO);
}
