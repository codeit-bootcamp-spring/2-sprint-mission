package com.sprint.mission.discodeit.core.status.usecase.user;

import org.springframework.stereotype.Service;


@Service
public interface UserStatusService extends CreateUserStatusUseCase, FindUserStatusUseCase,
    UpdateUserStatusUseCase,
    DeleteUserStatusUseCase, OnlineUserStatusUseCase {

}
