package com.sprint.mission.discodeit.adapter.inbound.user;

import com.sprint.mission.discodeit.adapter.inbound.user.request.UserCreateRequest;
import com.sprint.mission.discodeit.adapter.inbound.user.request.UserLoginRequest;
import com.sprint.mission.discodeit.adapter.inbound.user.request.UserUpdateRequest;
import com.sprint.mission.discodeit.core.user.usecase.dto.LoginUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.CreateUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UpdateUserCommand;
import java.util.UUID;

public final class UserDtoMapper {

  private UserDtoMapper() {

  }

  static CreateUserCommand toCreateUserCommand(UserCreateRequest requestBody) {
    return new CreateUserCommand(requestBody.name(), requestBody.email(), requestBody.password());
  }

  static LoginUserCommand toLoginUserCommand(UserLoginRequest requestBody) {
    return new LoginUserCommand(requestBody.name(), requestBody.password());
  }

  static UpdateUserCommand toUpdateUserCommand(UUID userId, UserUpdateRequest requestBody) {
    return new UpdateUserCommand(userId, requestBody.newName(), requestBody.newEmail());
  }

}
