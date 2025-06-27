package com.sprint.mission.discodeit.core.user.service;

import com.sprint.mission.discodeit.core.user.service.usecase.CreateUserUseCase;
import com.sprint.mission.discodeit.core.user.service.usecase.DeleteUserUseCase;
import com.sprint.mission.discodeit.core.user.service.usecase.FindUserUseCase;
import com.sprint.mission.discodeit.core.user.service.usecase.OnlineUserUseCase;
import com.sprint.mission.discodeit.core.user.service.usecase.UpdateUserUseCase;

public interface UserService extends CreateUserUseCase, FindUserUseCase, UpdateUserUseCase,
    DeleteUserUseCase, OnlineUserUseCase {

}
