package com.sprint.mission.discodeit.core.user.controller;

import com.sprint.mission.discodeit.core.user.controller.dto.UserCreateRequest;
import com.sprint.mission.discodeit.core.user.controller.dto.UserLoginRequest;
import com.sprint.mission.discodeit.core.user.controller.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserCreateCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserLoginCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserUpdateCommand;
import java.util.UUID;

public final class UserDtoMapper {

  private UserDtoMapper() {
  }

  public static UserCreateCommand toCreateUserCommand(UserCreateRequest requestBody) {
    return new UserCreateCommand(requestBody.username(), requestBody.email(),
        requestBody.password());
  }

  public static UserLoginCommand toLoginUserCommand(UserLoginRequest requestBody) {
    return new UserLoginCommand(requestBody.username(), requestBody.password());
  }

  public static UserUpdateCommand toUpdateUserCommand(UUID userId, UserUpdateRequest requestBody) {
    return new UserUpdateCommand(userId, requestBody.newUsername(), requestBody.newEmail(),
        requestBody.newPassword());
  }

}
