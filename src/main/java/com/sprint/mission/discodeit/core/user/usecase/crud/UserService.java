package com.sprint.mission.discodeit.core.user.usecase.crud;

import com.sprint.mission.discodeit.core.user.usecase.UserLoginUseCase;

public interface UserService extends CreateUserUseCase, FindUserUseCase, UpdateUserUseCase,
    UserLoginUseCase, DeleteUserUseCase {

}
