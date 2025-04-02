package com.sprint.mission.discodeit.core.user.usecase;

import com.sprint.mission.discodeit.core.user.usecase.dto.LoginUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.LoginUserResult;
import org.springframework.stereotype.Service;


@Service
public interface UserLoginUseCase {

  LoginUserResult login(LoginUserCommand userLoginDTO);
}
