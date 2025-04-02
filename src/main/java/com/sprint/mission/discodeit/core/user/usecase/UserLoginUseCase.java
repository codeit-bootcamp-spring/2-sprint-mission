package com.sprint.mission.discodeit.core.user.usecase;

import org.springframework.stereotype.Service;


@Service
public interface UserLoginUseCase {

  LoginUserResult login(LoginUserCommand userLoginDTO);
}
