package com.sprint.mission.discodeit.core.user.controller;

import com.sprint.mission.discodeit.core.content.controller.BinaryContentDtoMapper;
import com.sprint.mission.discodeit.core.user.controller.request.UserCreateRequest;
import com.sprint.mission.discodeit.core.user.controller.request.UserLoginRequest;
import com.sprint.mission.discodeit.core.user.controller.request.UserUpdateRequest;
import com.sprint.mission.discodeit.core.user.controller.response.UserResponse;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.usecase.dto.CreateUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.LoginUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UpdateUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserResult;
import java.util.UUID;

public final class UserDtoMapper {

  private UserDtoMapper() {
  }

  public static UserResponse toCreateResponse(User user) {
    return new UserResponse(user.getId(), user.getName(), user.getEmail(),
        BinaryContentDtoMapper.toCreateResponse(user.getProfile()),
        user.getUserStatus().isOnline());
  }

  public static UserResponse toCreateResponse(UserResult result) {
    if (result.profile() == null) {
      return new UserResponse(result.id(), result.username(), result.email(), null,
          result.online());
    } else {
      return new UserResponse(result.id(), result.username(), result.email(),
          BinaryContentDtoMapper.toCreateResponse(result.profile()),
          result.online());
    }
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
