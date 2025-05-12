package com.sprint.mission.discodeit.core.user.controller;

import com.sprint.mission.discodeit.core.user.controller.dto.UserCreateRequest;
import com.sprint.mission.discodeit.core.user.controller.dto.UserLoginRequest;
import com.sprint.mission.discodeit.core.user.controller.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.core.user.usecase.dto.CreateUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.LoginUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UpdateUserCommand;
import java.util.UUID;

public final class UserDtoMapper {

  private UserDtoMapper() {
  }

  public static CreateUserCommand toCreateUserCommand(UserCreateRequest requestBody) {
    return new CreateUserCommand(requestBody.username(), requestBody.email(),
        requestBody.password());
  }

  public static LoginUserCommand toLoginUserCommand(UserLoginRequest requestBody) {
    return new LoginUserCommand(requestBody.username(), requestBody.password());
  }

  public static UpdateUserCommand toUpdateUserCommand(UUID userId, UserUpdateRequest requestBody) {
    return new UpdateUserCommand(userId, requestBody.newUsername(), requestBody.newEmail(),
        requestBody.newPassword());
  }

}
