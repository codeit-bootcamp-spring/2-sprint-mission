package com.sprint.mission.discodeit.adapter.inbound.user;

import com.sprint.mission.discodeit.adapter.inbound.user.dto.UserCreateRequest;
import com.sprint.mission.discodeit.adapter.inbound.user.dto.UserLoginRequest;
import com.sprint.mission.discodeit.adapter.inbound.user.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.core.user.usecase.LoginUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.crud.dto.CreateUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.crud.dto.UpdateUserCommand;
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
